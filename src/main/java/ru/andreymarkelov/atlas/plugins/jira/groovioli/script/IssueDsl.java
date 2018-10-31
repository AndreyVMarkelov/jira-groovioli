package ru.andreymarkelov.atlas.plugins.jira.groovioli.script;

import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueFactory;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObjectSupport;
import org.apache.commons.lang3.StringUtils;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.util.ScriptDslException;

public class IssueDsl extends GroovyObjectSupport {
    public Issue newIssue(@DelegatesTo(NewIssueHandler.class) Closure closure) {
        NewIssueHandler newIssueHandler = new NewIssueHandler();
        closure.setDelegate(newIssueHandler);
        closure.call();

        if (!newIssueHandler.isValid()) {
            throw new ScriptDslException("Invalid parameters for newIssue");
        }

        IssueFactory issueFactory = (IssueFactory) closure.getProperty("issueFactory");
        IssueManager issueManager = (IssueManager) closure.getProperty("issueManager");
        JiraAuthenticationContext jiraAuthenticationContext = (JiraAuthenticationContext) closure.getProperty("jiraAuthenticationContext");

        MutableIssue newIssue = issueFactory.getIssue();
        newIssue.setSummary(newIssueHandler.summary);
        newIssue.setDescription(newIssueHandler.description);
        newIssue.setProjectObject(newIssueHandler.project);
        newIssue.setIssueTypeObject(newIssueHandler.issueType);
        newIssue.setAssignee(newIssueHandler.assignee);
        if (newIssueHandler.reporter != null) {
            newIssue.setReporter(newIssueHandler.reporter);
        } else {
            newIssue.setReporter(jiraAuthenticationContext.getLoggedInUser());
        }

        try {
            return issueManager.createIssueObject(jiraAuthenticationContext.getLoggedInUser(), newIssue);
        } catch (CreateException e) {
            throw new ScriptDslException("Error create issue", e);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public class NewIssueHandler {
        private String summary;
        private String description;
        private Project project;
        private IssueType issueType;
        private ApplicationUser assignee;
        private ApplicationUser reporter;

        public boolean isValid() {
            return StringUtils.isNotBlank(summary) && project != null && issueType != null;
        }

        public void summary(String summary) {
            this.summary = summary;
        }

        public void description(String description) {
            this.description = description;
        }

        public void project(Project project) {
            this.project = project;
        }

        public void issueType(IssueType issueType) {
            this.issueType = issueType;
        }

        public void assignee(ApplicationUser assignee) {
            this.assignee = assignee;
        }

        public void reporter(ApplicationUser reporter) {
            this.reporter = reporter;
        }
    }
}
