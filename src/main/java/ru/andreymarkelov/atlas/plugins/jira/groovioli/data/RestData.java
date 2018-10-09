package ru.andreymarkelov.atlas.plugins.jira.groovioli.data;

import java.util.Date;

public class RestData {
    private Integer id;
    private String note;
    private String path;
    private String performer;
    private String script;
    private Date created;

    public RestData() {}

    public RestData(String note, String path, String performer, String script) {
        this(null, note, path, performer, script, new Date());
    }

    public RestData(Integer id, String note, String path, String performer, String script, Date created) {
        this.id = id;
        this.note = note;
        this.path = path;
        this.performer = performer;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
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
        return "RestData{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", path='" + path + '\'' +
                ", performer='" + performer + '\'' +
                ", script='" + script + '\'' +
                ", created=" + created +
                '}';
    }
}
