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

package org.apache.skywalking.apm.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.skywalking.apm.webapp.compont.SSOConfiguration;
import org.apache.skywalking.apm.webapp.service.SSOservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liu-XinYuan
 */
@RestController
@RequestMapping("/sso")
public class SSOCallBackController {
    private Logger logger = LoggerFactory.getLogger(SSOCallBackController.class);

    @Autowired
    SSOservice ssOservice;
    @Autowired
    SSOConfiguration ssoConfiguration;

    @GetMapping(value = "callback")
    public void getCode(@RequestParam String code, HttpServletRequest request, String env,
        HttpServletResponse response) {
        StandardSessionFacade session = (StandardSessionFacade)request.getSession(true);
        session.setMaxInactiveInterval(1800);
        String userId = ssOservice.getUserId(code, env);
        session.setAttribute("userId", userId);
        session.setAttribute("env", env);
        try {
            response.sendRedirect(ssoConfiguration.getRedicturl());
        } catch (Exception e) {
            logger.error("", e);
        }

    }
}
