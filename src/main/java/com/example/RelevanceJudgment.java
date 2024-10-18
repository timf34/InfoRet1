package com.example;

public class RelevanceJudgment {
    private String queryId;
    private String docId;
    private int relevance;

    public RelevanceJudgment(String queryId, String docId, int relevance) {
        this.queryId = queryId;
        this.docId = docId;
        this.relevance = relevance;
    }

    // Getters
    public String getQueryId() { return queryId; }
    public String getDocId() { return docId; }
    public int getRelevance() { return relevance; }
}
