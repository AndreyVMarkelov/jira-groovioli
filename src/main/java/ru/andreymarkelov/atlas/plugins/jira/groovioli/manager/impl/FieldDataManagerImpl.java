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
    public String getReadOnlyScript(FieldConfig config) {
        return null;
    }

    @Override
    public void setReadOnlyScript(FieldConfig config, String readOnlyScript) {

    }

    private synchronized PluginSettings getPluginSettings() {
        return pluginSettings;
    }
}
