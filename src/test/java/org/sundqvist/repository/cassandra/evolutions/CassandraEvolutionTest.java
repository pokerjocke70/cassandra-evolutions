/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sundqvist.repository.cassandra.evolutions;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Cassandra integration test that loads evolutions from a file and verifies that evolutions are created in the embedded cassandra database.
 *
 * @author Joakim Sundqvist
 * @since 22/06/15
 */
public class CassandraEvolutionTest {

    private static Cluster localhost;

    @Test
    public void testExecute() throws Exception {

        final Session session = localhost.connect();
        final CassandraEvolution evolution = new CassandraEvolution(session, new SimpleKeyspaceStrategy(1));
        evolution.execute();
        assertEquals(session.execute("select count(*) from evolutions.evolutions").one().getLong(0), 6L);
        assertEquals(session.execute("select count(*) from evolutions.evolutions6").one().getLong(0), 0L);
        assertEquals(session.execute("select author from evolutions.evolutions where id = 5").one().getString(0), "kalle");
        localhost.close();
    }

    @BeforeClass
    public static void testLoadCassandra() throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra("cassandra-conf.yaml");
        //  TODO - add dynamic port to contact point
        localhost = new Cluster.Builder().addContactPoint("localhost").build();
    }


    // TODO - add dynamic yaml with free ports in config
    private void createYamlFile(int port, int port2) {


    }
}