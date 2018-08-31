package ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ao;

import java.util.Date;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.StringLength;

import static net.java.ao.schema.StringLength.UNLIMITED;

@Preload
public interface ListenerDataAO extends Entity {
    String getNote();
    void setNote(String note);

    @Indexed
    Long getProjectId();
    void setProjectId(Long projectId);

    @Indexed
    String getEvent();
    void setEvent(String event);

    @StringLength(value = UNLIMITED)
    String getScript();
    void setScript(String script);

    Date getCreated();
    void setCreated(Date created);
}
