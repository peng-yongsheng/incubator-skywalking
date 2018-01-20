/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.collector.ui.graphql.base;

import graphql.ExecutionResult;
import graphql.GraphQL;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author peng-yongsheng
 */
public class SchemaBinderTestCase {

    @Test
    public void testFileSchemaBuild() {
        GraphQL graphQL = SchemaBinder.newBinder()
            .file("simple.graphqls")
            .build();

        ExecutionResult executionResult = graphQL.execute("{test(id: \"1\")}");
        String result = executionResult.getData().toString();
        Assert.assertEquals("{test=null}", result);
    }

    @Test
    public void testProtocolSchemaBuild() {
        GraphQL graphQL = SchemaBinder.newBinder()
            .protocol("schema {query: QueryType}" +
                "type QueryType {" +
                "    test(id: String, name: String): String" +
                "}")
            .build();

        ExecutionResult executionResult = graphQL.execute("{test(id: \"1\", name: \"1\")}");
        String result = executionResult.getData().toString();
        Assert.assertEquals("{test=hello}", result);
    }

    @Test
    public void testTypeBinder() {
        GraphQL graphQL = SchemaBinder.newBinder()
            .protocol("schema {query: QueryType}" +
                "type QueryType {" +
                "    test(id: String, name: String): String" +
                "}" +
                "type Human {" +
                "    id: ID!" +
                "name: String!" +
                "}" +
                "input Duration {" +
                "   start: String!" +
                "   end: String!" +
                "}" +
                "enum Episode {" +
                "   NEWHOPE" +
                "   EMPIRE" +
                "   JEDI" +
                "}")
            .build();
    }

    @Test
    public void testTypeExtendsBinder() {
        GraphQL graphQL = SchemaBinder.newBinder()
            .protocol("schema {" +
                "   query: QueryType" +
                "}" +
                "type QueryType {" +
                "    test(id: String, name: String): String" +
                "}" +
                "type Human {" +
                "    id: ID!" +
                "name: String!" +
                "}" +
                "input Duration {" +
                "   start: String!" +
                "   end: String!" +
                "}" +
                "enum Episode {" +
                "   NEWHOPE" +
                "   EMPIRE" +
                "   JEDI" +
                "}" +
                "extend type QueryType {" +
                "   test(id: String): String" +
                "}")
            .build();
    }
}
