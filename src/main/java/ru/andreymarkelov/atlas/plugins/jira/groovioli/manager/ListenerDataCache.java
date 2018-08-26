package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import java.util.List;
import javax.annotation.Nonnull;

import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.EventType;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;

public interface ListenerDataCache {
    void createListener(ListenerData listenerData);
    void deleteListener(Integer listenerId);
    List<String> getScript(@Nonnull EventType eventType, @Nonnull Long projectId);
}
