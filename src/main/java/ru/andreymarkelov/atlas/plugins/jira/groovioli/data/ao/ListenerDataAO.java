package ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ao;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;

@Preload
public interface ListenerDataAO extends Entity {
    String getNote();
    void setNote(String note);

    @Indexed
    String getProject();
    void setProject(String project);

    @Indexed
    String getEvent();
    void setEvent(String event);

    String getScript();
    void setScript(String script);
}
