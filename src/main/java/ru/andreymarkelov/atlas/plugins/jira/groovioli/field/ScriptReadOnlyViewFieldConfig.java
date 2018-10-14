package ru.andreymarkelov.atlas.plugins.jira.groovioli.field;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.FieldDataManager;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class ScriptReadOnlyViewFieldConfig implements FieldConfigItemType {
    private static final Logger log = LoggerFactory.getLogger(ScriptReadOnlyViewFieldConfig.class);

    private final FieldDataManager fieldDataManager;
    private final TemplateRenderer templateRenderer;

    public ScriptReadOnlyViewFieldConfig(
            FieldDataManager fieldDataManager,
            TemplateRenderer templateRenderer) {
        this.fieldDataManager = fieldDataManager;
        this.templateRenderer = templateRenderer;
    }

    @Override
    public String getDisplayName() {
        return "Templates";
    }

    @Override
    public String getDisplayNameKey() {
        return "groovioli-field.readonly.config.templates";
    }

    @Override
    public String getViewHtml(FieldConfig fieldConfig, FieldLayoutItem fieldLayoutItem) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("viewTemplate", fieldDataManager.getReadOnlyScriptView(fieldConfig));
        parameters.put("columnTemplate", fieldDataManager.getReadOnlyScriptColumn(fieldConfig));
        StringWriter stringWriter = new StringWriter();
        try {
            templateRenderer.render(
                    "/ru/andreymarkelov/atlas/plugins/jira/groovioli/templates/field/readonly-field-config-view.vm",
                    parameters,
                    stringWriter);
        } catch (Exception e) {
            log.error("Render exception", e);
            stringWriter.append("Render exception");
        }
        return stringWriter.toString();
    }

    @Override
    public String getObjectKey() {
        return "ScriptReadOnlyViewFieldConfig";
    }

    @Override
    public Object getConfigurationObject(Issue issue, FieldConfig fieldConfig) {
        Map<String, String> configObject = new HashMap<>();
        configObject.put("viewTemplate", fieldDataManager.getReadOnlyScriptView(fieldConfig));
        configObject.put("columnTemplate", fieldDataManager.getReadOnlyScriptColumn(fieldConfig));
        return configObject;
    }

    @Override
    public String getBaseEditUrl() {
        return "ScriptReadOnlyViewFieldAction!default.jspa";
    }
}
