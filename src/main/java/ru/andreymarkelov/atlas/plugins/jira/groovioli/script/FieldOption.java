package ru.andreymarkelov.atlas.plugins.jira.groovioli.script;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.customfields.option.Option;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigScheme;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObjectSupport;
import org.apache.commons.collections.CollectionUtils;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.util.ScriptDslException;

import java.util.List;

public class FieldOption extends GroovyObjectSupport {
    public void addOption(@DelegatesTo(FieldOptionHandler.class) Closure closure) {
        FieldOptionHandler fieldOptionHandler = new FieldOptionHandler();
        closure.setDelegate(fieldOptionHandler);
        closure.call();

        OptionsManager optionsManager = (OptionsManager) closure.getProperty("optionsManager");
        CustomFieldManager customFieldManager = (CustomFieldManager) closure.getProperty("customFieldManager");

        if (!fieldOptionHandler.isValid()) {
            throw new ScriptDslException("Invalid parameters for addOption");
        }

        CustomField customField = customFieldManager.getCustomFieldObject(fieldOptionHandler.getCustomField());
        if (customField == null) {
            throw new ScriptDslException("Custom field with id not found");
        }

        FieldConfig fieldConfig = null;
        List<FieldConfigScheme> fieldConfigSchemeList = customField.getConfigurationSchemes();
        if (fieldOptionHandler.getFieldConfigSchemeId() != null) {
            for (FieldConfigScheme fieldConfigScheme : fieldConfigSchemeList) {
                if (fieldConfigScheme.getId().equals(fieldOptionHandler.getFieldConfigSchemeId())) {
                    fieldConfig = fieldConfigScheme.getOneAndOnlyConfig();
                }
            }
        } else {
            for (FieldConfigScheme fieldConfigScheme : fieldConfigSchemeList) {
                fieldConfig = fieldConfigScheme.getOneAndOnlyConfig();
                break;
            }
        }

        if (fieldConfig == null) {
            throw new ScriptDslException("Field config not found");
        }

        List<Option> optionList = optionsManager.getAllOptions();
        long lastSequence = CollectionUtils.isEmpty(optionList) ? 1 : optionList.get(optionList.size() - 1).getSequence() + 1;
        for (String option : fieldOptionHandler.getOptions()) {
            optionsManager.createOption(fieldConfig, null, lastSequence, option);
            lastSequence++;
        }
    }
}
