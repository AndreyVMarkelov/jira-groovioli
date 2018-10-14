package ru.andreymarkelov.atlas.plugins.jira.groovioli.action.field;

import com.atlassian.jira.config.managedconfiguration.ManagedConfigurationItemService;
import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.web.action.admin.customfields.AbstractEditConfigurationItemAction;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.FieldDataManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ScriptReadOnlyViewFieldAction extends AbstractEditConfigurationItemAction {
    private final FieldDataManager fieldDataManager;

    private String viewTemplate;
    private String columnTemplate;

    public ScriptReadOnlyViewFieldAction(
            ManagedConfigurationItemService managedConfigurationItemService,
            FieldDataManager fieldDataManager) {
        super(managedConfigurationItemService);
        this.fieldDataManager = fieldDataManager;
    }

    @Override
    public String doDefault() {
        if (!getGlobalPermissionManager().hasPermission(ADMINISTER, getLoggedInUser())) {
            return PERMISSION_VIOLATION_RESULT;
        }

        String viewTemplate = fieldDataManager.getReadOnlyScriptView(getFieldConfig());
        if (isNotBlank(viewTemplate)) {
            this.viewTemplate = viewTemplate;
        }
        String columnTemplate = fieldDataManager.getReadOnlyScriptColumn(getFieldConfig());
        if (isNotBlank(columnTemplate)) {
            this.columnTemplate = columnTemplate;
        }

        return INPUT;
    }

    @Override
    @RequiresXsrfCheck
    protected String doExecute() {
        if (!getGlobalPermissionManager().hasPermission(ADMINISTER, getLoggedInUser())) {
            return PERMISSION_VIOLATION_RESULT;
        }

        fieldDataManager.setReadOnlyScriptView(getFieldConfig(), viewTemplate);
        fieldDataManager.setReadOnlyScriptColumn(getFieldConfig(), columnTemplate);

        return getRedirect("/secure/admin/ConfigureCustomField!default.jspa?customFieldId=" + getFieldConfig().getCustomField().getIdAsLong().toString());
    }

    @Override
    protected void doValidation() {
        super.doValidation();
    }

    public String getViewTemplate() {
        return viewTemplate;
    }

    public void setViewTemplate(String viewTemplate) {
        this.viewTemplate = viewTemplate;
    }

    public String getColumnTemplate() {
        return columnTemplate;
    }

    public void setColumnTemplate(String columnTemplate) {
        this.columnTemplate = columnTemplate;
    }
}
