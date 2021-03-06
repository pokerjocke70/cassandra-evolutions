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
