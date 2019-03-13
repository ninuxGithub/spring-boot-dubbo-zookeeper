package com.example.provider.elastic;

import com.example.provider.entity.Person;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author shenzm
 * @date 2019-3-4
 * @description 作用
 */
public class QueryTest {

    TransportClient client = null;

    @Before
    public void initClient() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

    }

    @After
    public void closeClient() {
        if (null != client) {
            client.close();
        }
    }


    @Test
    public void agg() {
        SearchResponse sr = client.prepareSearch()
                .addAggregation(
                        AggregationBuilders.terms("by_country").field("country")
                                .subAggregation(AggregationBuilders.dateHistogram("by_year")
                                        .field("dateOfBirth")
                                        .dateHistogramInterval(DateHistogramInterval.YEAR)
                                        .subAggregation(AggregationBuilders.avg("avg_children").field("children"))
                                )
                )
                .execute().actionGet();
    }

    @Test
    public void searchTemplate() {

        Map<String, Object> template_params = new HashMap<>();
        template_params.put("param_gender", "F");
        SearchResponse response = new SearchTemplateRequestBuilder(client)
                .setScript("{\"query\":{\"match\":{\"gender\":\"{{param_gender}}\"}}}")
                .setScriptType(ScriptType.INLINE)
                .setScriptParams(template_params)
                .setRequest(new SearchRequest())
                .get()
                .getResponse();
        System.out.println(response);
    }

    @Test
    public void aggTest() {
        SearchResponse searchResponse = client.prepareSearch()
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(AggregationBuilders.terms("agg1").field("field"))
                .addAggregation(AggregationBuilders.dateHistogram("agg2").field("birth").dateHistogramInterval(DateHistogramInterval.YEAR))
                .get();
        Aggregation agg1 = searchResponse.getAggregations().get("agg1");
        Aggregation agg2 = searchResponse.getAggregations().get("agg2");

        System.out.println(agg1);
        System.out.println(agg2);
    }

    @Test
    public void multiSearch() {
        SearchRequestBuilder srb1 = client.prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(1);
        SearchRequestBuilder srb2 = client.prepareSearch().setQuery(QueryBuilders.matchQuery("name", "kimchy")).setSize(1);

        MultiSearchResponse items =
                client.prepareMultiSearch()
                        .add(srb1)
                        .add(srb2)
                        .get();
        long nbHits = 0;
        for (MultiSearchResponse.Item item : items.getResponses()) {
            SearchResponse response = item.getResponse();
            nbHits += response.getHits().getTotalHits();
        }
        System.out.println(nbHits);

    }

    public void scroll() {
//        QueryBuilder qb = termQuery("multi", "test");
//
//        SearchResponse scrollResp = client.prepareSearch(test)
//                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
//                .setScroll(new TimeValue(60000))
//                .setQuery(qb)
//                .setSize(100).get(); //max of 100 hits will be returned for each scroll
//            //Scroll until no hits are returned
//        do {
//            for (SearchHit hit : scrollResp.getHits().getHits()) {
//                //Handle the hit...
//            }
//
//            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
//        } while(scrollResp.getHits().getHits().length != 0);
    }

    @Test
    public void reindex() {
        ReindexAction.INSTANCE.newRequestBuilder(client)
                .source("bank")
                .destination("shop")
                .filter(QueryBuilders.matchQuery("age", 40)).get();
    }

    @Test
    public void updateByQuery() {
        UpdateByQueryRequestBuilder updateByQueryRequestBuilder =
                UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        updateByQueryRequestBuilder.source("bank")
                .size(100)
                .source()
                .addSort("balance", SortOrder.DESC);
        BulkByScrollResponse bulkByScrollResponse = updateByQueryRequestBuilder.get();
        System.out.println(bulkByScrollResponse);


        //TODO
        UpdateByQueryRequestBuilder update = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        update.source("twitter").script(new Script(ScriptType.INLINE, "if(ctx._source.user=='kimchy'){ctx.op ='noop'}else if(ctx._source.message=='fine'){ctx.op='delete'}else{ctx._source.user='java xxx'}", "_id", Collections.emptyMap()));
        BulkByScrollResponse bulkByScrollResponse1 = update.get();
        System.out.println(bulkByScrollResponse1);

    }


    @Test
    public void bulkRequest() throws IOException {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        bulkRequestBuilder.add(client.prepareIndex("twitter", "_doc", "4")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
        );
        bulkRequestBuilder.add(client.prepareIndex("twitter", "_doc", "5")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
        );

        BulkResponse bulkItemResponses = bulkRequestBuilder.get();
        if (bulkItemResponses.hasFailures()) {
            System.out.println("fail");
        }


    }

    @Test
    public void multiGet() {
        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("twitter", "_doc", "1")
                .add("twitter", "_doc", "2", "3", "4")
                //.add("movies", "_doc", "1")
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                String json = response.getSourceAsString();
            }
        }
    }

    @Test
    public void updateApi() throws ExecutionException, InterruptedException, IOException {
        //update by script
        UpdateRequest updateRequest = new UpdateRequest("twitter", "_doc", "2")
                .script(new Script("ctx._source.user=\"java doc\""));
        client.update(updateRequest).get();


        //java update
        client.prepareUpdate("twitter", "_doc", "2")
                .setDoc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "api update")
                        .endObject()
                ).get();


        //upsert
        //如果不存在id 为3的记录 那么插入一个新的记录；  如果id 为3的记录存在， 那么修改message 为fine
        IndexRequest source = new IndexRequest("twitter", "_doc", "3")
                .source(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "test Upsert")
                        .field("message", "ok")
                        .endObject()
                );

        UpdateRequest upsert = new UpdateRequest("twitter", "_doc", "3")
                .doc(XContentFactory.jsonBuilder()
                        .startObject().field("message", "fine").endObject()
                ).upsert(source);
        client.update(upsert).get();

    }

    @Test
    public void queryDelete() {
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("user", "kimchy2"))
                .source("twitter")
                .execute(new ActionListener<BulkByScrollResponse>() {
                    @Override
                    public void onResponse(BulkByScrollResponse bulkByScrollResponse) {
                        long deleted = bulkByScrollResponse.getDeleted();
                        System.out.println(deleted);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println(e.getCause());
                    }
                });
    }


    @Test
    public void deleteApi() {
        DeleteResponse deleteResponse = client.prepareDelete("twitter", "_doc", "1").get();
        System.out.println(deleteResponse);
    }

    @Test
    public void getApi() {
        GetResponse response = client.prepareGet("twitter", "_doc", "1").get();
        System.out.println(response);
    }

    @Test
    public void indexApi() throws IOException {
        IndexResponse response = client.prepareIndex("twitter", "_doc", "2")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy2")
                        .field("postDate", new Date())
                        .field("message", "trying to Elasticsearch2")
                        .endObject()
                ).get();

        System.out.println(response);
        // Index name
        String _index = response.getIndex();
        // Type name
        String _type = response.getType();
        // Document ID (generated or not)
        String _id = response.getId();
        // Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();
        // status has stored current instance statement.
        RestStatus status = response.status();

        System.out.println(_index);
        System.out.println(_type);
        System.out.println(_id);
        System.out.println(_version);
        System.out.println(status);
    }

    public static void main(String[] args) throws IOException {
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        client.close();
    }

}
