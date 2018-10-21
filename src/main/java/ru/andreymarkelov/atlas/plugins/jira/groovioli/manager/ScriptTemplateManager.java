package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import ru.andreymarkelov.atlas.plugins.jira.groovioli.util.TemplateException;

import java.util.Map;

public interface ScriptTemplateManager {
    String validateSyntax(String groovyTemplate);
    String renderTemplate(String groovyTemplate, Map<String, Object> parameters) throws TemplateException;
}
