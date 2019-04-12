package org.apache.skywalking.apm.webapp.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.skywalking.apm.webapp.service.SSOservice;
import org.apache.skywalking.apm.webapp.vo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liu-XinYuan
 */
@RestController
@RequestMapping("/user")
public class ProjectsController {
    private Logger logger = LoggerFactory.getLogger(ProjectsController.class);

    @Autowired
    SSOservice ssOservice;

    @GetMapping(value = "projects")
    public R getProjects(HttpServletRequest request) {
        List<String> projects;
        String env = null;
        try {
            HttpSession session = request.getSession();
            // session.getId()
            String userId = session.getAttribute("userId").toString();
            env = session.getAttribute("env").toString();
            projects = ssOservice.getProjects(userId, env);
        } catch (Exception e) {
            logger.error("", e);
            return new R(500, "server internal error", null, env);
        }

        return new R(200, "ok", projects, env);
    }
}
