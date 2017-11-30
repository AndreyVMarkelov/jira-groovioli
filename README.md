# Groovioli

This is the plugin for Atlassian JIRA which add way to perform custom actions in Groovy.

## Components

1. Groovioli Condition

    Check by Groovy script whether or not a given workflow transition can be executed by a given user.
    ![alt text](https://github.com/AndreyVMarkelov/jira-groovioli/raw/master/pics/gr-con.png)

2. Groovioli Validator

    Check by Groovy script  that the data given to a workflow transition is valid.
    ![alt text](https://github.com/AndreyVMarkelov/jira-groovioli/raw/master/pics/gr-val.png)

3. Groovioli Function

    Perform by Groovy script actions after a workflow transition has been executed.
    ![alt text](https://github.com/AndreyVMarkelov/jira-groovioli/raw/master/pics/gr-post.png)

## Groovy binding variables

Global:
- groupManager: com.atlassian.jira.security.groups.GroupManager
- watcherManager: com.atlassian.jira.issue.watchers.WatcherManager
- userManager: com.atlassian.jira.user.util.UserManager
- customFieldManager: com.atlassian.jira.issue.CustomFieldManager
- workflowTransitionUtil: com.atlassian.jira.workflow.WorkflowTransitionUtil
- attachmentManager: com.atlassian.jira.issue.AttachmentManager
- jiraAuthenticationContext: com.atlassian.jira.security.JiraAuthenticationContext
- projectRoleManager: com.atlassian.jira.security.roles.ProjectRoleManager

Post-Function:
- issue: com.atlassian.jira.issue.MutableIssue
- issueImpl: com.atlassian.jira.issue.IssueImpl
- transientVars: java.util.Map
- args: java.util.Map
- ps: com.opensymphony.module.propertyset.PropertySet

## Examples

- [Conditions](https://github.com/AndreyVMarkelov/jira-groovioli/tree/master/scripts/conditions)
- [Validators](https://github.com/AndreyVMarkelov/jira-groovioli/tree/master/scripts/validators)
- [Functions](https://github.com/AndreyVMarkelov/jira-groovioli/tree/master/scripts/functions)

## Build versions

To install manually you can find build versions [here](https://github.com/AndreyVMarkelov/jira-groovioli/tree/master/build).

## Contribution

Any issues and suggestions put [here](https://github.com/AndreyVMarkelov/jira-groovioli/issues). Please, contribute!

## Changelog:
- 0.0.1: Init version
