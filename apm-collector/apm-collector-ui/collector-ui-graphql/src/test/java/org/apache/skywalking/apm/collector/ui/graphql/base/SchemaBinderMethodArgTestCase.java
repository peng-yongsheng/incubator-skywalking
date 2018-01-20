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
public class SchemaBinderMethodArgTestCase {

    @Test
    public void testInputString() throws GraphQLSchemaException {
        GraphQL graphQL = SchemaBinder.newBinder()
            .protocol("schema {query: StringInputQueryType}" +
                "type StringInputQueryType {" +
                "    test(arg1: String, arg2: String, arg3: String): String" +
                "}")
            .build();

        ExecutionResult executionResult = graphQL.execute("{test(arg1: \"1\", arg2: \"1\", arg3: \"1\")}");
        String result = executionResult.getData().toString();
        Assert.assertEquals("{test=ok}", result);
    }

    @Test
    public void testInputInteger() throws GraphQLSchemaException {
        GraphQL graphQL = SchemaBinder.newBinder()
            .protocol("schema {query: StringInputQueryType}" +
                "type StringInputQueryType {" +
                "    test(arg1: Int, arg2: Int, arg3: Int): Int" +
                "}")
            .build();

        ExecutionResult executionResult = graphQL.execute("{test(arg1: 1, arg2: 1, arg3: 1)}");
        String result = executionResult.getData().toString();
        Assert.assertEquals("{test=0}", result);
    }
}
