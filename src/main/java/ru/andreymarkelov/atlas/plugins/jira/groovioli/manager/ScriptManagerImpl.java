package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.watchers.WatcherManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.util.JiraUtils;
import com.atlassian.jira.workflow.WorkflowTransitionUtil;
import com.atlassian.jira.workflow.WorkflowTransitionUtilImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.opensymphony.workflow.WorkflowException;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilationFailedException;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ScriptManagerImpl implements ScriptManager {
    private final GroovyShell shell = new GroovyShell(this.getClass().getClassLoader());

    private final LoadingCache<String, Script> scriptCache = CacheBuilder.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(7, TimeUnit.DAYS)
            .build(new CacheLoader<String, Script>() {
                @Override
                public Script load(String scriptSource) throws CompilationFailedException {
                    return shell.parse(scriptSource);
                }
            });

    private final GroupManager groupManager;
    private final ProjectRoleManager projectRoleManager;
    private final WatcherManager watcherManager;
    private final UserManager userManager;
    private final CustomFieldManager customFieldManager;
    private final WorkflowTransitionUtil workflowTransitionUtil;
    private final AttachmentManager attachmentManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;

    public ScriptManagerImpl(
            GroupManager groupManager,
            ProjectRoleManager projectRoleManager,
            WatcherManager watcherManager,
            UserManager userManager,
            CustomFieldManager customFieldManager,
            AttachmentManager attachmentManager,
            JiraAuthenticationContext jiraAuthenticationContext) {
        this.groupManager = groupManager;
        this.projectRoleManager = projectRoleManager;
        this.watcherManager = watcherManager;
        this.userManager = userManager;
        this.customFieldManager = customFieldManager;
        this.workflowTransitionUtil = JiraUtils.loadComponent(WorkflowTransitionUtilImpl.class);
        this.attachmentManager = attachmentManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }

    @Override
    public Object executeScript(String groovyScript, Map<String, Object> parameters) throws WorkflowException {
        try {
            Script script = scriptCache.get(groovyScript);
            script.setBinding(fromMap(parameters));
            return script.run();
        } catch (ExecutionException ex) {
            throw new WorkflowException("Error executing groovy script", ex);
        }
    }

    @Override
    public String validateSyntax(String groovyScript) {
        try {
            shell.parse(groovyScript);
        } catch (CompilationFailedException e) {
            //e.getUnit().getErrorCollector();
            return e.getMessage();
        }
        return null;
    }

    private Binding fromMap(Map<String, Object> parameters) {
        Binding binding = new Binding();
        binding.setVariable("groupManager", groupManager);
        binding.setVariable("watcherManager", watcherManager);
        binding.setVariable("userManager", userManager);
        binding.setVariable("customFieldManager", customFieldManager);
        binding.setVariable("workflowTransitionUtil", workflowTransitionUtil);
        binding.setVariable("attachmentManager", attachmentManager);
        binding.setVariable("jiraAuthenticationContext", jiraAuthenticationContext);
        binding.setVariable("projectRoleManager", projectRoleManager);
        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                binding.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return binding;
    }
}
