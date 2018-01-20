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
class QueryMethodBuilder {

    private QueryMethodBuilder() {
    }

    private static final String SEPARATOR = ":";
    private static final String METHOD = "METHOD";
    private static final String RETURN = "RETURN";
    private static final String INPUT = "INPUT";

    static void appendMethodName(StringBuilder methodDefinition, String methodName) {
        methodDefinition.append(METHOD).append(SEPARATOR).append(methodName).append(SEPARATOR);
    }

    static void appendReturnType(StringBuilder methodDefinition, String returnType) {
        methodDefinition.append(RETURN).append(SEPARATOR).append(returnType).append(SEPARATOR);
    }

    static void appendInputHead(StringBuilder methodDefinition) {
        methodDefinition.append(INPUT).append(SEPARATOR);
    }

    static void appendInputField(StringBuilder methodDefinition, String typeName) {
        methodDefinition.append(typeName).append(SEPARATOR);
    }

    static String toString(StringBuilder methodDefinition) {
        return methodDefinition.substring(0, methodDefinition.length() - 1);
    }
}
