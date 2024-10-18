package com.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.Similarity;

import java.io.IOException;
import java.nio.file.Paths;

public class Searcher {
    private final IndexSearcher searcher;
    private final Analyzer analyzer;

    public Searcher(String indexPath, Analyzer analyzer, Similarity similarity) throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        this.searcher = new IndexSearcher(reader);
        this.searcher.setSimilarity(similarity);
        this.analyzer = analyzer;

        // Increase the maximum number of clauses
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
    }

    public TopDocs search(String queryStr, int numResults) throws Exception {
        CustomQueryParser parser = new CustomQueryParser("content", analyzer);
        Query query = parser.parse(queryStr);
        return searcher.search(query, numResults);
    }

    public Document getDocument(int docId) throws IOException {
        return searcher.doc(docId);
    }

    private static class CustomQueryParser extends QueryParser {
        public CustomQueryParser(String field, Analyzer analyzer) {
            super(field, analyzer);
            setAllowLeadingWildcard(true);
        }

        @Override
        protected Query getFieldQuery(String field, String queryText, boolean quoted) throws org.apache.lucene.queryparser.classic.ParseException {
            // For very long queries, treat them as phrases instead of individual terms
            if (queryText.split("\\s+").length > 10) {
                return getPhraseQuery(field, queryText, 0);
            }
            return super.getFieldQuery(field, queryText, quoted);
        }
    }
}