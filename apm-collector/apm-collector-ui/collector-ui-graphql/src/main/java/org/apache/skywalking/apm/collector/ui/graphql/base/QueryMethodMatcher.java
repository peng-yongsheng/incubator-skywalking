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

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author peng-yongsheng
 */
public class QueryMethodMatcher implements DataFetcher {

    private final QueryInvokerContainer invokerContainer;
    private final TypeContainer typeContainer;

    public QueryMethodMatcher(QueryInvokerContainer invokerContainer,
        TypeContainer typeContainer) {
        this.invokerContainer = invokerContainer;
        this.typeContainer = typeContainer;
    }

    @Override public Object get(DataFetchingEnvironment environment) {
        GraphQLFieldDefinition fieldDefinition = environment.getFieldDefinition();
        StringBuilder methodDefinition = new StringBuilder();
        QueryMethodBuilder.appendMethodName(methodDefinition, fieldDefinition.getName());
        QueryMethodBuilder.appendReturnType(methodDefinition, fieldDefinition.getType().getName());
        QueryMethodBuilder.appendInputHead(methodDefinition);

        List<Object> arguments = new LinkedList<>();
        Map<String, Object> values = environment.getArguments();

        fieldDefinition.getArguments().forEach(argument -> {
            QueryMethodBuilder.appendInputField(methodDefinition, argument.getType().getName());

            arguments.add(dataInvoke(argument, values));
        });

        QueryInvoker invoker = invokerContainer.getInvoker(QueryMethodBuilder.toString(methodDefinition));

        return invoker.invoke(arguments.toArray());
    }

    private Object dataInvoke(GraphQLArgument argument, Map<String, Object> values) {
        if (DataTypeTransform.isBaseType(argument.getType().getName())) {
            return values.get(argument.getName());
        }
        return null;
    }
}
