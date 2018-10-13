package ru.andreymarkelov.atlas.plugins.jira.groovioli.data.ao;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.StringLength;

import java.util.Date;

import static net.java.ao.schema.StringLength.UNLIMITED;

@Preload
public interface RestDataAO extends Entity {
    String getNote();
    void setNote(String note);

    @Indexed
    String getPath();
    void setPath(String path);

    String getPerformer();
    void setPerformer(String performer);

    @StringLength(value = UNLIMITED)
    String getScript();
    void setScript(String script);

    Date getCreated();
    void setCreated(Date created);
}
