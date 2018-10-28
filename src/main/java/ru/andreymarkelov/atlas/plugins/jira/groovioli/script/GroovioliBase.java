package ru.andreymarkelov.atlas.plugins.jira.groovioli.script;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

public class GroovioliBase extends Script {
    @Override
    public Object invokeMethod(String name, Object args) {
        if ("addOption".equals(name)) {
            return ((GroovyObjectSupport) getProperty("fileOptionScript")).invokeMethod(name, args);
        }
        return super.invokeMethod(name, args);
    }

    @Override
    public Object run() {
        return null;
    }
}
