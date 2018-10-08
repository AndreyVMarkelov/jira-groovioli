package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.apache.commons.lang3.StringUtils;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.EventType;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;

import java.util.List;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class RestsAddAction extends JiraWebActionSupport {
    private final ListenerDataManager listenerDataManager;
    private final ScriptManager scriptManager;
    private final ProjectManager projectManager;

    private String note;
    private Long projectId;
    private String eventType;
    private String script;

    public RestsAddAction(
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
        return (projectId != null) && projectId.equals(checkProjectId);
    }

    public List<String> getAllEvents() {
        return EventType.getAllEvents();
    }

    public boolean isSelectedEvent(String checkEvent) {
        return (eventType != null) && eventType.equals(checkEvent);
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
