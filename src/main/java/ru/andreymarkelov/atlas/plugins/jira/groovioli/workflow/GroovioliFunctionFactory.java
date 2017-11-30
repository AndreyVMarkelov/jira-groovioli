package ru.andreymarkelov.atlas.plugins.jira.groovioli.workflow;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;
import webwork.action.ActionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroovioliFunctionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory {
    public static final String FIELD = "script";
    public static final String SCRIPT_ID = "scriptId";
    private static final String LAST_AUTHOR = "author";
    private static final String LAST_UPDATE = "update";

    private final WorkflowManager workflowManager;
    private final ScriptManager scriptManager;

    public GroovioliFunctionFactory(WorkflowManager workflowManager, ScriptManager scriptManager) {
        this.workflowManager = workflowManager;
        this.scriptManager = scriptManager;
    }

    @Override
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        velocityParams.put(FIELD, "");
    }

    @Override
    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);
        getVelocityParamsForView(velocityParams, descriptor);
    }

    @Override
    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        if (!(descriptor instanceof FunctionDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a FunctionDescriptor.");
        }

        FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;
        String script = (String) functionDescriptor.getArgs().get(FIELD);

        scriptManager.validateSyntax(script);

        Map<String, String[]> myParams = ActionContext.getParameters();
        JiraWorkflow jiraWorkflow = workflowManager.getWorkflow(myParams.get("workflowName")[0]);

        velocityParams.put(FIELD, script);
        velocityParams.put(LAST_AUTHOR, jiraWorkflow.getUpdateAuthorName());
        velocityParams.put(LAST_UPDATE, jiraWorkflow.getUpdatedDate());
    }

    @Override
    public Map<String,?> getDescriptorParams(Map<String, Object> formParams) {
        Map params = new HashMap();
        String message = extractSingleParam(formParams, FIELD);
        params.put(FIELD, message);
        params.put(SCRIPT_ID, UUID.randomUUID().toString());
        return params;
    }
}
