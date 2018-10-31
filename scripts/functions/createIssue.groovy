newIssue {
    summary "Some text"
    description "Some descr"
    project findProject { key 'AAA' }
    issueType findIssueType { name 'Task' }
    assignee jiraAuthenticationContext.getLoggedInUser()
}
