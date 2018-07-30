package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import java.util.List;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class ListenersSetupAction extends JiraWebActionSupport {
    private final ListenerDataManager listenerDataManager;
    private final ProjectManager projectManager;

    private List<ListenerData> listenerDataList;

    public ListenersSetupAction(
            ListenerDataManager listenerDataManager,
            ProjectManager projectManager) {
        this.listenerDataManager = listenerDataManager;
        this.projectManager = projectManager;
    }

    @Override
    public String doExecute() {
        if (!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }
        listenerDataList = listenerDataManager.getAll();
        return SUCCESS;
    }

    private boolean hasAdminPermission() {
        ApplicationUser applicationUser = getLoggedInUser();
        if (applicationUser != null) {
            return getGlobalPermissionManager().hasPermission(ADMINISTER, applicationUser);
        }
        return false;
    }

    public String resolveProjectName(Long projectId) {
        Project project = projectManager.getProjectObj(projectId);
        return (project != null) ? project.getName() : "Unknown";
    }

    public List<ListenerData> getListenerDataList() {
        return listenerDataList;
    }
}
