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
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.script.ConstantDsl;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.script.FieldOptionDsl;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.script.GroovioliBase;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.script.IssueDsl;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.script.ProjectDsl;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.util.ScriptException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ScriptManagerImpl implements ScriptManager {
    private final GroovyShell shell;
    private final Map<String, Object> baseVariables;
    private final LoadingCache<String, Script> scriptCache;

    public ScriptManagerImpl(
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
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(GroovioliBase.class.getName());
        shell = new GroovyShell(this.getClass().getClassLoader(), config);

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

        // DSL
        baseVariables.put("fieldOptionDsl", new FieldOptionDsl());
        baseVariables.put("projectDsl", new ProjectDsl());
        baseVariables.put("constantDsl", new ConstantDsl());
        baseVariables.put("issueDsl", new IssueDsl());

        scriptCache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(7, TimeUnit.DAYS)
                .build(new CacheLoader<String, Script>() {
                    @Override
                    public Script load(String scriptSource) throws CompilationFailedException {
                        return shell.parse(scriptSource);
                    }
                });
    }

    @Override
    public Object executeScript(String groovyScript, Map<String, Object> parameters) throws ScriptException {
        try {
            Script script = scriptCache.get(groovyScript);
            script.setBinding(fromMap(parameters));
            return script.run();
        } catch (ExecutionException ex) {
            throw new ScriptException("Error executing groovy script", ex);
        }
    }

    @Override
    public String validateSyntax(String groovyScript) {
        try {
            shell.parse(groovyScript);
        } catch (CompilationFailedException e) {
            return e.getMessage();
        }
        return null;
    }

    private Binding fromMap(Map<String, Object> parameters) {
        Map variables = new HashMap(baseVariables);
        if (parameters != null) {
            variables.putAll(parameters);
        }
        return new Binding(variables);
    }
}
