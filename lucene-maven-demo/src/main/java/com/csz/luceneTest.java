package com.csz;


import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class luceneTest {


    private  Directory directory;
    private  Analyzer analyzer;
    private  IndexWriterConfig config;
    private  IndexWriter indexWriter;

    private IndexSearcher indexSearcher;
    private DirectoryReader indexReader;

    @Before
    public void init() throws IOException {
        analyzer = new IKAnalyzer();
        config = new IndexWriterConfig();
        directory = FSDirectory.open(
                new File("D:\\Desktop\\lucene-solr-elastic").toPath());

        //写入对象
        indexWriter = new IndexWriter(directory, config);


        //被搜索的（读取）对象
        indexReader = DirectoryReader.open(directory);
        //创建indexsearcher对象
        indexSearcher = new IndexSearcher(indexReader);


    }



    @Test
    public void create() throws IOException {

        File file = new File("D:\\Desktop\\searchsource");
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                String name = f.getName();
                Field fName = new TextField("fileName", name, Field.Store.YES);
                String content = FileUtils.readFileToString(f);
                Field fContent = new TextField("fileContent", content, Field.Store.YES);
                Document document = new Document();
                document.add(fContent);
                document.add(fName);
                document.add(fContent);
                document.add(fName);
                indexWriter.addDocument(document);

            }
        }
        indexWriter.close();
        directory.close();
    }

    @Test
    public void select() throws IOException {

        Query query=new TermQuery(new Term("fileName","spring"));

        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("查询结果的总条数："+ topDocs.totalHits);
        for (ScoreDoc  scoreDoc:topDocs.scoreDocs){
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("fileName"));
        }



    }

    @Test
    public void testTokenStream() throws Exception {
        //创建一个标准分析器对象
        Analyzer analyzer = new IKAnalyzer();
        //获得tokenStream对象
        //第一个参数：域名，可以随便给一个
        //第二个参数：要分析的文本内容
        TokenStream tokenStream = analyzer.tokenStream("test", "夜晚，云落山顶峰星空下，凉风习习万籁俱寂。\n" +
                "苏落眼底温柔中带了一丝甜蜜，柔情脉脉地凝望着眼前男子：“云起，等退出组织，我们就在这里定居好不好？”\n" +
                "云起剑眉星目中带着醉人的温柔：“丫头，就这么想退出么？”\n" +
                "苏落转身眺望远处的夜空，回头眼底带着一丝明媚灿烂的笑容：“这十几年来不是训练就是打打杀杀，时刻处于生死边缘，没有一刻安宁日子。现在我已经厌倦了这样的生活，很想快点脱离出来，难道你就不想吗？”\n" +
                "说着，苏落拿出手中的小锦盒在云起面前晃了晃：“猜猜，这里面是什么？”\n" +
                "苏落的眼睛亮晶晶的，盈满了幸福的神采。\n" +
                "沉浸在对未来美好生活向往中的她，没看到云起眼底闪过的一抹诡谲光芒。\n" +
                "“龙之戒？原来你已经拿到手了？这怎么可能？什么时候发生的事？”云起丹凤眼微眯，眼底是醉人的柔情。\n" +
                "“就在你去欧美执行任务的这段时间啊，运气好，所以就拿到了。这次，你跟我一起退出，好不好？”苏落拉着云起的手，满眼希冀，“把龙之戒还给组织，然后我们就在这里定居，幸福的生活下去，好不好？”\n" +
                "“好。”他轻柔的吻印在苏落光洁额头，强而有力的手一把将她拥入怀中，紧紧地拥抱着。\n" +
                "靠在他肩窝锁骨处，苏落眼底是满满的、幸福的微笑。\n" +
                "青梅竹马十几年，他们从枪林弹雨中一起携手走过，他是她最重要的亲人，也是最信任的人，现在她已经怀了他的宝宝，等退出组织后……忽然，苏");
        //添加一个引用，可以获得每个关键词
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //添加一个偏移量的引用，记录了关键词的开始位置以及结束位置
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        //将指针调整到列表的头部
        tokenStream.reset();
        //遍历关键词列表，通过incrementToken方法判断列表是否结束
        while(tokenStream.incrementToken()) {
            //关键词的起始位置
            System.out.print(offsetAttribute.startOffset()+"->" );
            //取关键词
            System.out.print(charTermAttribute);
            //结束位置
            System.out.println("->"+offsetAttribute.endOffset());
        }
        tokenStream.close();
    }
    @Test
    public void delete() throws IOException {
        indexWriter.deleteAll();
        indexWriter.close();
    }



}
