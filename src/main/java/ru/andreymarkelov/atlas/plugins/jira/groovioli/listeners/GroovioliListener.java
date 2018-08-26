package ru.andreymarkelov.atlas.plugins.jira.groovioli.listeners;

import java.util.List;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.DashboardViewEvent;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.user.LoginEvent;
import com.atlassian.jira.event.user.LogoutEvent;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.EventType;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

import static ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager.NO_PROJECT;

public class GroovioliListener implements InitializingBean, DisposableBean {
    private final EventPublisher eventPublisher;
    private final ListenerDataManager listenerDataManager;

    public GroovioliListener(
            EventPublisher eventPublisher,
            ListenerDataManager listenerDataManager) {
        this.eventPublisher = eventPublisher;
        this.listenerDataManager = listenerDataManager;
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
        List<String> scripts = listenerDataManager.getScript(EventType.ISSUE, issueEvent.getProject().getId());
    }

    @EventListener
    public void onDashboardViewEvent(DashboardViewEvent dashboardViewEvent) {
        List<String> scripts = listenerDataManager.getScript(EventType.DASHBOARD_VIEW, NO_PROJECT);
    }

    @EventListener
    public void onLoginEvent(LoginEvent loginEvent) {
        List<String> scripts = listenerDataManager.getScript(EventType.LOGIN, NO_PROJECT);
    }

    @EventListener
    public void onLogoutEvent(LogoutEvent logoutEvent) {
        List<String> scripts = listenerDataManager.getScript(EventType.LOGOUT, NO_PROJECT);
    }
}
