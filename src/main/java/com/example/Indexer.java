package com.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Indexer {
    public void indexDocuments(String docsPath, String indexPath, Analyzer analyzer) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

        // Create a new index in the directory, removing any previously indexed documents
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter writer = new IndexWriter(dir, iwc);

        // Parse documents
        List<DocumentData> documents = CranfieldParser.parseDocuments(docsPath);

        for (DocumentData docData : documents) {
            Document doc = new Document();

            doc.add(new StringField("docId", docData.getId(), Field.Store.YES));
            doc.add(new TextField("title", docData.getTitle(), Field.Store.YES));
            doc.add(new TextField("author", docData.getAuthor(), Field.Store.YES));
            doc.add(new TextField("content", docData.getContent(), Field.Store.YES));

            writer.addDocument(doc);
        }

        writer.close();
    }
}
