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

import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.TypeName;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import org.apache.skywalking.apm.collector.core.util.FileScanUtils;

/**
 * @author peng-yongsheng
 */
class QueryTypeBinder {

    private final QueryInvokerContainer invokerContainer;

    QueryTypeBinder(QueryInvokerContainer invokerContainer) throws GraphQLSchemaException {
        this.invokerContainer = invokerContainer;
        loadClass();
    }

    void bind(ObjectTypeDefinition queryTypeDefinition) throws MethodNotFoundException {
        for (FieldDefinition fieldDefinition : queryTypeDefinition.getFieldDefinitions()) {
            StringBuilder methodDefinition = new StringBuilder();
            QueryMethodBuilder.appendMethodName(methodDefinition, fieldDefinition.getName());
            QueryMethodBuilder.appendReturnType(methodDefinition, ((TypeName)fieldDefinition.getType()).getName());
            QueryMethodBuilder.appendInputHead(methodDefinition);

            fieldDefinition.getInputValueDefinitions().forEach(inputValueDefinition -> {
                QueryMethodBuilder.appendInputField(methodDefinition, ((TypeName)inputValueDefinition.getType()).getName());
            });

            if (!invokerContainer.contains(QueryMethodBuilder.toString(methodDefinition))) {
                throw new MethodNotFoundException("The method: " + QueryMethodBuilder.toString(methodDefinition) + "not found.");
            }
        }
    }

    private void loadClass() throws GraphQLSchemaException {
        AnnotationFilter annotationFilter = new AnnotationFilter();
        List<String> scanFiles = FileScanUtils.scan("org.apache.skywalking.apm.collector.ui.graphql");
        List<Class> queryTypeClasses = annotationFilter.filter(scanFiles, GraphQLQueryType.class);

        for (Class queryTypeClass : queryTypeClasses) {
            Method[] declaredMethods = queryTypeClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                StringBuilder methodDefinition = new StringBuilder();
                QueryMethodBuilder.appendMethodName(methodDefinition, method.getName());
                QueryMethodBuilder.appendReturnType(methodDefinition, method.getReturnType().getSimpleName());
                QueryMethodBuilder.appendInputHead(methodDefinition);

                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    QueryMethodBuilder.appendInputField(methodDefinition, parameter.getType().getSimpleName());
                }

                Object instance = null;
                try {
                    instance = queryTypeClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new ReflectionException(e.getMessage());
                }

                invokerContainer.put(QueryMethodBuilder.toString(methodDefinition), new QueryInvoker(instance, method));
            }
        }
    }
}
