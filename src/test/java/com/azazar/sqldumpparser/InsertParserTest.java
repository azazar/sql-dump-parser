/*
 * Copyright (C) 2023 Azazar <spam@azazar.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.azazar.sqldumpparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author Mikhail Yevchenko <spam@uo1.net>
 * @since  May 17, 2023
 */
public class InsertParserTest {

    private Set<String> tableNames;
    private List<Map<String, Object>> capturedInserts;

    @BeforeEach
    void setUp() {
        tableNames = new HashSet<>();
        tableNames.add("users");
        capturedInserts = new ArrayList<>();
    }

    static <K, V> void assertDeepEquals(Map<K, V> m1, Map<K, V> m2) {
        assertEquals(m2.size(), m1.size());
        assertTrue(m1.keySet().containsAll(m1.keySet()));

        for (Map.Entry<K, V> e : m1.entrySet()) {
            assertEquals(e.getValue(), m2.get(e.getKey()));
        }
    }

    @Test
    void testInsertParser() throws Exception {
        String inputSql = "INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30), (2, 'Bob', 25);";
        StringReader inputReader = new StringReader(inputSql);
        
        SqlInsertParseCallback callback = (tableName, values) -> {
            var m = new LinkedHashMap<String, Object>();
            for (Map.Entry<String,SqlValue> e : values.entrySet()) {
                m.put(e.getKey(), e.getValue().getValue());
            }
            capturedInserts.add(m);
        };
        
        SqlInsertParser.parse(inputReader, tableNames, callback);

        assertEquals(2, capturedInserts.size());

        Map<String, Object> expectedValues1 = new LinkedHashMap<>();
        expectedValues1.put("id", 1L);
        expectedValues1.put("name", "Alice");
        expectedValues1.put("age", 30L);

        Map<String, Object> expectedValues2 = new LinkedHashMap<>();
        expectedValues2.put("id", 2L);
        expectedValues2.put("name", "Bob");
        expectedValues2.put("age", 25L);

        assertDeepEquals(expectedValues1, capturedInserts.get(0));
        assertDeepEquals(expectedValues2, capturedInserts.get(1));
    }
    
}
