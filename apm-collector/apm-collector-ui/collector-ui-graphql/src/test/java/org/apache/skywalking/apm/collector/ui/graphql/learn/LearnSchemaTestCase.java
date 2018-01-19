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

package org.apache.skywalking.apm.collector.ui.graphql.learn;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

/**
 * @author peng-yongsheng
 */
public class LearnSchemaTestCase {

    @Test
    public void test() {
        SchemaParser schemaParser = new SchemaParser();

        TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();
        typeDefinitionRegistry.merge(schemaParser.parse(loadSchema("learn.graphqls")));

        Map<String, DataFetcher> dataFetchersMap = new HashMap<>();
        dataFetchersMap.put("human", environment -> {
            String id = environment.getArgument("id");
            System.out.println("id: " + id);
            Human human = new Human();
            human.setId("hello");
            return human;
        });

        dataFetchersMap.put("hero", environment -> {
            String episode = environment.getArgument("episode");
            System.out.println("episode:" + episode);
            Human human = new Human();
            human.setName("pengys");
            return human;
        });

        dataFetchersMap.put("farmer", environment -> {
            Object object = environment.getArgument("duration");
            System.out.println("duration:" + object.getClass().getName());
            Human human = new Human();
            human.setName("farmer");
            return human;
        });

        RuntimeWiring runtimeWiring = newRuntimeWiring()
            .type("QueryType", builder -> builder.dataFetchers(dataFetchersMap))
            .type("Character", builder -> builder.typeResolver(new CharacterTypeResolver()))
            .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = build.execute("{human(id: \"1\"){id}}");
        System.out.println(executionResult.getData().toString());

        executionResult = build.execute("{hero(episode: NEWHOPE){name}}");
        System.out.println(executionResult.getData().toString());

        executionResult = build.execute("{farmer(duration: {start: \"1\", end: \"1\"}){name}}");
        System.out.println(executionResult.getData().toString());
    }

    private File loadSchema(final String s) {
        return new File(LearnSchemaTestCase.class.getClassLoader().getResource(s).getFile());
    }
}
