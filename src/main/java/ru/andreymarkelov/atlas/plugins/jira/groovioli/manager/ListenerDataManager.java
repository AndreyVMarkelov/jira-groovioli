package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import java.util.List;

import com.atlassian.activeobjects.tx.Transactional;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.EventType;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;

@Transactional
public interface ListenerDataManager {
    Long NO_PROJECT = -1L;

    void createListener(ListenerData listenerData);
    void deleteListener(Integer listenerId);
    List<ListenerData> getAll();
    ListenerData get(Integer listenerId);
    List<String> getScript(EventType eventType, Long projectId);
}
