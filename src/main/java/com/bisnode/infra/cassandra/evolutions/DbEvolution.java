package com.bisnode.infra.cassandra.evolutions;

import com.bisnode.infra.cassandra.evolutions.domain.Changesets;
import com.bisnode.infra.cassandra.evolutions.domain.Meta;
import com.datastax.driver.core.*;
import com.google.gson.Gson;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Joakim Sundqvist
 * @since 21/06/15
 */
public class DbEvolution {

    private final Session session;

    private final Changesets changesets;

    private final KeyspaceStrategy keyspaceStrategy;

    private static final String EVOLUTION_TABLE = "CREATE TABLE IF NOT EXISTS %s.evolutions(\n" +
            "id int,\n" +
            "author text,\n" +
            "applied_date timestamp,\n" +
            "content text,\n" +
            "PRIMARY KEY (id))";

    private static final String KEYSPACE_DEFINITION = "CREATE KEYSPACE IF NOT EXISTS %s WITH strategy = %s;";

    private static final String EVOLUTION_INSERT = "INSERT INTO %s.evolutions (id, author, applied_date, content) values (%s, '%s', '%s', '%s');";

    /**
     * @param session
     * @param keyspaceStrategy
     */
    public DbEvolution(@Nonnull Session session, @Nonnull KeyspaceStrategy keyspaceStrategy) throws IOException {
        this.session = session;
        this.keyspaceStrategy = keyspaceStrategy;
        try (final InputStreamReader json = new InputStreamReader(getClass().getResourceAsStream("/cassandra-evolutions.json"))) {
            this.changesets = new Gson().fromJson(json, Changesets.class);
            System.out.printf("Loaded %s change(s) into memory from cassandra-evolutions.json.%n", changesets.getChangesets().size());
        }
    }


    public void execute() {
        ensureKeyspace(session);
        ensureMetaTable(session);
        final int latestEvolution = getLatestEvolution(session);
        final List<Meta> changesToApply = getChangesToApply(latestEvolution);
        applyChanges(changesToApply, session);
    }


    private void applyChanges(List<Meta> evolutionsToApply, Session session) {
        if (evolutionsToApply.isEmpty()) {
            System.out.println("No changes to apply. Cassandra is up-to-date");
        } else {
            for (Meta meta : evolutionsToApply) {
                session.execute(meta.getCql());
                session.execute(String.format(EVOLUTION_INSERT, changesets.getKeyspace(), meta.getId(), meta.getAuthor(), new Date().getTime(), meta.getCql()));
                System.out.printf("Applied change %s by author %s.%n", meta.getId(), meta.getAuthor());
            }
        }
    }

    private List<Meta> getChangesToApply(int latestEvolution) {
        List<Meta> evolutionsToApply = new LinkedList<>();
        for (Meta meta : changesets.getChangesets()) {
            if (meta.getId() > latestEvolution) {
                evolutionsToApply.add(meta);
            }
        }
        return evolutionsToApply;
    }

    private int getLatestEvolution(Session session) {
        final SimpleStatement statement = new SimpleStatement(String.format("select id from %s.evolutions;", changesets.getKeyspace()));
        statement.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM);
        final ResultSet execute = session.execute(statement);
        int max = 0;
        for (Row row : execute) {
            final int id = row.getInt("id");
            if (id > max) {
                max = id;
            }
        }
        System.out.printf("Latest change applied to cassandra is: %s%n", max);
        return max;
    }


    private void ensureMetaTable(Session session) {
        final String cql = String.format(EVOLUTION_TABLE, changesets.getKeyspace());
        System.out.println(cql);
        session.execute(cql);
    }

    private void ensureKeyspace(Session session) {
        final String cql = String.format(KEYSPACE_DEFINITION, changesets.getKeyspace(), keyspaceStrategy.getStrategyAsString());
        System.out.println(cql);
        session.execute(cql);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final Cluster localhost = new Cluster.Builder().addContactPoint("localhost").build();
        final DbEvolution evolution = new DbEvolution(localhost.connect(), new SimpleKeyspaceStrategy(1));
        evolution.execute();
        Thread.sleep(25000L);
        localhost.close();
    }
}
