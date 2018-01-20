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

import graphql.language.OperationDefinition;
import graphql.language.OperationTypeDefinition;
import graphql.language.TypeName;
import java.util.List;
import org.apache.skywalking.apm.collector.core.UnexpectedException;

/**
 * @author peng-yongsheng
 */
class OperationTypeBinder {

    private final OperationTypeContainer operationTypeContainer;

    OperationTypeBinder(OperationTypeContainer operationTypeContainer) {
        this.operationTypeContainer = operationTypeContainer;
    }

    void bind(List<OperationTypeDefinition> operationTypeDefinitions) {
        operationTypeDefinitions.forEach(typeDefinition -> {
            if (OperationDefinition.Operation.QUERY.name().toLowerCase().equals(typeDefinition.getName())) {
                operationTypeContainer.setQueryTypeName(((TypeName)typeDefinition.getType()).getName());
            } else if (OperationDefinition.Operation.MUTATION.name().toLowerCase().equals(typeDefinition.getName())) {

            } else if (OperationDefinition.Operation.SUBSCRIPTION.name().toLowerCase().equals(typeDefinition.getName())) {

            } else {
                throw new UnexpectedException("");
            }
        });
    }
}
