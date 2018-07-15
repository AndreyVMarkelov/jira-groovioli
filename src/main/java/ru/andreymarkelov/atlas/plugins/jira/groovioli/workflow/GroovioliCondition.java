package ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueImpl;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;

public class GroovioliCondition extends AbstractJiraCondition {
    private static final Logger log = LoggerFactory.getLogger(GroovioliCondition.class);

    private final ScriptManager scriptManager;

    public GroovioliCondition(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        Issue issue = getIssue(transientVars);
        String script = (String) args.get(GroovioliFunctionFactory.FIELD);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("issue", issue);
        parameters.put("issueImpl", IssueImpl.class.cast(issue));
        parameters.put("transientVars", transientVars);
        parameters.put("args", args);
        parameters.put("ps", ps);

        try {
            return (Boolean) scriptManager.executeScript(script, parameters);
        } catch (Exception ex) {
            log.error("Error", ex);
            throw ex;
        }
    }
}
