# cassandra-evolutions
cassandra-evolutions - A first stab at trying to mimic the behaviour of [flywaydb](http://flywaydb.org/) and [Liquibase](http://www.liquibase.org/) 
but for Cassandra.  


## Travis CI Status [![Build Status](https://travis-ci.org/pokerjocke70/cassandra-evolutions.svg?branch=master)](https://travis-ci.org/pokerjocke70/cassandra-evolutions)

## Getting started

final CassandraEvolution evolution = new CassandraEvolution(session, new SimpleKeyspaceStrategy(1));
evolution.execute();






