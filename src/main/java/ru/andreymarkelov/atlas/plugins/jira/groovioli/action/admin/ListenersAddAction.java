package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import java.util.List;

import com.atlassian.jira.event.DashboardViewEvent;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.issue.IssueViewEvent;
import com.atlassian.jira.event.user.LoginEvent;
import com.atlassian.jira.event.user.LogoutEvent;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.apache.commons.lang3.StringUtils;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;

import static java.util.Arrays.asList;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class ListenersAddAction extends JiraWebActionSupport {
    private static final List<String> allEvents = asList(
            IssueEvent.class.getSimpleName(),
            IssueViewEvent.class.getSimpleName(),
            DashboardViewEvent.class.getSimpleName(),
            LoginEvent.class.getSimpleName(),
            LogoutEvent.class.getSimpleName()
    );

    private final ListenerDataManager listenerDataManager;
    private final ScriptManager scriptManager;
    private final ProjectManager projectManager;

    private String note;
    private Long projectId;
    private String eventType;
    private String script;

    public ListenersAddAction(
            ListenerDataManager listenerDataManager,
            ScriptManager scriptManager,
            ProjectManager projectManager) {
        this.listenerDataManager = listenerDataManager;
        this.scriptManager = scriptManager;
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
    public String doExecute() {
        if (!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }
        listenerDataManager.createListener(new ListenerData(note, projectId, eventType, script));
        return getRedirect("ListenersSetupAction.jspa");
    }

    @Override
    protected void doValidation() {
        super.doValidation();

        String scriptError = scriptManager.validateSyntax(script);
        if (StringUtils.isNotBlank(scriptError)) {
            addError("script", scriptError);
        }
    }

    public List<Project> getAllProjects() {
        return projectManager.getProjects();
    }

    public boolean isSelectedProject(Long checkProjectId) {
        return (projectId != null) ? projectId.equals(checkProjectId) : false;
    }

    public List<String> getAllEvents() {
        return allEvents;
    }

    public boolean isSelectedEvent(String checkEvent) {
        return (eventType != null) ? eventType.equals(checkEvent) : false;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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
