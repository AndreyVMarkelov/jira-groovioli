package ru.andreymarkelov.atlas.plugins.jira.groovioli.rest;

import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.RestData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.RestDataManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;
import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.status;

@Path("/rest")
public class RestResource {
    private static final Logger log = LoggerFactory.getLogger(RestResource.class);

    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final GlobalPermissionManager globalPermissionManager;
    private final RestDataManager restDataManager;

    public RestResource(
            JiraAuthenticationContext jiraAuthenticationContext,
            GlobalPermissionManager globalPermissionManager,
            RestDataManager restDataManager) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.globalPermissionManager = globalPermissionManager;
        this.restDataManager = restDataManager;
    }

    @Path("/getscript")
    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public Response getScript(@QueryParam("restDataId") Integer restDataId) {
        ApplicationUser currentUser = jiraAuthenticationContext.getLoggedInUser();
        if (!globalPermissionManager.hasPermission(ADMINISTER, currentUser)) {
            log.warn("Invalid user:{} tries to access script", currentUser.getName());
            return status(FORBIDDEN)
                    .entity(jiraAuthenticationContext.getI18nHelper().getText("groovioli-rest.listener.noaccess"))
                    .build();
        }

        RestData restData = restDataManager.get(restDataId);
        if (restData == null) {
            return status(NOT_FOUND)
                    .entity(jiraAuthenticationContext.getI18nHelper().getText("groovioli-rest.listener.nolistener"))
                    .build();
        }
        return status(OK).entity(restData.getScript()).build();
    }
}
