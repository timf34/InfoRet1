package com.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.*;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            String docsPath = "data/cranfield/cran.all.1400";
            String indexPath = "index";
            String queriesPath = "data/cranfield/cran.qry";
            String qrelsPath = "data/cranfield/cran.rel";
            String resultsPathVSM = "results/vsm_results.txt";
            String resultsPathBM25 = "results/bm25_results.txt";
            String trecEvalPath = "trec_eval/trec_eval.exe";
            String evalOutputPathVSM = "trec_eval/eval_vsm.txt";
            String evalOutputPathBM25 = "trec_eval/eval_bm25.txt";

            // Choose analyzer
            Analyzer analyzer = AnalyzerFactory.getAnalyzer("custom");

            // Index documents
            Indexer indexer = new Indexer();
            indexer.indexDocuments(docsPath, indexPath, analyzer);

            // Parse queries
            Map<String, String> queries = CranfieldParser.parseQueries(queriesPath);

            // Search and evaluate using Vector Space Model
            Similarity vsmSimilarity = new ClassicSimilarity();
            Searcher searcherVSM = new Searcher(indexPath, analyzer, vsmSimilarity);
            Map<String, TopDocs> queryResultsVSM = new LinkedHashMap<>();

            for (Map.Entry<String, String> entry : queries.entrySet()) {
                String queryId = entry.getKey();
                String queryText = entry.getValue();
                TopDocs results = searcherVSM.search(queryText, 50);
                queryResultsVSM.put(queryId, results);
            }

            Evaluation evaluation = new Evaluation();
            evaluation.formatResults(queryResultsVSM, resultsPathVSM);
            evaluation.runTrecEval(qrelsPath, resultsPathVSM, trecEvalPath, evalOutputPathVSM);

            // Search and evaluate using BM25
            Similarity bm25Similarity = new BM25Similarity();
            Searcher searcherBM25 = new Searcher(indexPath, analyzer, bm25Similarity);
            Map<String, TopDocs> queryResultsBM25 = new LinkedHashMap<>();

            for (Map.Entry<String, String> entry : queries.entrySet()) {
                String queryId = entry.getKey();
                String queryText = entry.getValue();
                TopDocs results = searcherBM25.search(queryText, 50);
                queryResultsBM25.put(queryId, results);
            }

            evaluation.formatResults(queryResultsBM25, resultsPathBM25);
            evaluation.runTrecEval(qrelsPath, resultsPathBM25, trecEvalPath, evalOutputPathBM25);

            System.out.println("Search and evaluation completed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
