package ru.andreymarkelov.atlas.plugins.jira.groovioli.data;

public class ListenerData {
    private Integer id;
    private String note;
    private String project;
    private String event;
    private String script;

    public ListenerData() {}

    public ListenerData(Integer id, String note, String project, String event, String script) {
        this.id = id;
        this.note = note;
        this.project = project;
        this.event = event;
        this.script = script;
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    @Override
    public String toString() {
        return "ListenerData{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", project='" + project + '\'' +
                ", event='" + event + '\'' +
                ", script='" + script + '\'' +
                '}';
    }
}
