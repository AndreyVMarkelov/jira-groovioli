package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.RestData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.RestDataManager;

import java.util.List;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class RestsSetupAction extends JiraWebActionSupport {
    private final RestDataManager restDataManager;

    private List<RestData> restDataList;

    public RestsSetupAction(
            RestDataManager restDataManager) {
        this.restDataManager = restDataManager;
    }

    @Override
    public String doExecute() {
        if (!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }
        restDataList = restDataManager.getAll();
        return SUCCESS;
    }

    private boolean hasAdminPermission() {
        ApplicationUser applicationUser = getLoggedInUser();
        if (applicationUser != null) {
            return getGlobalPermissionManager().hasPermission(ADMINISTER, applicationUser);
        }
        return false;
    }

    public List<RestData> getRestDataList() {
        return restDataList;
    }
}
