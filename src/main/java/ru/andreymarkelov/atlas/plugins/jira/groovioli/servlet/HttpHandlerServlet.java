package ru.andreymarkelov.atlas.plugins.jira.groovioli.servlet;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.RestData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.RestDataManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.util.ScriptException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.split;

public class HttpHandlerServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(HttpHandlerServlet.class);

    private final RestDataManager restDataManager;
    private final ScriptManager scriptManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final UserUtil userUtil;

    public HttpHandlerServlet(
            RestDataManager restDataManager,
            ScriptManager scriptManager,
            JiraAuthenticationContext jiraAuthenticationContext,
            UserUtil userUtil) {
        this.restDataManager = restDataManager;
        this.scriptManager = scriptManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.userUtil = userUtil;
    }

    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String[] pathParts = split(path, '/');

        if (ArrayUtils.isEmpty(pathParts)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "At least one path after /groovioli should be set");
            return;
        }

        RestData restData = restDataManager.getByPath(pathParts[0]);
        if (restData == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No action for current path was found");
            return;
        }

        if (StringUtils.isBlank(restData.getPerformer())) {
            if (!jiraAuthenticationContext.isLoggedInUser()) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User should be authorized for this action");
                return;
            }
        } else {
            ApplicationUser performerUser = userUtil.getUserByName(restData.getPerformer());
            if (performerUser == null) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Performer user is not found for this action");
                return;
            }
            jiraAuthenticationContext.setLoggedInUser(performerUser);
        }

        // headers
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();

            List<String> value = new ArrayList<>();
            headers.put(headerName, value);

            Enumeration<String> headerValues = req.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                value.add(headerValues.nextElement());
            }
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("httpMethod", req.getMethod());
        parameters.put("httpPath", path);
        parameters.put("httpContentType", req.getContentType());
        parameters.put("httpHeaders", headers);
        parameters.put("httpRemoteAddr", req.getRemoteAddr());
        parameters.put("httpParameterMap", req.getParameterMap());
        parameters.put("httpPayload", inputStreamToString(req.getInputStream()));
        parameters.put("performerUser", jiraAuthenticationContext.getLoggedInUser());
        try {
            scriptManager.executeScript(restData.getScript(), parameters);
        } catch (ScriptException e) {
            log.error("Error executing HTTP handler script for path:{}", restData.getPath(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8.name()));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        }
        return "";
    }
}
