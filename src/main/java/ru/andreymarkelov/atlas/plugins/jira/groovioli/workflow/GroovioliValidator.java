package ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow;

import com.atlassian.jira.issue.Issue;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;

import java.util.Map;

import static ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow.GroovioliValidatorFactory.FIELD;

public class GroovioliValidator implements Validator {
    private static final Logger log = LoggerFactory.getLogger(GroovioliValidator.class);

    private final ScriptManager scriptManager;

    public GroovioliValidator(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    public void validate(Map transientVars, Map args, PropertySet ps) throws InvalidInputException {
        String word = (String) transientVars.get(FIELD);
        Issue issue = (Issue) transientVars.get("issue");
        String description = issue.getDescription();

        if (description == null || !description.contains(word)) {
            throw new InvalidInputException("Issue must contain the word '" + word + "' in the description");
        }
    }
}
