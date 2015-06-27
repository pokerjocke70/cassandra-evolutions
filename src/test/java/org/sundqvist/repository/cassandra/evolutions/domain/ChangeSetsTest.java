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

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link ChangeSets}
 *
 * @author Joakim Sundqvist
 * @since 22/06/15
 */
public class ChangeSetsTest {

    @Test
    public void testGetChangeSets() throws Exception {

        final ChangeSets demo = new ChangeSets(Arrays.asList(new Meta("", "", 2), new Meta("", "", 5), new Meta("", "", 0), new Meta("", "", 1)), "demo");

        assertEquals(demo.getChangeSets().get(0).getId(), 0);
        assertEquals(demo.getChangeSets().get(3).getId(), 5);

    }
}