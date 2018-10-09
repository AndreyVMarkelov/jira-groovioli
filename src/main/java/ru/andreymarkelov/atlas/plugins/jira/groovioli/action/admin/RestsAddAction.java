package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.apache.commons.lang3.StringUtils;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.RestData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.RestDataManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class RestsAddAction extends JiraWebActionSupport {
    private final RestDataManager restDataManager;
    private final ScriptManager scriptManager;

    private String note;
    private String path;
    private String performer;
    private String script;

    public RestsAddAction(
            RestDataManager restDataManager,
            ScriptManager scriptManager) {
        this.restDataManager = restDataManager;
        this.scriptManager = scriptManager;
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
        restDataManager.createRestData(new RestData(note, path, performer, script));
        return getRedirect("RestsSetupAction.jspa");
    }

    @Override
    protected void doValidation() {
        super.doValidation();

        String scriptError = scriptManager.validateSyntax(script);
        if (StringUtils.isNotBlank(scriptError)) {
            addError("script", scriptError);
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
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
