package ru.andreymarkelov.atlas.plugins.jira.groovioli.script;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

public class GroovioliBase extends Script {
    @Override
    public Object invokeMethod(String name, Object args) {
        if ("addOption".equals(name)) {
            return ((GroovyObjectSupport) getProperty("fieldOptionDsl")).invokeMethod(name, args);
        } else if ("findProject".equals(name)) {
            return ((GroovyObjectSupport) getProperty("projectDsl")).invokeMethod(name, args);
        } else if ("findIssueType".equals(name)) {
            return ((GroovyObjectSupport) getProperty("constantDsl")).invokeMethod(name, args);
        } else if ("newIssue".equals(name)) {
            return ((GroovyObjectSupport) getProperty("issueDsl")).invokeMethod(name, args);
        }
        return super.invokeMethod(name, args);
    }

    @Override
    public Object run() {
        return null;
    }
}
