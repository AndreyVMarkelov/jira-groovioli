package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.EventType;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataCache;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

import static java.util.Collections.emptyList;

public class ListenerDataCacheImpl implements ListenerDataCache, InitializingBean, DisposableBean {
    private static final Long NO_PROJECT = -1L;

    private final ListenerDataManager listenerDataManager;
    private final Map<String, Map<Long, List<String>>> eventMap;

    public ListenerDataCacheImpl(ListenerDataManager listenerDataManager) {
        this.listenerDataManager = listenerDataManager;
        this.eventMap = new HashMap<>();
    }

    @Override
    public List<String> getScript(@Nonnull EventType eventType, @Nonnull Long projectId) {
        Map<Long, List<String>> projectMap = eventMap.get(eventType.name());
        if (projectMap != null) {
            List<String> scripts = projectMap.get(projectId);
            if (scripts != null) {
                return scripts;
            }
        }
        return emptyList();
    }

    @Override
    public void createListener(ListenerData listenerData) {
        listenerDataManager.createListener(listenerData);
        rebuildCache();
    }

    @Override
    public void deleteListener(Integer listenerId) {
        listenerDataManager.deleteListener(listenerId);
        rebuildCache();
    }

    @Override
    public void afterPropertiesSet() {
        buildCache();
    }

    @Override
    public void destroy() {
        clearCache();
    }

    private void buildCache() {
        List<ListenerData> listenerDataList = listenerDataManager.getAll();
        for (ListenerData listenerData : listenerDataList) {
            final String event = listenerData.getEvent();
            final Long projectId = (listenerData.getProjectId() != null) ? listenerData.getProjectId() : NO_PROJECT;
            if (!eventMap.containsKey(event)) {
                eventMap.put(event, new HashMap<Long, List<String>>());
            }

            Map<Long, List<String>> projectMap = eventMap.get(event);
            if (!projectMap.containsKey(projectId)) {
                projectMap.put(projectId, new ArrayList<String>());
            }

            List<String> projectScripts = projectMap.get(projectId);
            projectScripts.add(listenerData.getScript());
        }
    }

    private void clearCache() {
        eventMap.clear();
    }

    private void rebuildCache() {
        clearCache();
        buildCache();
    }
}
