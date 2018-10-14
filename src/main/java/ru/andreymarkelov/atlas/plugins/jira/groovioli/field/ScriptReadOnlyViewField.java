package ru.andreymarkelov.atlas.plugins.jira.groovioli.field;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigItemType;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.templaterenderer.TemplateRenderer;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.FieldDataManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScriptReadOnlyViewField extends CalculatedCFType<String, String> {
    private final FieldDataManager fieldDataManager;
    private final TemplateRenderer templateRenderer;

    public ScriptReadOnlyViewField(
            FieldDataManager fieldDataManager,
            TemplateRenderer templateRenderer) {
        this.fieldDataManager = fieldDataManager;
        this.templateRenderer = templateRenderer;
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
        return null;
    }

    @Override
    public Map<String, Object> getVelocityParameters(Issue issue, CustomField customField, FieldLayoutItem fieldLayoutItem) {
        Map<String, Object> params = super.getVelocityParameters(issue, customField, fieldLayoutItem);
        if (issue != null) {
            FieldConfig fieldConfig = customField.getRelevantConfig(issue);
            if (fieldConfig != null) {
                params.put("script", fieldDataManager.getReadOnlyScriptColumn(fieldConfig));
            }
        }
        return params;
    }
}
