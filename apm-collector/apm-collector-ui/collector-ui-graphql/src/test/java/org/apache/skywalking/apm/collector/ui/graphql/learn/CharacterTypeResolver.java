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

import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;

/**
 * @author peng-yongsheng
 */
public class CharacterTypeResolver implements TypeResolver {

    @Override public GraphQLObjectType getType(TypeResolutionEnvironment environment) {
        Object object = environment.getObject();
        System.out.println("CharacterTypeResolver: " + object.getClass().getName());
        if (object instanceof Human) {
            return (GraphQLObjectType)environment.getSchema().getType("Human");
        }
        return null;
    }
}
