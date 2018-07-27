package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class ListenersSetupAction extends JiraWebActionSupport {
    private final ListenerDataManager listenerDataManager;

    public ListenersSetupAction(ListenerDataManager listenerDataManager) {
        this.listenerDataManager = listenerDataManager;
    }

    @Override
    public String doExecute() {
        if (!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }

        return SUCCESS;
    }

    private boolean hasAdminPermission() {
        ApplicationUser applicationUser = getLoggedInUser();
        if (applicationUser != null) {
            return getGlobalPermissionManager().hasPermission(ADMINISTER, applicationUser);
        }
        return false;
    }
}
