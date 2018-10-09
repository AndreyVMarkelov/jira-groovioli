package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.RestDataManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class RestsDeleteAction extends JiraWebActionSupport {
    private final RestDataManager restDataManager;

    private Integer restDataId;

    public RestsDeleteAction(RestDataManager restDataManager) {
        this.restDataManager = restDataManager;
    }

    @Override
    @RequiresXsrfCheck
    public String doExecute() {
        if (!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }
        restDataManager.deleteRestData(restDataId);
        return getRedirect("RestsSetupAction.jspa");
    }

    public Integer getRestDataId() {
        return restDataId;
    }

    public void setRestDataId(Integer restDataId) {
        this.restDataId = restDataId;
    }

    private boolean hasAdminPermission() {
        ApplicationUser applicationUser = getLoggedInUser();
        if (applicationUser != null) {
            return getGlobalPermissionManager().hasPermission(ADMINISTER, applicationUser);
        }
        return false;
    }
}
