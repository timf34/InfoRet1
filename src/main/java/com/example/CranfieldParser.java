package com.example;

import java.io.*;
import java.util.*;

public class CranfieldParser {

    // Parse the cran.all.1400 documents
    public static List<DocumentData> parseDocuments(String filePath) throws IOException {
        List<DocumentData> documents = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder content = new StringBuilder();
        String docId = "";
        String title = "";
        String author = "";
        String contentField = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(".I")) {
                if (!docId.isEmpty()) {
                    documents.add(new DocumentData(docId, title, author, contentField));
                    title = "";
                    author = "";
                    contentField = "";
                }
                docId = line.substring(3).trim();
            } else if (line.startsWith(".T")) {
                content = new StringBuilder();
                while ((line = reader.readLine()) != null && !line.startsWith(".")) {
                    content.append(line).append(" ");
                }
                title = content.toString().trim();
                if (line == null) break;
                else continue;
            } else if (line.startsWith(".A")) {
                content = new StringBuilder();
                while ((line = reader.readLine()) != null && !line.startsWith(".")) {
                    content.append(line).append(" ");
                }
                author = content.toString().trim();
                if (line == null) break;
                else continue;
            } else if (line.startsWith(".W")) {
                content = new StringBuilder();
                while ((line = reader.readLine()) != null && !line.startsWith(".")) {
                    content.append(line).append(" ");
                }
                contentField = content.toString().trim();
                if (line == null) break;
                else continue;
            }
        }
        // Add the last document
        if (!docId.isEmpty()) {
            documents.add(new DocumentData(docId, title, author, contentField));
        }
        reader.close();
        return documents;
    }

    // Parse the cran.qry queries
    public static Map<String, String> parseQueries(String filePath) throws IOException {
        Map<String, String> queries = new LinkedHashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        String queryId = "";
        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(".I")) {
                if (!queryId.isEmpty()) {
                    queries.put(queryId, content.toString().trim());
                    content = new StringBuilder();
                }
                queryId = line.substring(3).trim();
            } else if (line.startsWith(".W")) {
                while ((line = reader.readLine()) != null && !line.startsWith(".")) {
                    content.append(line).append(" ");
                }
                if (line == null) break;
                else continue;
            }
        }
        // Add the last query
        if (!queryId.isEmpty()) {
            queries.put(queryId, content.toString().trim());
        }
        reader.close();
        return queries;
    }

    // Parse the cran.rel relevance judgments
    public static List<RelevanceJudgment> parseRelevance(String filePath) throws IOException {
        List<RelevanceJudgment> judgments = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.trim().split("\\s+");
            if (tokens.length >= 3) {
                String queryId = tokens[0];
                String docId = tokens[1];
                int relevance = Integer.parseInt(tokens[2]);
                judgments.add(new RelevanceJudgment(queryId, docId, relevance));
            }
        }
        reader.close();
        return judgments;
    }
}
