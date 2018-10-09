package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.impl;

import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.Query;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.RestData;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ao.RestDataAO;
import ru.andreymarkelov.atlas.plugins.jira.groovioli.manager.RestDataManager;

import java.util.ArrayList;
import java.util.List;

public class RestDataManagerImpl implements RestDataManager {
    private final ActiveObjects ao;

    public RestDataManagerImpl(ActiveObjects ao) {
        this.ao = ao;
    }

    @Override
    public void createRestData(RestData restData) {
        RestDataAO restDataAO = ao.create(RestDataAO.class);
        restDataAO.setNote(restData.getNote());
        restDataAO.setPath(restData.getPath());
        restDataAO.setPerformer(restData.getPerformer());
        restDataAO.setScript(restData.getScript());
        restDataAO.setCreated(restData.getCreated());
        restDataAO.save();
    }

    @Override
    public void deleteRestData(Integer restDataId) {
        if (restDataId != null) {
            RestDataAO restDataAO = ao.get(RestDataAO.class, restDataId);
            if (restDataAO != null) {
                ao.delete(restDataAO);
            }
        }
    }

    @Override
    public List<RestData> getAll() {
        List<RestData> result = new ArrayList<>();
        RestDataAO[] restDataAOs = ao.find(RestDataAO.class, Query.select().order("CREATED DESC"));
        if (restDataAOs != null) {
            for (RestDataAO restDataAO : restDataAOs) {
                result.add(new RestData(
                        restDataAO.getID(),
                        restDataAO.getNote(),
                        restDataAO.getPath(),
                        restDataAO.getPerformer(),
                        restDataAO.getScript(),
                        restDataAO.getCreated()
                ));
            }
        }
        return result;
    }
}
