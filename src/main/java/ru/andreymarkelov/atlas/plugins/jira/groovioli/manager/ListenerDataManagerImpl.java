package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Query;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ListenerData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ao.ListenerDataAO;

public class ListenerDataManagerImpl implements ListenerDataManager {
    private final ActiveObjects ao;

    public ListenerDataManagerImpl(ActiveObjects ao) {
        this.ao = ao;
    }

    @Override
    public void createListener(ListenerData listenerData) {
        ListenerDataAO listenerDataAO = ao.create(ListenerDataAO.class);
        listenerDataAO.setProject(listenerData.getProject());
        listenerDataAO.setEvent(listenerData.getEvent());
        listenerDataAO.setNote(listenerData.getNote());
        listenerDataAO.setScript(listenerData.getScript());
        listenerDataAO.save();
    }

    @Override
    public List<ListenerData> getAll() {
        List<ListenerData> result = new ArrayList<>();
        ListenerDataAO[] listenerDataAOs = ao.find(ListenerDataAO.class, Query.select().order(""));
        if (listenerDataAOs != null) {
            for (ListenerDataAO listenerDataAO : listenerDataAOs) {

            }
        }
        return result;
    }
}
