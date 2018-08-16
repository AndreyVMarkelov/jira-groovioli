import com.atlassian.jira.issue.MutableIssue

MutableIssue newIssue = issueFactory.getIssue()
newIssue.setSummary("Created on transition")
newIssue.setDescription("Some description here..")
newIssue.setProjectObject(projectManager.getProjectObjByKey("AAA"))
newIssue.setIssueType(constantsManager.getAllIssueTypeObjects().find {
    it.getName() == "Task"
})
newIssue.setAssignee(jiraAuthenticationContext.getLoggedInUser())

log.info(newIssue.toString())
issueManager.createIssueObject(jiraAuthenticationContext.getLoggedInUser(), newIssue)
