package ru.andreymarkelov.atlas.plugins.jira.groovioli.servlet;

import org.apache.commons.lang3.ArrayUtils;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.RestDataManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.split;

public class HttpHandlerServlet extends HttpServlet {
    private final RestDataManager restDataManager;

    public HttpHandlerServlet(RestDataManager restDataManager) {
        this.restDataManager = restDataManager;
    }

    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String[] pathParts = split(path, '/');

        if (ArrayUtils.isEmpty(pathParts)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "At least one path after /groovioli should be set");
        }



        super.service(req, resp);
    }
}
