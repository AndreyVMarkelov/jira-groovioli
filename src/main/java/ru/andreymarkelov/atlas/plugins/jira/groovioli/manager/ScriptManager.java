package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import com.opensymphony.workflow.WorkflowException;

import java.util.Map;

public interface ScriptManager {
    String validateSyntax(String groovyScript);
    Object executeScript(String groovyScript, Map<String, Object> parameters) throws WorkflowException;
}
