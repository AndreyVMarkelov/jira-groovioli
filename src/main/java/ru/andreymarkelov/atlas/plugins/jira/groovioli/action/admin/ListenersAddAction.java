package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import java.util.Arrays;
import java.util.List;

import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.issue.IssueViewEvent;
import com.atlassian.jira.event.user.UserEvent;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class ListenersAddAction extends JiraWebActionSupport {
    private static final List<String> allEvents = Arrays.asList(
            IssueEvent.class.getSimpleName(),
            IssueViewEvent.class.getSimpleName(),
            UserEvent.class.getSimpleName()
    );

    private final ListenerDataManager listenerDataManager;
    private final ProjectManager projectManager;

    private String note;
    private String[] projects;
    private String[] events;
    private String script;

    public ListenersAddAction(
            ListenerDataManager listenerDataManager,
            ProjectManager projectManager) {
        this.listenerDataManager = listenerDataManager;
        this.projectManager = projectManager;
    }

    @Override
    public String doDefault() {
        if (!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }
        return INPUT;
    }

    @Override
    @RequiresXsrfCheck
    public String doExecute() throws Exception {
        if (!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }
        return getRedirect("ListenersSetupAction.jspa");
    }

    public List<Project> getAllProjects() {
        return projectManager.getProjects();
    }

    public boolean isSelectedProject(Long projectId) {
        return false;
    }

    public List<String> getAllEvents() {
        return allEvents;
    }

    public boolean isSelectedEvent(String event) {
        return false;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String[] getProjects() {
        return projects;
    }

    public void setProjects(String[] projects) {
        this.projects = projects;
    }

    public String[] getEvents() {
        return events;
    }

    public void setEvents(String[] events) {
        this.events = events;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    private boolean hasAdminPermission() {
        ApplicationUser applicationUser = getLoggedInUser();
        if (applicationUser != null) {
            return getGlobalPermissionManager().hasPermission(ADMINISTER, applicationUser);
        }
        return false;
    }
}
