package ru.andreymarkelov.atlas.plugins.jira.groovioli.script;

import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.issuetype.IssueType;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObjectSupport;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class ConstantDsl extends GroovyObjectSupport {
    public IssueType findIssueType(@DelegatesTo(FindIssueTypeHandler.class) Closure closure) {
        FindIssueTypeHandler findIssueTypeHandler = new FindIssueTypeHandler();
        closure.setDelegate(findIssueTypeHandler);
        closure.call();

        ConstantsManager constantsManager = (ConstantsManager) closure.getProperty("constantsManager");
        Collection<IssueType> allIssueTypeObjects =  constantsManager.getAllIssueTypeObjects();
        for (IssueType issueType : allIssueTypeObjects) {
            if (issueType.getName().equals(findIssueTypeHandler.name)) {
                return issueType;
            }
        }
        return null;
    }

    @SuppressWarnings("WeakerAccess")
    public class FindIssueTypeHandler {
        private String name = null;

        public boolean isValid() {
            return StringUtils.isNotBlank(name);
        }

        public void name(String name) {
            this.name = name;
        }
    }
}
