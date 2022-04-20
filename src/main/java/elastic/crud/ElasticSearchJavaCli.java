package elastic.crud;

import com.alibaba.fastjson.JSON;
import elastic.crud.model.Person;
import lombok.SneakyThrows;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ElasticSearchJavaCli {

    @Autowired
    RestHighLevelClient client;

    public void indexDocument(){
        Person person=new Person(1,"java",new Date(),32);

        IndexRequest indexRequest = new IndexRequest("myindex").id("1");
        indexRequest.source(JSON.toJSON(person), XContentType.JSON);



        try {
            IndexResponse indexResponse = client.index(indexRequest,
                    RequestOptions.DEFAULT);
            System.out.println(indexResponse.getIndex());

            GetRequest getRequest=new GetRequest("myindex","1");
            GetResponse getResponse = client.get(getRequest,
                    RequestOptions.DEFAULT);
            System.out.println(getResponse.getSource());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @SneakyThrows
    public void searchRequestExample(){
        System.out.println("Entered search request ");
        SearchRequest searchRequest=new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse=client.search(searchRequest,RequestOptions.DEFAULT);

        SearchHit[] searchHits=searchResponse.getHits().getHits();

        List<Person> personList= Arrays.stream(searchHits).map(hit-> JSON.parseObject(hit.getSourceAsString(),Person.class)).collect(Collectors.toList());

        System.out.println(personList.toString());

    }
    public void reIndexDocs(){
        ReindexRequest request = new ReindexRequest();
        request.setSourceIndices("employee_index");
        request.setDestIndex("emp");
        request.setDestVersionType(VersionType.EXTERNAL);
        try {
            BulkByScrollResponse bulkResponse =
                    client.reindex(request, RequestOptions.DEFAULT);
            System.out.println(bulkResponse.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
