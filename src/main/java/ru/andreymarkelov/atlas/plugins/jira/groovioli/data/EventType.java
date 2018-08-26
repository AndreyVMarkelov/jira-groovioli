package ru.andreymarkelov.atlas.plugins.jira.groovioli.data;

import java.util.ArrayList;
import java.util.List;

public enum EventType {
    ISSUE(true),
    ISSUE_VIEW(true),
    DASHBOARD_VIEW(false),
    LOGIN(false),
    LOGOUT(false);

    private final boolean projectRequired;

    EventType(boolean projectRequired) {
        this.projectRequired = projectRequired;
    }

    public boolean isProjectRequired() {
        return projectRequired;
    }

    public static List<String> getAllEvents() {
        List<String> result = new ArrayList<>();
        for (EventType eventType : values()) {
            result.add(eventType.name());
        }
        return result;
    }
}
