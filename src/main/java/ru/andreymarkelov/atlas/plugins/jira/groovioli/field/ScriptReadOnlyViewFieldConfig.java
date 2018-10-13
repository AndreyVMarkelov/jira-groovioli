package ru.andreymarkelov.atlas.plugins.jira.groovioli.field;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.FieldDataManager;

public class ScriptReadOnlyViewFieldConfig implements FieldConfigItemType {
    private final FieldDataManager fieldDataManager;

    public ScriptReadOnlyViewFieldConfig(FieldDataManager fieldDataManager) {
        this.fieldDataManager = fieldDataManager;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getDisplayNameKey() {
        return null;
    }

    @Override
    public String getViewHtml(FieldConfig fieldConfig, FieldLayoutItem fieldLayoutItem) {
        return null;
    }

    @Override
    public String getObjectKey() {
        return null;
    }

    @Override
    public Object getConfigurationObject(Issue issue, FieldConfig fieldConfig) {
        return null;
    }

    @Override
    public String getBaseEditUrl() {
        return null;
    }
}
