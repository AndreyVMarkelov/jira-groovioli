package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Query;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ao.ListenerDataAO;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.ListenerDataManager;

public class ListenerDataManagerImpl implements ListenerDataManager {
    private final ActiveObjects ao;

    public ListenerDataManagerImpl(ActiveObjects ao) {
        this.ao = ao;
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
    }

    @Override
    public void deleteListener(Integer listenerId) {
        if (listenerId != null) {
            ListenerDataAO listenerDataAO = ao.get(ListenerDataAO.class, listenerId);
            if (listenerDataAO != null) {
                ao.delete(listenerDataAO);
            }
        }
    }

    @Override
    public List<ListenerData> getAll() {
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

    @Override
    public ListenerData get(Integer listenerId) {
        ListenerDataAO listenerDataAO = ao.get(ListenerDataAO.class, listenerId);
        if (listenerDataAO != null) {
            return new ListenerData(
                    listenerDataAO.getID(),
                    listenerDataAO.getNote(),
                    listenerDataAO.getProjectId(),
                    listenerDataAO.getEvent(),
                    listenerDataAO.getScript(),
                    listenerDataAO.getCreated()
            );
        }
        return null;
    }
}
