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

import java.io.File;

/**
 * @author peng-yongsheng
 */
public class PathUtils {

    private PathUtils() {
    }

    public static String pathToPackage(String path) {
        if (path.startsWith(Const.FILE_PATH_SEPARATOR)) {
            path = path.substring(1);
        }

        return path.replaceAll(Const.FILE_PATH_SEPARATOR, Const.PACKAGE_SEPARATOR);
    }

    public static String packageToPath(String packageName) {
        return packageName.replaceAll(Const.PACKAGE_SEPARATOR_REGEX, File.separator);
    }

    public static String trimSuffix(String name) {
        int dotIndex = name.indexOf(Const.PACKAGE_SEPARATOR);
        if (-1 == dotIndex) {
            return name;
        }

        return name.substring(0, dotIndex);
    }
}
