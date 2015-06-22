package com.bisnode.infra.cassandra.evolutions;

import com.bisnode.infra.cassandra.evolutions.domain.ChangeSets;
import com.bisnode.infra.cassandra.evolutions.domain.Meta;
import com.datastax.driver.core.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages all tasks related to Cassandra database evolutions.
 *
 * @author Joakim Sundqvist
 * @since 21/06/15
 */
public class DbEvolution {

    private final Session session;

    private final ChangeSets changeSets;

    private final KeyspaceStrategy keyspaceStrategy;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String EVOLUTION_TABLE = "CREATE TABLE IF NOT EXISTS %s.evolutions(\n" +
            "id int,\n" +
            "author text,\n" +
            "applied_date timestamp,\n" +
            "content text,\n" +
            "PRIMARY KEY (id))";

    private static final String KEYSPACE_DEFINITION = "CREATE KEYSPACE IF NOT EXISTS %s WITH REPLICATION = %s;";

    private static final String EVOLUTION_INSERT = "INSERT INTO %s.evolutions (id, author, applied_date, content) values (%s, '%s', '%s', '%s');";

    /**
     * Constructor
     *
     * @param session Cassandra session
     * @param keyspaceStrategy keyspace strategy
     */
    public DbEvolution(Session session, KeyspaceStrategy keyspaceStrategy) throws IOException {
        this(session, keyspaceStrategy, null);
    }

    /**
     * Constructor
     *
     * @param session Cassandra session
     * @param keyspaceStrategy keyspace strategy
     * @param evolutionFileName filename of evolutions, uses default value if null
     * @throws IOException
     */
    public DbEvolution(Session session, KeyspaceStrategy keyspaceStrategy, String evolutionFileName) throws IOException {
        if (session == null) {
            throw new NullPointerException("session must not be null");
        }
        if (keyspaceStrategy == null) {
            throw new NullPointerException("strategy must not be null");
        }
        this.session = session;
        this.keyspaceStrategy = keyspaceStrategy;
        final String fileName = evolutionFileName == null ? "cassandra-evolutions.json" : evolutionFileName;

        logger.info("Loading evolutions from {}", fileName);
        try (final InputStreamReader json = new InputStreamReader(getClass().getResourceAsStream("/" + fileName))) {
            this.changeSets = new Gson().fromJson(json, ChangeSets.class);
            logger.info("Loaded {} change(s) into memory from cassandra-evolutions.json.", changeSets.getChangeSets().size());
        }
    }

    /**
     * Coordinates all the work that needs to be done to get the database in sync.
     *
     */
    public void execute() {
        ensureKeyspace();
        ensureMetaTable();
        final int latestEvolution = getLatestEvolution();
        final List<Meta> changesToApply = getChangesToApply(latestEvolution);
        applyChanges(changesToApply);
    }

    /**
     * Writes all the changes not applied yet to the database
     *
     * @param evolutionsToApply list of {@link Meta} to apply
     */
    private void applyChanges(List<Meta> evolutionsToApply) {
        if (evolutionsToApply.isEmpty()) {
            logger.info("No changes to apply. Cassandra is up-to-date");
        } else {
            for (Meta meta : evolutionsToApply) {
                session.execute(meta.getCql());
                session.execute(String.format(EVOLUTION_INSERT, changeSets.getKeyspace(), meta.getId(), meta.getAuthor(), new Date().getTime(), meta.getCql()));
                logger.info("Applied change {} by author {}.", meta.getId(), meta.getAuthor());
            }
        }
    }

    /**
     * Creates a list of {@link Meta} that has not been applied yet to the database.
     *
     * @param latestEvolution the value of the latest evolution written.
     * @return not null
     */
    private List<Meta> getChangesToApply(int latestEvolution) {
        List<Meta> evolutionsToApply = new LinkedList<>();
        for (Meta meta : changeSets.getChangeSets()) {
            if (meta.getId() > latestEvolution) {
                evolutionsToApply.add(meta);
            }
        }
        return evolutionsToApply;
    }

    /**
     * Finds the id of the latest evolution from the database
     *
     * @return 0 if nothing was found in db
     */
    private int getLatestEvolution() {
        final SimpleStatement statement = new SimpleStatement(String.format("select id from %s.evolutions;", changeSets.getKeyspace()));
        statement.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM);
        final ResultSet execute = session.execute(statement);
        int max = 0;
        for (Row row : execute) {
            final int id = row.getInt("id");
            if (id > max) {
                max = id;
            }
        }
        logger.info("Latest change applied to cassandra is: {}", max);
        return max;
    }

    /**
     * Creates the evolution table if needed
     */
    private void ensureMetaTable() {
        final String cql = String.format(EVOLUTION_TABLE, changeSets.getKeyspace());
        logger.info(cql);
        session.execute(cql);
    }

    /**
     * Creates the keyspace if needed - the keyspace defined in the evolutions mapping.
     */
    private void ensureKeyspace() {
        final String cql = String.format(KEYSPACE_DEFINITION, changeSets.getKeyspace(), keyspaceStrategy.getStrategyAsString());
        logger.info(cql);
        session.execute(cql);
    }

}
