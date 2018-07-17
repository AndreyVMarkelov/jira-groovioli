def subTasks = issue.getSubTaskObjects()

String currentUser = ((com.opensymphony.workflow.WorkflowContext) transientVars.get("context")).getCaller()
return subTasks.every { it.assigneeId == currentUser }
