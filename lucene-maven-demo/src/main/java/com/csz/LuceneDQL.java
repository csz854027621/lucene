package com.csz;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class LuceneDQL {

    private IndexSearcher  indexSearcher;


    @Before
    public void init() throws IOException {
        IndexReader indexReader= DirectoryReader.open(
                FSDirectory.open(new File("D:\\Desktop\\lucene-solr-elastic").toPath()));
        indexSearcher=new IndexSearcher(indexReader);
    }

    @After
    public void destroy() throws IOException {

    }

    @Test
    public void findAllByTerm() throws IOException {
        Query query=new TermQuery(new Term("name","spring"));
        printAll(query);
    }

    @Test
    public void findAllByRangeQuery() throws IOException {
        Query query = LongPoint.newRangeQuery("size", 0, -1);
        printAll(query);

    }

    @Test
    public void findAllByQueryParser() throws IOException, ParseException {
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        Query query = queryParser.parse("Lucene是java开发的");
        printAll(query);

    }

    public void printAll(Query query) throws IOException {
        TopDocs search = indexSearcher.search(query, 10);
        System.out.println("查询总数："+search.totalHits);
        for (ScoreDoc scoreDoc:search.scoreDocs){
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("name"));
        }
    }


}
