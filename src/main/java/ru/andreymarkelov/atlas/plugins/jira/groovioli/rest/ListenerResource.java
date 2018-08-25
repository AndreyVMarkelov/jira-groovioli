package ru.andreymarkelov.atlas.plugins.jira.groovioli.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.status;

@Path("/listener")
public class ListenerResource {
    private static final Logger log = LoggerFactory.getLogger(ListenerResource.class);

    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final GlobalPermissionManager globalPermissionManager;
    private final ListenerDataManager listenerDataManager;

    public ListenerResource(
            JiraAuthenticationContext jiraAuthenticationContext,
            GlobalPermissionManager globalPermissionManager,
            ListenerDataManager listenerDataManager) {
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.globalPermissionManager = globalPermissionManager;
        this.listenerDataManager = listenerDataManager;
    }

    @Path("/getscript")
    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public Response getScript(@QueryParam("listenerId") Integer listenerId) {
        ApplicationUser currentUser = jiraAuthenticationContext.getLoggedInUser();
        if (!globalPermissionManager.hasPermission(ADMINISTER, currentUser)) {
            log.warn("Invalid user:{} tries to access script", currentUser.getName());
            return status(FORBIDDEN)
                    .entity(jiraAuthenticationContext.getI18nHelper().getText("groovioli-rest.listener.noaccess"))
                    .build();
        }

        ListenerData listenerData = listenerDataManager.get(listenerId);
        if (listenerData == null) {
            return status(NOT_FOUND)
                    .entity(jiraAuthenticationContext.getI18nHelper().getText("groovioli-rest.listener.nolistener"))
                    .build();
        }
        return status(OK).entity(listenerData.getScript()).build();
    }
}
