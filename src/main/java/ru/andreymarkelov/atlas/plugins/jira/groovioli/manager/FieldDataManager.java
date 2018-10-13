package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import com.atlassian.jira.issue.fields.config.FieldConfig;

public interface FieldDataManager {
    String getReadOnlyScript(FieldConfig config);
    void setReadOnlyScript(FieldConfig config, String readOnlyScript);
}
