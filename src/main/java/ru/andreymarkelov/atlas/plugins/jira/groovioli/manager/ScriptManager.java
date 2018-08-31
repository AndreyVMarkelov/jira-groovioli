package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import java.util.Map;

import ru.andreymarkelov.atlas.plugins.jira.groovioli.util.ScriptException;

public interface ScriptManager {
    String validateSyntax(String groovyScript);
    Object executeScript(String groovyScript, Map<String, Object> parameters) throws ScriptException;
}
