package com.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

public class AnalyzerFactory {
    public static Analyzer getAnalyzer(String type) {
        switch (type.toLowerCase()) {
            case "standard":
                return new StandardAnalyzer();
            case "custom":
                return new CustomAnalyzer();
            case "english":
                return new EnglishAnalyzer();
            default:
                return new StandardAnalyzer();
        }
    }
}
