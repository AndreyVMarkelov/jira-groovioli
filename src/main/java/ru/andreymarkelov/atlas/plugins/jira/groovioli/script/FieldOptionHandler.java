package ru.andreymarkelov.atlas.plugins.jira.groovioli.script;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;

@SuppressWarnings("WeakerAccess")
public class FieldOptionHandler {
    private String customField = null;
    private Long fieldConfigSchemeId = null;
    private Set<String> options = new LinkedHashSet<>();

    public String getCustomField() {
        return customField;
    }

    public Long getFieldConfigSchemeId() {
        return fieldConfigSchemeId;
    }

    public Set<String> getOptions() {
        return Collections.unmodifiableSet(options);
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(customField) && !options.isEmpty();
    }

    public void customField(String customField) {
        this.customField = customField;
    }

    public void fieldConfigSchemeId(Long fieldConfigSchemeId) {
        this.fieldConfigSchemeId = fieldConfigSchemeId;
    }

    public void option(String option) {
        this.options.add(option);
    }

    public void options(String... options) {
        this.options.addAll(asList(options));
    }
}
