package org.sundqvist.repository.cassandra.evolutions;

/**
 * SimpleStrategy for Cassandra network
 *
 * @author Joakim Sundqvist
 * @since 22/06/15
 */
public class SimpleKeyspaceStrategy implements KeyspaceStrategy {


    private final int replicationFactor;

    public SimpleKeyspaceStrategy(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    @Override
    public String getStrategyAsString(){
        return String.format("{ 'class' : 'SimpleStrategy', 'replication_factor': '%s'}", replicationFactor );
    }
}
