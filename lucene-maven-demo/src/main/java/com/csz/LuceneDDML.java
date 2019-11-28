package com.csz;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;


import java.io.File;
import java.io.IOException;

public class LuceneDDML {

    private IndexWriter indexWriter;

    @Before
    public void init() throws IOException {
        indexWriter = new IndexWriter(
                FSDirectory.open(
                 new File("D:\\Desktop\\lucene-solr-elastic").toPath()),
                new IndexWriterConfig(new IKAnalyzer()));
    }
    @After
    public void destroy() throws IOException {
        indexWriter.close();
    }

    @Test
    public void add() throws IOException {

        File originFile = new File("D:\\Desktop\\searchsource");
        for (File file : originFile.listFiles()) {
            if (file.isFile()) {
                String fileName = file.getName();
                long fileSize = FileUtils.sizeOf(file);
                String fileContent = FileUtils.readFileToString(file, "utf-8");
                String filePath = file.getPath();
                Field name = new TextField("name", fileName, Field.Store.YES);
                Field size=new LongPoint("size",fileSize);
                Field sizeStore = new StoredField("size", fileSize);
                Field content = new TextField("content", fileContent, Field.Store.YES);
                Field path = new StringField("path", filePath, Field.Store.YES);
                Document document = new Document();
                document.add(name);
                // document.add(size);
                document.add(sizeStore);
                document.add(content);
                document.add(path);
                indexWriter.addDocument(document);
            }
        }
        indexWriter.close();

    }

    @Test
    public void deleteAll() throws IOException {
        indexWriter.deleteAll();
    }
    @Test
    public void delete() throws IOException {
        indexWriter.deleteDocuments(new Term("name","spring"));

    }

    @Test
    public void update() throws IOException {
        Document document=new Document();
        document.add(new TextField("name","修改文件", Field.Store.YES));
        document.add(new TextField("content","修改文件", Field.Store.YES));
        document.add(new TextField("path","修改文件", Field.Store.YES));
        indexWriter.updateDocument(new Term("name","spring"),document);
        indexWriter.close();
    }



}
