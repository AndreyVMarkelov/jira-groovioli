package ru.andreymarkelov.atlas.plugins.jira.groovioli.field;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.FieldDataManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptTemplateManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ScriptReadOnlyViewField extends CalculatedCFType<String, String> {
    private static final Logger log = LoggerFactory.getLogger(ScriptReadOnlyViewField.class);

    private final FieldDataManager fieldDataManager;
    private final TemplateRenderer templateRenderer;
    private final ScriptTemplateManager scriptTemplateManager;

    public ScriptReadOnlyViewField(
            FieldDataManager fieldDataManager,
            TemplateRenderer templateRenderer,
            ScriptTemplateManager scriptTemplateManager) {
        this.fieldDataManager = fieldDataManager;
        this.templateRenderer = templateRenderer;
        this.scriptTemplateManager = scriptTemplateManager;
    }

    @Override
    public List<FieldConfigItemType> getConfigurationItemTypes() {
        List<FieldConfigItemType> configurationItemTypes = new ArrayList<>();
        configurationItemTypes.add(new ScriptReadOnlyViewFieldConfig(fieldDataManager, templateRenderer));
        return configurationItemTypes;
    }

    @Override
    public String getSingularObjectFromString(String s) throws FieldValidationException {
        return s;
    }

    @Override
    public String getStringFromSingularObject(String s) {
        return s;
    }

    @Nullable
    @Override
    public String getValueFromIssue(CustomField customField, Issue issue) {
        return "Ferfe";
    }

    @Override
    public Map<String, Object> getVelocityParameters(Issue issue, CustomField customField, FieldLayoutItem fieldLayoutItem) {
        Map<String, Object> params = super.getVelocityParameters(issue, customField, fieldLayoutItem);
        if (issue != null) {
            FieldConfig fieldConfig = customField.getRelevantConfig(issue);
            if (fieldConfig != null) {
                String view = fieldDataManager.getReadOnlyScriptView(fieldConfig);
                String column = fieldDataManager.getReadOnlyScriptColumn(fieldConfig);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("issue", issue);
                parameters.put("customField", customField);
                parameters.put("fieldLayoutItem", fieldLayoutItem);

                try {
                    if (isNotBlank(view)) {
                        params.put("resultView", scriptTemplateManager.renderTemplate(view, parameters));
                    }
                } catch (Exception ex) {
                    log.error("Error render issue view", ex);
                }

                try {
                    if (isNotBlank(column)) {
                        params.put("resultColumn", scriptTemplateManager.renderTemplate(column, parameters));
                    }
                } catch (Exception ex) {
                    log.error("Error render column issue view", ex);

                }
            }
        }
        return params;
    }
}
