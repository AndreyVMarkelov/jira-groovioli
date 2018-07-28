package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import java.util.List;

import com.atlassian.activeobjects.tx.Transactional;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;

@Transactional
public interface ListenerDataManager {
    void createListener(ListenerData listenerData);
    List<ListenerData> getAll();
}
