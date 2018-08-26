package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Query;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.EventType;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ao.ListenerDataAO;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

import static java.util.Collections.emptyList;

public class ListenerDataManagerImpl implements ListenerDataManager {
    private static final Long NO_PROJECT = -1L;

    private final ActiveObjects ao;
    private final Map<String, Map<Long, List<String>>> eventMap;
    private final List<ListenerData> listenerDataList;

    public ListenerDataManagerImpl(ActiveObjects ao) {
        this.ao = ao;
        this.eventMap = new HashMap<>();
        this.listenerDataList = new ArrayList<>();
    }

    @Override
    public void createListener(ListenerData listenerData) {
        ListenerDataAO listenerDataAO = ao.create(ListenerDataAO.class);
        listenerDataAO.setProjectId(listenerData.getProjectId());
        listenerDataAO.setEvent(listenerData.getEvent());
        listenerDataAO.setNote(listenerData.getNote());
        listenerDataAO.setScript(listenerData.getScript());
        listenerDataAO.setCreated(listenerData.getCreated());
        listenerDataAO.save();
        rebuildCache();
    }

    @Override
    public void deleteListener(Integer listenerId) {
        if (listenerId != null) {
            ListenerDataAO listenerDataAO = ao.get(ListenerDataAO.class, listenerId);
            if (listenerDataAO != null) {
                ao.delete(listenerDataAO);
                rebuildCache();
            }
        }
    }

    @Override
    public List<ListenerData> getAll() {
        if (listenerDataList.isEmpty()) {
            rebuildCache();
        }
        return listenerDataList;
    }

    @Override
    public ListenerData get(Integer listenerId) {
        if (listenerDataList.isEmpty()) {
            rebuildCache();
        }

        for (ListenerData listenerData : listenerDataList) {
            if (listenerData.getId().equals(listenerId)) {
                return listenerData;
            }
        }
        return null;
    }

    @Override
    public List<String> getScript(EventType eventType, Long projectId) {
        Map<Long, List<String>> projectMap = eventMap.get(eventType.name());
        if (projectMap != null) {
            List<String> scripts = projectMap.get(projectId);
            if (scripts != null) {
                return scripts;
            }
        }
        return emptyList();
    }

    private void rebuildCache() {
        eventMap.clear();
        listenerDataList.clear();
        List<ListenerData> dbListenerDataList = getAllInternal();
        for (ListenerData listenerData : dbListenerDataList) {
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
            listenerDataList.add(listenerData);
        }
    }

    private List<ListenerData> getAllInternal() {
        List<ListenerData> result = new ArrayList<>();
        ListenerDataAO[] listenerDataAOs = ao.find(ListenerDataAO.class, Query.select().order("CREATED DESC"));
        if (listenerDataAOs != null) {
            for (ListenerDataAO listenerDataAO : listenerDataAOs) {
                result.add(new ListenerData(
                        listenerDataAO.getID(),
                        listenerDataAO.getNote(),
                        listenerDataAO.getProjectId(),
                        listenerDataAO.getEvent(),
                        listenerDataAO.getScript(),
                        listenerDataAO.getCreated()
                ));
            }
        }
        return result;
    }
}
