package org.sundqvist.repository.cassandra.evolutions;

import com.datastax.driver.core.Cluster;
import org.junit.Test;

/**
 * @author Joakim Sundqvist
 * @since 22/06/15
 */
public class CassandraEvolutionTest {

    @Test
    public void testExecute() throws Exception {
        final Cluster localhost = new Cluster.Builder().addContactPoint("localhost").build();
        final CassandraEvolution evolution = new CassandraEvolution(localhost.connect(), new SimpleKeyspaceStrategy(1));
        evolution.execute();
        localhost.close();
    }
}