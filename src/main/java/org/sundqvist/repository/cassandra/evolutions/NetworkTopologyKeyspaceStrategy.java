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
        return String.format("{ 'class' : 'NetworkTopologyStrategy', '%s' : '%s' }", datacenter, replicationFactor);
    }
}
