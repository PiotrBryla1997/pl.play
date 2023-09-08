package com.example.playdocker.models;

public class Score {
    private String threatType;//Typ zagrożenia badany przez API google
    private String confidenceLevel;//Poziom zagrożenia stwarzany przez badany URL

    public Score() {
    }

    public String getThreatType() {
        return threatType;
    }

    public void setThreatType(String threatType) {
        this.threatType = threatType;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
}
