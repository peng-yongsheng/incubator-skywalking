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
import java.util.LinkedList;
import java.util.List;

/**
 * @author peng-yongsheng
 */
public class FileScanUtils {

    private FileScanUtils() {
    }

    public static List<String> scan(String basePackage) {
        String path = FileScanUtils.class.getResource(Const.FILE_PATH_SEPARATOR).getPath();
        return scan(path, basePackage);
    }

    private static List<String> scan(String path, String basePackage) {
        File fileOrDirectory = new File(path + PathUtils.packageToPath(basePackage) + "/");

        List<String> classNameList = new LinkedList<>();

        File[] files = fileOrDirectory.listFiles();
        if (null != files) {
            for (File file : files) {
                if (file.isDirectory()) {
                    List<String> list = scan(path, basePackage + Const.PACKAGE_SEPARATOR + file.getName());
                    classNameList.addAll(list);
                } else if (file.getName().endsWith(ResourceType.CLASS_FILE.getTypeString())) {
                    String className = PathUtils.trimSuffix(file.getName());
                    if (-1 != className.lastIndexOf("$")) {
                        continue;
                    }

                    String result = basePackage + Const.PACKAGE_SEPARATOR + className;
                    classNameList.add(result);
                }
            }
        }

        return classNameList;
    }
}
