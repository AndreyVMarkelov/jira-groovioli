package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import com.atlassian.jira.issue.fields.config.FieldConfig;

public interface FieldDataManager {
    String getReadOnlyScriptView(FieldConfig config);
    void setReadOnlyScriptView(FieldConfig config, String readOnlyScriptView);
    String getReadOnlyScriptColumn(FieldConfig config);
    void setReadOnlyScriptColumn(FieldConfig config, String readOnlyScriptColumn);
}
