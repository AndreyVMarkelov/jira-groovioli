package ru.andreymarkelov.atlas.plugins.jira.groovioli.manager;

import ru.andreymarkelov.atlas.plugins.jira.groovioli.data.RestData;

import java.util.List;

public interface RestDataManager {
    void createRestData(RestData restData);
    void deleteRestData(Integer restDataId);
    List<RestData> getAll();
    RestData get(Integer restDataId);
    RestData getByPath(String path);
}
