package org.sundqvist.repository.cassandra.evolutions;

/**
 * Network topology strategy
 *
 * TODO - add support for multiple datacenters
 *
 * @author Joakim Sundqvist
 * @since 22/06/15
 */
public class NetworkTopologyKeyspaceStrategy implements KeyspaceStrategy {

    private final String datacenter;

    private final int replicationFactor;


    /**
     * Constructor
     *
     * @param datacenter name of the datacenter, maps to the config in cassandra.yaml on the Cassandra server
     * @param replicationFactor number of replicas to replicate to
     */
    public NetworkTopologyKeyspaceStrategy(String datacenter, int replicationFactor) {
        this.datacenter = datacenter;
        this.replicationFactor = replicationFactor;
    }

    @Override
    public String getStrategyAsString(){
        return String.format("{ 'class' : 'NetworkTopologyStrategy', '%s' : '%s }", datacenter, replicationFactor);
    }
}
