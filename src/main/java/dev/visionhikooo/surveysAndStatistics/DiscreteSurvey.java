package dev.visionhikooo.surveysAndStatistics;

import java.util.HashMap;

public class DiscreteSurvey extends Survey {

    private HashMap<String, Integer> antworten;

    public DiscreteSurvey(String frage) {
        super(frage);
        antworten = new HashMap<>();
    }

    @Override
    public void init(String... answerchoices) {
        for (String s : answerchoices) {
            antworten.put(s, 0);
        }
    }

    public void teilnahme(long id, String answer) {
        addTeilnehmer(id);
        if (antworten.containsKey(answer))
            antworten.put(answer, antworten.get(answer) + 1);
    }
}
