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
class TypeContainer {

    private final Map<String, Class> objectTypeClassMap;
    private final Map<String, Class> inputObjectTypeClassMap;
    private final Map<String, Class> enumTypeClassMap;

    TypeContainer() {
        this.objectTypeClassMap = new HashMap<>();
        this.inputObjectTypeClassMap = new HashMap<>();
        this.enumTypeClassMap = new HashMap<>();
    }

    void putObjectTypeClass(String typeName, Class clazz) {
        objectTypeClassMap.put(typeName, clazz);
    }

    boolean containsObjectTypeClass(String typeName) {
        return objectTypeClassMap.containsKey(typeName);
    }

    void putInputObjectTypeClass(String typeName, Class clazz) {
        inputObjectTypeClassMap.put(typeName, clazz);
    }

    boolean containsInputObjectTypeClass(String typeName) {
        return inputObjectTypeClassMap.containsKey(typeName);
    }

    void putEnumTypeClass(String typeName, Class clazz) {
        enumTypeClassMap.put(typeName, clazz);
    }

    boolean containsEnumTypeClass(String typeName) {
        return enumTypeClassMap.containsKey(typeName);
    }
}
