package ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueImpl;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;

import static ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow.GroovioliValidatorFactory.FIELD;

public class GroovioliValidator implements Validator {
    private static final Logger log = LoggerFactory.getLogger(GroovioliValidator.class);

    private final ScriptManager scriptManager;

    public GroovioliValidator(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public void validate(Map transientVars, Map args, PropertySet ps) throws InvalidInputException {
        Issue issue = (Issue) transientVars.get("issue");
        String script = (String) args.get(FIELD);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("issue", issue);
        parameters.put("issueImpl", IssueImpl.class.cast(issue));
        parameters.put("transientVars", transientVars);
        parameters.put("args", args);
        parameters.put("ps", ps);
        parameters.put("log", log);

        try {
            scriptManager.executeScript(script, parameters);
        } catch (Exception ex) {
            log.error("Error", ex);
            throw new InvalidInputException(ex);
        }
    }
}
