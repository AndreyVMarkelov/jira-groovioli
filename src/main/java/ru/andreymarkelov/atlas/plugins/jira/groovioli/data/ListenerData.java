package ru.andreymarkelov.atlas.plugins.jira.groovioli.data;

import java.util.Date;
import java.util.List;

public class ListenerData {
    private Integer id;
    private String note;
    private List<Long> projectIds;
    private String event;
    private String script;
    private Date created;

    public ListenerData() {}

    public ListenerData(String note, List<Long> projectIds, String event, String script) {
        this(null, note, projectIds, event, script, new Date());
    }

    public ListenerData(Integer id, String note, List<Long> projectIds, String event, String script, Date created) {
        this.id = id;
        this.note = note;
        this.projectIds = projectIds;
        this.event = event;
        this.script = script;
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Long> projectIds) {
        this.projectIds = projectIds;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "ListenerData{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", projectIds=" + projectIds +
                ", event='" + event + '\'' +
                ", script='" + script + '\'' +
                ", created=" + created +
                '}';
    }
}
