package ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow;

import com.atlassian.jira.issue.IssueImpl;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;

import java.util.HashMap;
import java.util.Map;

import static ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow.GroovioliFunctionFactory.FIELD;

public class GroovioliFunction extends AbstractJiraFunctionProvider {
    private static final Logger log = LoggerFactory.getLogger(GroovioliFunction.class);

    private final ScriptManager scriptManager;

    public GroovioliFunction(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        MutableIssue issue = getIssue(transientVars);
        String script = (String) args.get(FIELD);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("issue", issue);
        parameters.put("issueImpl", IssueImpl.class.cast(issue));
        parameters.put("transientVars", transientVars);
        parameters.put("args", args);
        parameters.put("ps", ps);

        try {
            scriptManager.executeScript(script, parameters);
        } catch (Exception ex) {
            log.error("Error", ex);
            throw ex;
        }
    }
}
