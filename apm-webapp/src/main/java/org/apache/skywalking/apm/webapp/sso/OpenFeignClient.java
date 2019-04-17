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

package org.apache.skywalking.apm.webapp.sso;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Liu-XinYuan
 */
@FeignClient(name = "openapi", url = "http://test.openapis.yonghui.cn")
@Component
public interface OpenFeignClient {

    @RequestMapping(value = "/api/v1/yhmds/kfitwork/{userId}/project", method = RequestMethod.GET)
    ResponseEntity<Object> getKFProjects(@PathVariable(name = "userId") String userId);

    @RequestMapping(value = "/api/v1/yhmds/devitwork/{userId}/project", method = RequestMethod.GET)
    ResponseEntity<Object> getDEVProjects(@PathVariable(name = "userId") String userId);

}