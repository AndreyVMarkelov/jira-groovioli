package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Query;
import org.apache.commons.lang3.StringUtils;
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
        listenerDataAO.setProjectIds(projectIdsToStr(listenerData.getProjectIds()));
        listenerDataAO.setEvent(listenerData.getEvent());
        listenerDataAO.setNote(listenerData.getNote());
        listenerDataAO.setScript(listenerData.getScript());
        listenerDataAO.setCreated(listenerData.getCreated());
        listenerDataAO.save();
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
                        projectIdsFromStr(listenerDataAO.getProjectIds()),
                        listenerDataAO.getEvent(),
                        listenerDataAO.getScript(),
                        listenerDataAO.getCreated()
                ));
            }
        }
        return result;
    }

    private static List<Long> projectIdsFromStr(String projectIdsStr) {
        String[] tokens = StringUtils.split(projectIdsStr, ",");
        List<Long> res = new ArrayList<>(tokens.length);
        for (int i = 0; i < tokens.length; i++) {
            res.add(Long.valueOf(tokens[i]));
        }
        return res;
    }

    private static String projectIdsToStr(List<Long> projectIds) {
        return StringUtils.join(projectIds, ",");
    }
}
