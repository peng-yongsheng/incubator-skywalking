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

/**
 * @author peng-yongsheng
 */
class DataTypeTransform {

    private static final String TYPE_INT = "int";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_TRANSFORM_INT = "Integer";
    private static final String TYPE_TRANSFORM_STRING = "String";
    private static final String TYPE_TRANSFORM_DOUBLE = "Double";
    private static final String TYPE_TRANSFORM_BOOLEAN = "Boolean";

    private DataTypeTransform() {
    }

    static String typeNameTransform(String typeName) {
        if (TYPE_INT.equals(typeName.toLowerCase())) {
            return TYPE_TRANSFORM_INT;
        } else if (TYPE_FLOAT.equals(typeName.toLowerCase())) {
            return TYPE_TRANSFORM_DOUBLE;
        } else {
            return typeName;
        }
    }

    static boolean isBaseType(String typeName) {
        String transformedTypeName = typeNameTransform(typeName);
        return TYPE_TRANSFORM_INT.equals(transformedTypeName)
            || TYPE_TRANSFORM_STRING.equals(transformedTypeName)
            || TYPE_TRANSFORM_BOOLEAN.equals(transformedTypeName)
            || TYPE_TRANSFORM_DOUBLE.equals(transformedTypeName);
    }
}
