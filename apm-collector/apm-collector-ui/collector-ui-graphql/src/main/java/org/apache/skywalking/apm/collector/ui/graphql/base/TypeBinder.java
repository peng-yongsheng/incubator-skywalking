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

import graphql.language.EnumTypeDefinition;
import graphql.language.InputObjectTypeDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.TypeDefinition;
import java.util.Collection;
import java.util.List;
import org.apache.skywalking.apm.collector.core.UnexpectedException;
import org.apache.skywalking.apm.collector.core.util.FileScanUtils;

/**
 * @author peng-yongsheng
 */
class TypeBinder {

    private final OperationTypeContainer operationTypeContainer;
    private final TypeContainer typeContainer;
    private final QueryTypeBinder queryTypeBinder;

    TypeBinder(OperationTypeContainer operationTypeContainer,
        TypeContainer typeContainer,
        QueryTypeBinder queryTypeBinder) {
        this.operationTypeContainer = operationTypeContainer;
        this.typeContainer = typeContainer;
        this.queryTypeBinder = queryTypeBinder;

        loadClass();
    }

    void bind(Collection<TypeDefinition> typeDefinitions) {
        typeDefinitions.forEach(typeDefinition -> {
            if (typeDefinition instanceof ObjectTypeDefinition) {
                if (operationTypeContainer.isQueryTypeDefinition(typeDefinition.getName())) {
                    queryTypeBinder.bind((ObjectTypeDefinition)typeDefinition);
                } else if (!typeContainer.containsObjectTypeClass(typeDefinition.getName())) {
                    throw new UnexpectedException("Could not found the object type definition named " + typeDefinition.getName());
                }
            } else if (typeDefinition instanceof InputObjectTypeDefinition) {
                if (!typeContainer.containsInputObjectTypeClass(typeDefinition.getName())) {
                    throw new UnexpectedException("Could not found the input object type definition named " + typeDefinition.getName());
                }
            } else if (typeDefinition instanceof EnumTypeDefinition) {
                if (!typeContainer.containsEnumTypeClass(typeDefinition.getName())) {
                    throw new UnexpectedException("Could not found the enum type definition named " + typeDefinition.getName());
                }
            }
        });
    }

    private void loadClass() {
        AnnotationFilter annotationFilter = new AnnotationFilter();
        List<String> scanFiles = FileScanUtils.scan("org.apache.skywalking.apm.collector.ui.graphql");
        List<Class> objectTypeClasses = annotationFilter.filter(scanFiles, GraphQLObjectType.class);
        objectTypeClasses.forEach(clazz -> typeContainer.putObjectTypeClass(clazz.getSimpleName(), clazz));

        List<Class> inputObjectTypeClasses = annotationFilter.filter(scanFiles, GraphQLInputObjectType.class);
        inputObjectTypeClasses.forEach(clazz -> typeContainer.putInputObjectTypeClass(clazz.getSimpleName(), clazz));

        List<Class> enumTypeClasses = annotationFilter.filter(scanFiles, GraphQLEnumType.class);
        enumTypeClasses.forEach(clazz -> typeContainer.putEnumTypeClass(clazz.getSimpleName(), clazz));
    }
}
