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

import java.util.HashMap;
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

    private final Map<String, Object> baseVariables;

    public ScriptManagerImpl(
            GroupManager groupManager,
            ProjectRoleManager projectRoleManager,
            WatcherManager watcherManager,
            UserManager userManager,
            CustomFieldManager customFieldManager,
            AttachmentManager attachmentManager,
            JiraAuthenticationContext jiraAuthenticationContext) {
        baseVariables = new HashMap<>();
        baseVariables.put("groupManager", groupManager);
        baseVariables.put("watcherManager", watcherManager);
        baseVariables.put("userManager", userManager);
        baseVariables.put("customFieldManager", customFieldManager);
        baseVariables.put("workflowTransitionUtil", JiraUtils.loadComponent(WorkflowTransitionUtilImpl.class));
        baseVariables.put("attachmentManager", attachmentManager);
        baseVariables.put("jiraAuthenticationContext", jiraAuthenticationContext);
        baseVariables.put("projectRoleManager", projectRoleManager);
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
        Map variables = new HashMap(baseVariables);

        if (parameters != null) {
            variables.putAll(parameters);
        }
        return new Binding(variables);
    }
}
