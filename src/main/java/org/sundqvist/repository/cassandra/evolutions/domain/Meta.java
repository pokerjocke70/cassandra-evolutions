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
package org.sundqvist.repository.cassandra.evolutions.domain;

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
            return -1;
        } else if (id == o.getId()) {
            return 0;
        } else {
            return 1;
        }
    }
}