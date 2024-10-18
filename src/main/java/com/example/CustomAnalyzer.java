package com.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.CharArraySet;

import java.util.Arrays;

public class CustomAnalyzer extends Analyzer {
    private final CharArraySet stopWords;

    public CustomAnalyzer() {
        // Create a set of common English stop words
        String[] stopWordsArray = {"a", "an", "and", "are", "as", "at", "be", "by", "for", "from",
                "has", "he", "in", "is", "it", "its", "of", "on", "that", "the", "to", "was",
                "were", "will", "with"};
        this.stopWords = new CharArraySet(Arrays.asList(stopWordsArray), true);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        // Tokenizer: splits text into tokens
        Tokenizer source = new StandardTokenizer();
        
        // LowerCaseFilter: converts tokens to lowercase
        TokenStream filter = new LowerCaseFilter(source);
        
        // StopFilter: removes common English stopwords using our custom stop word list
        filter = new StopFilter(filter, stopWords);
        
        // PorterStemFilter: applies stemming to reduce words to their base form
        filter = new PorterStemFilter(filter);
        
        // ASCIIFoldingFilter: converts special characters to their ASCII equivalents
        filter = new ASCIIFoldingFilter(filter);
        
        return new TokenStreamComponents(source, filter);
    }
}