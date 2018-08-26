package ru.andreymarkelov.atlas.plugins.jira.groovioli.listeners;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.DashboardViewEvent;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.issue.IssueViewEvent;
import com.atlassian.jira.event.user.LoginEvent;
import com.atlassian.jira.event.user.LogoutEvent;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

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
    }

    @EventListener
    public void onDashboardViewEvent(DashboardViewEvent dashboardViewEvent) {
    }

    @EventListener
    public void onIssueViewEvent(IssueViewEvent issueViewEvent) {
    }

    @EventListener
    public void onLoginEvent(LoginEvent loginEvent) {
    }

    @EventListener
    public void onLogoutEvent(LogoutEvent logoutEvent) {
    }
}
