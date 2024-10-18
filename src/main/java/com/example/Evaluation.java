package com.example;

import org.apache.lucene.search.*;
import java.io.*;
import java.util.*;

public class Evaluation {

    public void formatResults(Map<String, TopDocs> queryResults, String outputPath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));

        for (Map.Entry<String, TopDocs> entry : queryResults.entrySet()) {
            String queryId = entry.getKey();
            TopDocs topDocs = entry.getValue();
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            int rank = 1;

            for (ScoreDoc scoreDoc : scoreDocs) {
                String docId = String.valueOf(scoreDoc.doc + 1); // Adjusting for 0-based indexing
                float score = scoreDoc.score;
                writer.write(String.format("%s Q0 %s %d %f STANDARD\n", queryId, docId, rank, score));
                rank++;
            }
        }

        writer.close();
    }

    public void runTrecEval(String qrelsPath, String resultsPath, String trecEvalPath, String evalOutputPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(trecEvalPath, qrelsPath, resultsPath);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new FileWriter(evalOutputPath));
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
        reader.close();
        process.waitFor();
    }
}
