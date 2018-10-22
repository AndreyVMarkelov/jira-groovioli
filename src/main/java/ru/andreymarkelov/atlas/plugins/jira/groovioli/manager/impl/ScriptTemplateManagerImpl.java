package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.impl;

import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueFactory;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.label.LabelManager;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.vote.VoteManager;
import com.atlassian.jira.issue.watchers.WatcherManager;
import com.atlassian.jira.issue.worklog.WorklogManager;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.VersionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.util.JiraUtils;
import com.atlassian.jira.workflow.WorkflowTransitionUtilImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptTemplateManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.util.TemplateException;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ScriptTemplateManagerImpl implements ScriptTemplateManager {
    private final SimpleTemplateEngine templateEngine = new SimpleTemplateEngine(this.getClass().getClassLoader());

    private final LoadingCache<String, Template> templateCache = CacheBuilder.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(7, TimeUnit.DAYS)
            .build(new CacheLoader<String, Template>() {
                @Override
                public Template load(String templateSource) throws Exception {
                    return templateEngine.createTemplate(templateSource);
                }
            });

    private final Map<String, Object> baseVariables;

    public ScriptTemplateManagerImpl(
            GroupManager groupManager,
            ProjectRoleManager projectRoleManager,
            WatcherManager watcherManager,
            UserManager userManager,
            CustomFieldManager customFieldManager,
            AttachmentManager attachmentManager,
            JiraAuthenticationContext jiraAuthenticationContext,
            IssueLinkManager issueLinkManager,
            CommentManager commentManager,
            IssueFactory issueFactory,
            IssueManager issueManager,
            SubTaskManager subTaskManager,
            ConstantsManager constantsManager,
            ProjectManager projectManager,
            LabelManager labelManager,
            ChangeHistoryManager changeHistoryManager,
            VoteManager voteManager,
            WorklogManager worklogManager,
            VersionManager versionManager,
            OptionsManager optionsManager,
            ApplicationProperties applicationProperties) {
        baseVariables = new HashMap<>();
        baseVariables.put("groupManager", groupManager);
        baseVariables.put("watcherManager", watcherManager);
        baseVariables.put("userManager", userManager);
        baseVariables.put("customFieldManager", customFieldManager);
        baseVariables.put("workflowTransitionUtil", JiraUtils.loadComponent(WorkflowTransitionUtilImpl.class));
        baseVariables.put("attachmentManager", attachmentManager);
        baseVariables.put("jiraAuthenticationContext", jiraAuthenticationContext);
        baseVariables.put("projectRoleManager", projectRoleManager);
        baseVariables.put("issueLinkManager", issueLinkManager);
        baseVariables.put("commentManager", commentManager);
        baseVariables.put("issueFactory", issueFactory);
        baseVariables.put("issueManager", issueManager);
        baseVariables.put("subTaskManager", subTaskManager);
        baseVariables.put("constantsManager", constantsManager);
        baseVariables.put("projectManager", projectManager);

        baseVariables.put("labelManager", labelManager);
        baseVariables.put("changeHistoryManager", changeHistoryManager);
        baseVariables.put("voteManager", voteManager);
        baseVariables.put("worklogManager", worklogManager);
        baseVariables.put("versionManager", versionManager);
        baseVariables.put("optionsManager", optionsManager);
        baseVariables.put("applicationProperties", applicationProperties);
    }

    @Override
    public String renderTemplate(String groovyTemplate, Map<String, Object> parameters) throws TemplateException {
        try (StringWriter stringWriter = new StringWriter()) {
            Template template = templateCache.get(groovyTemplate);
            template.make(fromMap(parameters)).writeTo(stringWriter);
            return stringWriter.toString();
        } catch (Exception ex) {
            throw new TemplateException("Error executing groovy template", ex);
        }
    }

    @Override
    public String validateSyntax(String groovyTemplate) {
        try {
            templateEngine.createTemplate(groovyTemplate);
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }

    private Map<String, Object> fromMap(Map<String, Object> parameters) {
        Map variables = new HashMap(baseVariables);
        if (parameters != null) {
            variables.putAll(parameters);
        }
        return variables;
    }
}
