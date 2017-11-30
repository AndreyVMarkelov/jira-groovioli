package ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginConditionFactory;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;
import webwork.action.ActionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroovioliConditionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginConditionFactory {
    public static final String FIELD = "script";
    public static final String SCRIPT_ID = "scriptId";
    private static final String LAST_AUTHOR = "author";
    private static final String LAST_UPDATE = "update";

    private final WorkflowManager workflowManager;
    private final ScriptManager scriptManager;

    public GroovioliConditionFactory(WorkflowManager workflowManager, ScriptManager scriptManager) {
        this.workflowManager = workflowManager;
        this.scriptManager = scriptManager;
    }

    @Override
    protected void getVelocityParamsForInput(Map velocityParams) {
        velocityParams.put(FIELD, "");
    }

    @Override
    protected void getVelocityParamsForEdit(Map velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);
        getVelocityParamsForView(velocityParams, descriptor);
    }

    @Override
    protected void getVelocityParamsForView(Map velocityParams, AbstractDescriptor descriptor) {
        if (!(descriptor instanceof ConditionDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a ConditionDescriptor.");
        }

        ConditionDescriptor conditionDescriptor = (ConditionDescriptor) descriptor;
        String script = (String) conditionDescriptor.getArgs().get(FIELD);

        scriptManager.validateSyntax(script);

        Map<String, String[]> myParams = ActionContext.getParameters();
        JiraWorkflow jiraWorkflow = workflowManager.getWorkflow(myParams.get("workflowName")[0]);

        velocityParams.put(FIELD, script);
        velocityParams.put(LAST_AUTHOR, jiraWorkflow.getUpdateAuthorName());
        velocityParams.put(LAST_UPDATE, jiraWorkflow.getUpdatedDate());
    }

    @Override
    public Map getDescriptorParams(Map conditionParams) {
        Map params = new HashMap();
        params.put(FIELD, extractSingleParam(conditionParams, FIELD));
        params.put(SCRIPT_ID, UUID.randomUUID().toString());
        return params;
    }
}
