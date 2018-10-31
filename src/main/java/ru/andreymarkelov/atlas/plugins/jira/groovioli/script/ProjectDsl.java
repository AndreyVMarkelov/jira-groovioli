package ru.andreymarkelov.atlas.plugins.jira.groovioli.script;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObjectSupport;
import org.apache.commons.lang3.StringUtils;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.util.ScriptDslException;

public class ProjectDsl extends GroovyObjectSupport {
    public Project findProject(@DelegatesTo(FindProjectHandler.class) Closure closure) {
        FindProjectHandler findProjectHandler = new FindProjectHandler();
        closure.setDelegate(findProjectHandler);
        closure.call();

        if (!findProjectHandler.isValid()) {
            throw new ScriptDslException("Invalid parameters for findProject");
        }

        ProjectManager projectManager = (ProjectManager) closure.getProperty("projectManager");
        if (StringUtils.isNotBlank(findProjectHandler.key)) {
            return projectManager.getProjectObjByKey(findProjectHandler.key);
        } else {
            return projectManager.getProjectObjByName(findProjectHandler.name);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public class FindProjectHandler {
        private String key = null;
        private String name = null;

        public boolean isValid() {
            return StringUtils.isNotBlank(key) || StringUtils.isNotBlank(name);
        }

        public void key(String key) {
            this.key = key;
        }

        public void name(String name) {
            this.name = name;
        }
    }
}
