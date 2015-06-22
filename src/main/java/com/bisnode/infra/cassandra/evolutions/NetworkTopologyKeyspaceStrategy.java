package com.bisnode.infra.cassandra.evolutions;

/**
 * @author Joakim Sundqvist
 * @since 22/06/15
 */
public class NetworkTopologyKeyspaceStrategy implements KeyspaceStrategy {

    private final String datacenter;

    private final int replicationFactor;


    public NetworkTopologyKeyspaceStrategy(String datacenter, int replicationFactor) {
        this.datacenter = datacenter;
        this.replicationFactor = replicationFactor;
    }

    @Override
    public String getStrategyAsString(){
        return String.format("{ 'class' : 'NetworkTopologyStrategy', '%s' : '%s }", datacenter, replicationFactor );
    }
}
