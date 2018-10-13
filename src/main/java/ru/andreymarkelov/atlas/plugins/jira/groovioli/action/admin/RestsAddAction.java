package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.admin;

import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.RestData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.RestDataManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;
import static org.apache.commons.lang.StringUtils.isAlphanumeric;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RestsAddAction extends JiraWebActionSupport {
    private final RestDataManager restDataManager;
    private final ScriptManager scriptManager;
    private final UserUtil userUtil;

    private String note;
    private String path;
    private String performer;
    private String script;

    public RestsAddAction(
            RestDataManager restDataManager,
            ScriptManager scriptManager,
            UserUtil userUtil) {
        this.restDataManager = restDataManager;
        this.scriptManager = scriptManager;
        this.userUtil = userUtil;
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

        if (isNotBlank(path)) {
            if (!isAlphanumeric(path)) {
                addError("path", getText("groovioli-admin.action.addRest.path.error.invalidpath"));
            }
        } else {
            addError("path", getText("groovioli-admin.action.addRest.path.error.nopath"));
        }

        if (isNotBlank(performer)) {
            if (!userUtil.userExists(performer)) {
                addError("performer", getText("groovioli-admin.action.addRest.performer.error.nouser"));
            }
        }

        String scriptError = scriptManager.validateSyntax(script);
        if (isNotBlank(scriptError)) {
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
