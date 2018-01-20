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

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
class QueryInvoker {

    private final Logger logger = LoggerFactory.getLogger(QueryInvoker.class);

    private final Object instance;
    private final Method method;

    QueryInvoker(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    Object invoke(Object... args) {
        try {
            return this.method.invoke(instance, args);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        return "hello";
    }
}
