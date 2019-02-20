package ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;
import webwork.action.ActionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroovioliValidatorFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory {
    public static final String FIELD = "script";
    public static final String MESSAGE = "message";
    public static final String SCRIPT_ID = "scriptId";
    private static final String LAST_AUTHOR = "author";
    private static final String LAST_UPDATE = "update";

    private final WorkflowManager workflowManager;
    private final ScriptManager scriptManager;

    public GroovioliValidatorFactory(WorkflowManager workflowManager, ScriptManager scriptManager) {
        this.workflowManager = workflowManager;
        this.scriptManager = scriptManager;
    }

    @Override
    protected void getVelocityParamsForInput(Map velocityParams) {
        velocityParams.put(FIELD, "");
        velocityParams.put(MESSAGE, "");
    }

    @Override
    protected void getVelocityParamsForEdit(Map velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);
        getVelocityParamsForView(velocityParams, descriptor);
    }

    @Override
    protected void getVelocityParamsForView(Map velocityParams, AbstractDescriptor descriptor) {
        if (!(descriptor instanceof ValidatorDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a ValidatorDescriptor.");
        }

        ValidatorDescriptor validatorDescriptor = (ValidatorDescriptor) descriptor;
        String script = (String) validatorDescriptor.getArgs().get(FIELD);
        String message = (String) validatorDescriptor.getArgs().get(MESSAGE);
        scriptManager.validateSyntax(script);
        Map<String, String[]> myParams = ActionContext.getParameters();
        JiraWorkflow jiraWorkflow = workflowManager.getWorkflow(myParams.get("workflowName")[0]);

        velocityParams.put(FIELD, script);
        velocityParams.put(MESSAGE, message);
        velocityParams.put(LAST_AUTHOR, jiraWorkflow.getUpdateAuthorName());
        velocityParams.put(LAST_UPDATE, jiraWorkflow.getUpdatedDate());
    }

    @Override
    public Map getDescriptorParams(Map validatorParams) {
        Map params = new HashMap();
        params.put(FIELD, extractSingleParam(validatorParams, FIELD));
        params.put(MESSAGE, extractSingleParam(validatorParams, MESSAGE));
        params.put(SCRIPT_ID, UUID.randomUUID().toString());
        return params;
    }
}
