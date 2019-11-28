package com.csz.solr;

import com.sun.net.httpserver.HttpServer;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import javax.management.Query;
import java.io.IOException;

public class SolrTest {

    @Test
    public void add() throws IOException, SolrServerException {
        HttpSolrServer  solrServer= new HttpSolrServer(
                "http://localhost:8080/solr/collection1/");
        SolrInputDocument document=new SolrInputDocument();
        document.addField("id","1");
        document.addField("name","csz");
        document.addField("content","caijia");
        document.addField("cat","13755748007");
        solrServer.add(document);
        solrServer.commit();
    }



    @Test
    public void update() throws IOException, SolrServerException {
        HttpSolrServer  solrServer= new HttpSolrServer(
                "http://localhost:8080/solr/collection1/");
        SolrInputDocument document=new SolrInputDocument();
        document.addField("id","1");
        document.addField("name","csz1");
        document.addField("content","caijia1");
        document.addField("cat","137557480071");
        solrServer.add(document);
        solrServer.commit();
    }

    @Test
    public void delete() throws IOException, SolrServerException {
        HttpSolrServer  solrServer= new HttpSolrServer(
                "http://localhost:8080/solr/collection1/");
        //solrServer.deleteById("1");
        solrServer.deleteByQuery("id:1");
        solrServer.commit();
    }

    @Test
    public void select() throws SolrServerException {
        HttpSolrServer solr=
                new HttpSolrServer("http://localhost:8080/solr/collection1/");
        SolrQuery query=new SolrQuery();
        query.setQuery("id:1");
        QueryResponse queryResponse = solr.query(query);
        SolrDocumentList results = queryResponse.getResults();
        results.forEach(result->{
            System.out.println(result.get("id"));
        });


    }

}
