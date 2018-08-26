package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.impl;

import java.util.List;
import javax.annotation.Nonnull;

import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.EventType;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataCache;

public class ListenerDataCacheImpl implements ListenerDataCache {
    @Override
    public void createListener(ListenerData listenerData) {

    }

    @Override
    public void deleteListener(Integer listenerId) {

    }

    @Override
    public List<String> getScript(@Nonnull EventType eventType, @Nonnull Long projectId) {
        return null;
    }
}
