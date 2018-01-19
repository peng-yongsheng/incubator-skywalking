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

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

/**
 * @author peng-yongsheng
 */
public class GraphQLSchemaBinder {

    private final SchemaParser schemaParser;
    private final TypeDefinitionRegistry typeDefinitionRegistry;

    private GraphQLSchemaBinder() {
        this.schemaParser = new SchemaParser();
        this.typeDefinitionRegistry = new TypeDefinitionRegistry();
    }

    public static GraphQLSchemaBinder newBinder() {
        return new GraphQLSchemaBinder();
    }

    public GraphQLSchemaBinder file(String fileName) {
        this.typeDefinitionRegistry.merge(schemaParser.parse(loadSchema(fileName)));
        return this;
    }

    public GraphQLSchemaBinder protocol(String protocol) {
        this.typeDefinitionRegistry.merge(schemaParser.parse(protocol));
        return this;
    }

    public GraphQL build() {
        RuntimeWiring.Builder builder = newRuntimeWiring();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, builder.build());

        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private File loadSchema(final String schema) {
        return new File(GraphQLSchemaBinder.class.getClassLoader().getResource(schema).getFile());
    }
}
