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

package org.apache.skywalking.apm.collector.analysis.segment.parser.provider;

import java.util.Properties;
import org.apache.skywalking.apm.collector.analysis.segment.parser.define.AnalysisSegmentParserModule;
import org.apache.skywalking.apm.collector.analysis.segment.parser.define.service.ISegmentParseService;
import org.apache.skywalking.apm.collector.analysis.segment.parser.define.service.ISegmentParserListenerRegister;
import org.apache.skywalking.apm.collector.analysis.segment.parser.provider.parser.SegmentParserListenerManager;
import org.apache.skywalking.apm.collector.analysis.segment.parser.provider.service.SegmentParseService;
import org.apache.skywalking.apm.collector.analysis.segment.parser.provider.service.SegmentParserListenerRegister;
import org.apache.skywalking.apm.collector.core.module.Module;
import org.apache.skywalking.apm.collector.core.module.ModuleProvider;
import org.apache.skywalking.apm.collector.core.module.ServiceNotProvidedException;

/**
 * @author peng-yongsheng
 */
public class AnalysisSegmentParserModuleProvider extends ModuleProvider {

    public static final String NAME = "default";
    private SegmentParserListenerManager listenerManager;

    @Override public String name() {
        return NAME;
    }

    @Override public Class<? extends Module> module() {
        return AnalysisSegmentParserModule.class;
    }

    @Override public void prepare(Properties config) throws ServiceNotProvidedException {
        this.listenerManager = new SegmentParserListenerManager();
        this.registerServiceImplementation(ISegmentParserListenerRegister.class, new SegmentParserListenerRegister(listenerManager));
        this.registerServiceImplementation(ISegmentParseService.class, new SegmentParseService(getManager(), listenerManager));
    }

    @Override public void start(Properties config) throws ServiceNotProvidedException {

    }

    @Override public void notifyAfterCompleted() throws ServiceNotProvidedException {

    }

    @Override public String[] requiredModules() {
        return new String[0];
    }
}