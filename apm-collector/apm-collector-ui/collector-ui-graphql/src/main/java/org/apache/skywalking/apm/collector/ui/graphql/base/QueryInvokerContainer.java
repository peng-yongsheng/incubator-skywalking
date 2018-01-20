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

import java.util.HashMap;
import java.util.Map;

/**
 * @author peng-yongsheng
 */
class QueryInvokerContainer {

    private final Map<String, QueryInvoker> invokerMap;

    QueryInvokerContainer() {
        this.invokerMap = new HashMap<>();
    }

    boolean contains(String invokerName) {
        return invokerMap.containsKey(invokerName);
    }

    void put(String invokerName, QueryInvoker invoker) throws DuplicatedQueryMethodException {
        if (invokerMap.containsKey(invokerName)) {
            String message = "Duplicated method definition: " + invokerName + " in Class { " + System.lineSeparator();
            message = message + invokerMap.get(invokerName).getClassName() + System.lineSeparator();
            message = message + invoker.getClassName() + System.lineSeparator();
            message = message + "}";
            throw new DuplicatedQueryMethodException(message);
        }
        invokerMap.put(invokerName, invoker);
    }

    QueryInvoker getInvoker(String methodDefinition) {
        return invokerMap.get(methodDefinition);
    }
}
