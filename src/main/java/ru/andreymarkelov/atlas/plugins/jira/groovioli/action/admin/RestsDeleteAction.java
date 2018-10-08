package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class RestsDeleteAction extends JiraWebActionSupport {
    private final ListenerDataManager listenerDataManager;

    private Integer listenerId;

    public RestsDeleteAction(ListenerDataManager listenerDataManager) {
        this.listenerDataManager = listenerDataManager;
    }

    @Override
    @RequiresXsrfCheck
    public String doExecute() {
        if (!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }
        listenerDataManager.deleteListener(listenerId);
        return getRedirect("ListenersSetupAction.jspa");
    }

    public Integer getListenerId() {
        return listenerId;
    }

    public void setListenerId(Integer listenerId) {
        this.listenerId = listenerId;
    }

    private boolean hasAdminPermission() {
        ApplicationUser applicationUser = getLoggedInUser();
        if (applicationUser != null) {
            return getGlobalPermissionManager().hasPermission(ADMINISTER, applicationUser);
        }
        return false;
    }
}
