package dev.visionhikooo.surveysAndStatistics;

import java.util.LinkedList;

public abstract class Survey {
    private final String frage;
    private LinkedList<Long> teilnehmer;

    public Survey(String frage) {
        this.frage = frage;
        teilnehmer = new LinkedList<>();
    }

    public void addTeilnehmer(long id) {
        teilnehmer.add(id);
    }

    public String getFrage() {
        return frage;
    }

    public abstract void init(String... answerchoices);
}
