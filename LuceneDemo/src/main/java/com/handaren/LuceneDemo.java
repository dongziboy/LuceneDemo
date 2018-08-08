package com.handaren;

import com.hankcs.lucene.HanLPAnalyzer;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class LuceneDemo {

    @Test
    public void indexWriter() throws Exception {
//        设置索引库地址
        Directory d = FSDirectory.open(Paths.get("D:\\itcast\\11_lucene&solr\\index_library"));
//         指定第三方分词器
        IndexWriterConfig conf = new IndexWriterConfig(new HanLPAnalyzer());
//        提供一个写入索引库的对象
        IndexWriter indexWriter = new IndexWriter(d, conf);

//        得到数据源的所有文件
        File sourceFile = new File("D:\\itcast\\11_lucene&solr\\source");
//        获得数据源中的所有文件
        File[] files = sourceFile.listFiles();
        for (File file : files) {
            String fileName = file.getName();
//            获得文件内容
            String fileContext = FileUtils.readFileToString(file);
//            获得文件路径
            String filePath = file.getPath();
            long filrSize = FileUtils.sizeOf(file);
            System.out.println(fileName);
            System.out.println(fileContext);
            System.out.println(filePath);
            System.out.println(filrSize);
//            设置三个是否，是否分词，是否索引，是否存储
//            参数一：域字段名；参数二：解析的具体指；参数三：是否存储
            TextField fName = new TextField("fileName", fileName, Field.Store.YES);//分词，索引，自定义存储
            TextField fContext = new TextField("fileContext", fileContext, Field.Store.YES);//分词，索引，自定义存储
            StoredField fPath = new StoredField("filePath", filePath);
            StoredField fSize = new StoredField("fileSize", filrSize);

//            创建文档，并存储
            Document doc = new Document();
            doc.add(fName);
            doc.add(fContext);
            doc.add(fPath);
            doc.add(fSize);

//            把文档写入索引库
            indexWriter.addDocument(doc);
        }
//        记得关闭资源
        indexWriter.close();
    }

    /**
     * 删除索引库中内容
     */
    @Test
    public void delIndex() throws IOException {
//        获取索引库位置
        Directory d = FSDirectory.open(Paths.get("D:\\itcast\\11_lucene&solr\\index_library"));
//        指定分词器
        IndexWriterConfig conf = new IndexWriterConfig(new HanLPAnalyzer());
//        获取索引库对象
        IndexWriter indexWriter = new IndexWriter(d, conf);
        indexWriter.deleteAll();
        indexWriter.close();
    }

    /**
     * 查询索引库
     */
    @Test
    public void queryIndex() throws IOException {
//        获取索引库位置
        Directory d = FSDirectory.open(Paths.get("D:\\itcast\\11_lucene&solr\\index_library"));
//        获取indexReader对象
        IndexReader indexReader = DirectoryReader.open(d);
//        把indexReader对象二次分装
        IndexSearcher searcher = new IndexSearcher(indexReader);

//        new不同的查询器，有不同的效果
//        查询所有
        Query query1 = new MatchAllDocsQuery();






//        下面是查询，传入不同的查询器，有不同的效果，
//        注，search方法之恩那个查询到文档数量和文档编号的集合
//                      查询器     查询条数
        TopDocs topDocs = searcher.search(query1, 6);
        int total = topDocs.totalHits;
        System.out.println("文档数量---"+total);

//        遍历文档
//        获取文档编号集合
        ScoreDoc[] docs = topDocs.scoreDocs;
        for (ScoreDoc doc : docs) {
//            获取每个文档编号
            int num = doc.doc;
            System.out.println("文档编号"+num);
//            通过编号查询文档
            Document document = searcher.doc(num);
            System.out.println("域名称"+document.get("fileName"));
            document.get("文档内容"+document.get("fileContext"));
            document.get("文档路径"+document.get("filePath"));
            document.get("文档大小"+document.get("fileSize"));
        }
//    关闭资源
        indexReader.close();
    }
}












