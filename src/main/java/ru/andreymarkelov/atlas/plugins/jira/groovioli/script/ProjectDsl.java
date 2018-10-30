package ru.andreymarkelov.atlas.plugins.jira.groovioli.script;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import groovy.lang.GroovyObjectSupport;

public class ProjectDsl extends GroovyObjectSupport {
    public Project projectByName(String projectName) {
        ProjectManager projectManager = (ProjectManager) getProperty("projectManager");
        return projectManager.getProjectObjByKey(projectName);
    }
}
