package ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.opensymphony.module.propertyset.PropertySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;

import java.util.Map;

import static ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow.GroovioliConditionFactory.FIELD;

public class GroovioliCondition extends AbstractJiraCondition {
    private static final Logger log = LoggerFactory.getLogger(GroovioliCondition.class);

    private final ScriptManager scriptManager;

    public GroovioliCondition(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) {
        String word = (String) transientVars.get(FIELD);
        Issue issue = getIssue(transientVars);
        String description = issue.getDescription();
        return description != null && description.contains(word);
    }
}
