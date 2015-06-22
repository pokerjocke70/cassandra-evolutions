package com.bisnode.infra.cassandra.evolutions.domain;

import java.util.Collections;
import java.util.List;

/**
 * @author Joakim Sundqvist
 * @since 21/06/15
 */
public class Changesets {

    private final List<Meta> changesets;

    private final String keyspace;


    public Changesets(List<Meta> changes, String keyspace) {
        this.changesets = changes;
        this.keyspace = keyspace;
    }

    public List<Meta> getChangesets() {
        Collections.sort(changesets);
        return Collections.unmodifiableList(changesets);
    }

    public String getKeyspace() {
        return keyspace;
    }
}
