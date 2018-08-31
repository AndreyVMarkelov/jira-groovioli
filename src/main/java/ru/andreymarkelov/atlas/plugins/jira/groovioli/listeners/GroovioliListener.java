package ru.andreymarkelov.atlas.plugins.jira.groovioli.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.DashboardViewEvent;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.user.LoginEvent;
import com.atlassian.jira.event.user.LogoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.EventType;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ScriptManager;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.util.ScriptException;

import static ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager.NO_PROJECT;

public class GroovioliListener implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(GroovioliListener.class);

    private final EventPublisher eventPublisher;
    private final ListenerDataManager listenerDataManager;
    private final ScriptManager scriptManager;

    public GroovioliListener(
            EventPublisher eventPublisher,
            ListenerDataManager listenerDataManager,
            ScriptManager scriptManager) {
        this.eventPublisher = eventPublisher;
        this.listenerDataManager = listenerDataManager;
        this.scriptManager = scriptManager;
    }

    @Override
    public void afterPropertiesSet() {
        eventPublisher.register(this);
    }

    @Override
    public void destroy() {
        eventPublisher.unregister(this);
    }

    @EventListener
    public void onIssueEvent(IssueEvent issueEvent) {
        List<String> scripts = listenerDataManager.getScripts(EventType.ISSUE, issueEvent.getProject().getId());
        for (String script : scripts) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("changeLog", issueEvent.getChangeLog());
            parameters.put("eventComment", issueEvent.getComment());
            parameters.put("eventWorklog", issueEvent.getWorklog());
            parameters.put("issue", issueEvent.getIssue());
            parameters.put("eventTypeId", issueEvent.getEventTypeId());
            parameters.put("eventUser", issueEvent.getUser());
            parameters.put("log", log);
            try {
                scriptManager.executeScript(script, parameters);
            } catch (ScriptException ex) {
                log.error("Listener error", ex);
            }
        }
    }

    @EventListener
    public void onDashboardViewEvent(DashboardViewEvent dashboardViewEvent) {
        List<String> scripts = listenerDataManager.getScripts(EventType.DASHBOARD_VIEW, NO_PROJECT);
    }

    @EventListener
    public void onLoginEvent(LoginEvent loginEvent) {
        List<String> scripts = listenerDataManager.getScripts(EventType.LOGIN, NO_PROJECT);
    }

    @EventListener
    public void onLogoutEvent(LogoutEvent logoutEvent) {
        List<String> scripts = listenerDataManager.getScripts(EventType.LOGOUT, NO_PROJECT);
    }
}
