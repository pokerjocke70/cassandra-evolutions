package org.sundqvist.repository.cassandra.evolutions.domain;

import java.util.Collections;
import java.util.List;

/**
 * Models the list of database changes
 *
 * @author Joakim Sundqvist
 * @since 21/06/15
 */
public class ChangeSets {

    private final List<Meta> changeSets;

    private final String keyspace;


    public ChangeSets(List<Meta> changes, String keyspace) {
        this.changeSets = changes;
        this.keyspace = keyspace;
    }

    public List<Meta> getChangeSets() {
        Collections.sort(changeSets);
        return Collections.unmodifiableList(changeSets);
    }

    public String getKeyspace() {
        return keyspace;
    }
}
