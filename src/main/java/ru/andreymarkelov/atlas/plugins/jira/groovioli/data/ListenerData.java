package ru.andreymarkelov.atlas.plugins.jira.groovioli.data;

import java.util.Date;

public class ListenerData {
    private Integer id;
    private String note;
    private Long projectId;
    private String event;
    private String script;
    private Date created;

    public ListenerData() {}

    public ListenerData(String note, Long projectId, String event, String script) {
        this(null, note, projectId, event, script, new Date());
    }

    public ListenerData(Integer id, String note, Long projectId, String event, String script, Date created) {
        this.id = id;
        this.note = note;
        this.projectId = projectId;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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
                ", projectId=" + projectId +
                ", event='" + event + '\'' +
                ", script='" + script + '\'' +
                ", created=" + created +
                '}';
    }
}
