package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.impl;

import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.FieldDataManager;

public class FieldDataManagerImpl implements FieldDataManager {
    private static final String PLUGIN_KEY = "GroovioliJira";

    private final PluginSettings pluginSettings;

    public FieldDataManagerImpl(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createSettingsForKey(PLUGIN_KEY);
    }

    @Override
    public String getReadOnlyScriptView(FieldConfig config) {
        Object obj = getPluginSettings().get(getKey(config, "readOnlyScriptView"));
        return obj != null ? obj.toString() : "";
    }

    @Override
    public void setReadOnlyScriptView(FieldConfig config, String readOnlyScriptView) {
        getPluginSettings().put(getKey(config, "readOnlyScriptView"), readOnlyScriptView);
    }

    @Override
    public String getReadOnlyScriptColumn(FieldConfig config) {
        Object obj = getPluginSettings().get(getKey(config, "readOnlyScriptColumn"));
        return obj != null ? obj.toString() : "";
    }

    @Override
    public void setReadOnlyScriptColumn(FieldConfig config, String readOnlyScriptColumn) {
        getPluginSettings().put(getKey(config, "readOnlyScriptColumn"), readOnlyScriptColumn);
    }

    private String getKey(FieldConfig config, String name) {
        return config.getFieldId().concat("_").concat(config.getId().toString()).concat("_").concat(name);
    }

    private synchronized PluginSettings getPluginSettings() {
        return pluginSettings;
    }
}
