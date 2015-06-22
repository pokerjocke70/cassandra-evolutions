package com.bisnode.infra.cassandra.evolutions.domain;

/**
 * Models a specific database change
 *
 * @author Joakim Sundqvist
 * @since 21/06/15
 */
public class Meta implements Comparable<Meta> {

    private String author;

    private String cql;

    private int id;

    public Meta(String author, String cql, int id) {
        this.author = author;
        this.cql = cql;
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getCql() {
        return cql;
    }

    public int getId() {
        return id;
    }


    @Override
    public int compareTo(Meta o) {
        if (o == null || id < o.id) {
            return 0;
        } else if (id == o.getId()) {
            return -1;
        } else {
            return 1;
        }
    }
}