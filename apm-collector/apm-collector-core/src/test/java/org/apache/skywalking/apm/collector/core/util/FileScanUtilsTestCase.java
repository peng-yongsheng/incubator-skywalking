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

package org.apache.skywalking.apm.collector.core.util;

import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author peng-yongsheng
 */
public class FileScanUtilsTestCase {

    private static final String PACKAGE_NAME = "org.apache.skywalking.apm.collector.core.scan";

    @Test
    public void testScan() throws IOException {
        List<String> files = FileScanUtils.scan(PACKAGE_NAME);

        Assert.assertEquals("org.apache.skywalking.apm.collector.core.scan.TestScanB", files.get(0));
        Assert.assertEquals("org.apache.skywalking.apm.collector.core.scan.TestScanA", files.get(1));
    }
}