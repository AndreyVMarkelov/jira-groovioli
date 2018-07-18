import com.atlassian.jira.user.ApplicationUser
import com.opensymphony.workflow.WorkflowContext

def customFieldObj = customFieldManager.getCustomFieldObjectByName("Master")
if (!customFieldObj) {
    log.warn("No custom field with name Master on issue: ${issue.key}")
    return false
}

def customFieldObjValue = issue.getCustomFieldValue(customFieldObj) as ApplicationUser
if (!customFieldObjValue) {
    return false
}

String currentUser = ((WorkflowContext) transientVars.get("context")).getCaller()
return customFieldObjValue.name == currentUser
