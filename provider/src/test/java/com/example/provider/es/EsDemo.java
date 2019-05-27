package com.example.provider.es;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.missing.MissingAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.*;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author shenzm
 * @date 2019-5-24
 * @description 作用
 */

public class EsDemo {

    private String host = "10.1.51.96";

    private int port = 9300;

    private String clusterName = "my-application";

    private TransportClient client;

    @Before
    public void init() {
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", clusterName)
                    //来使客户端去嗅探整个集群的状态
                    //.put("client.transport.sniff", true)
                    .put("client.transport.ping_timeout", "600s")
                    .build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @After
    public void destroy() {
        if (null != client) {
            client.close();
        }
    }

    @Test
    public void testQuery() {
        GetResponse response = client.prepareGet("bank", "_doc", "722").execute().actionGet();
        System.out.println(response.getSourceAsString());
    }

    @Test
    public void testInsert() {
        try {
            XContentBuilder blog = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("id", "2")
                    .field("title", "Java设计模式值单例模式")
                    .field("content", "为系统提供能够创建一个单一的实例对象方法")
                    .field("postdate", "2019-05-24")
                    .field("url", "https://www.baidu.com")
                    .endObject();
            IndexResponse response = client.prepareIndex("index", "blog", "101").setSource(blog).execute().actionGet();
            System.out.println(response.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDelete() {
        try {
            DeleteResponse deleteResponse = client.prepareDelete("index", "blog", "101").execute().actionGet();
            System.out.println(deleteResponse.status());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testUpdate() {
        try {
            XContentBuilder blogUpdate = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("title", "Java设计模式值单例模式ssss")
                    .endObject();
            UpdateResponse updateResponse = client.prepareUpdate("index", "blog", "100").setDoc(blogUpdate).execute().actionGet();
            System.out.println(updateResponse.status());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testUpsert() {
        try {
            IndexRequest indexRequest = new IndexRequest("index", "blog", "8").source(
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .field("id", "8")
                            .field("title", "Java设计模式值工厂模式")
                            .field("content", "为系统提供能够创建一个单一的实例对象方法")
                            .field("postdate", "2019-05-24")
                            .field("url", "https://www.baidu.com")
                            .endObject()
            );

            UpdateRequest updateRequest = new UpdateRequest("index", "blog", "8").doc(
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .field("title", "Java设计模式值工厂模式ssss")
                            .endObject()
            ).upsert(indexRequest);

            UpdateResponse updateResponse = client.update(updateRequest).get();
            System.out.println(updateResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMatchAll() {
        try {
            SearchResponse searchResponse = client.prepareSearch("index")
                    .setQuery(QueryBuilders.matchAllQuery())
                    .execute().actionGet();
            searchResponse.getHits().forEach(h -> {
                System.out.println(h.getSourceAsString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMatch() {
        try {
            SearchResponse searchResponse = client.prepareSearch("index")
                    .setQuery(QueryBuilders.matchQuery("id", "8"))
                    .execute().actionGet();
            searchResponse.getHits().forEach(h -> {
                System.out.println(h.getSourceAsString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从title,content字段中查找‘单例’字符
     */
    @Test
    public void testMultiMatch() {
        try {
            SearchResponse searchResponse = client.prepareSearch("index")
                    .setQuery(QueryBuilders.multiMatchQuery("单例", "title", "content"))
                    .execute().actionGet();
            searchResponse.getHits().forEach(h -> {
                System.out.println(h.getSourceAsString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTerm() {
        try {
            SearchResponse searchResponse = client.prepareSearch("index")
                    .setQuery(QueryBuilders.termQuery("title", "java"))
                    .execute().actionGet();
            searchResponse.getHits().forEach(h -> {
                System.out.println(h.getSourceAsString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTerms() {
        try {
            SearchResponse searchResponse = client.prepareSearch("index")
                    .setQuery(QueryBuilders.termsQuery("content", "系统", "方法"))
                    .execute().actionGet();
            searchResponse.getHits().forEach(h -> {
                System.out.println(h.getSourceAsString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRange() {
        try {
            //range
            //RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").from(20).to(30);

            //firstname 小写
            //PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("firstname", "antonia");

            //firstname 小写  匹配符号匹配
            //WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("firstname", "an*");

            //模糊查询
            //FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("address", "Avenue");

            //type query
            //TypeQueryBuilder typeQueryBuilder = QueryBuilders.typeQuery("_doc");

            //id查询
            IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery().addIds("25", "26", "30", "100");

            SearchResponse searchResponse = client.prepareSearch("bank")
                    .setQuery(idsQueryBuilder)
                    .setSize(100)
                    .execute().actionGet();
            searchResponse.getHits().forEach(h -> {
                System.out.println(h.getSourceAsString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAggs() {
        try {
            MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("maxAge").field("age");
            SearchResponse response = client.prepareSearch("bank")
                    .addAggregation(maxAggregationBuilder)
                    .execute().actionGet();
            Max maxAge = response.getAggregations().get("maxAge");
            System.out.println(maxAge.getValue());


            AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avgAge").field("age");
            SearchResponse response2 = client.prepareSearch("bank")
                    .addAggregation(avgAggregationBuilder)
                    .execute().actionGet();
            Avg avgAge = response2.getAggregations().get("avgAge");
            System.out.println(avgAge.getValue());

            CardinalityAggregationBuilder cardinalityAggregationBuilder = AggregationBuilders.cardinality("ca").field("age");
            SearchResponse searchResponse = client.prepareSearch("bank")
                    .addAggregation(cardinalityAggregationBuilder)
                    .execute().actionGet();

            Cardinality cardinality = searchResponse.getAggregations().get("ca");
            System.out.println(cardinality.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryString() {
        try {

            //特定的字段  的值是否满足
            //CommonTermsQueryBuilder commonTermsQueryBuilder = QueryBuilders.commonTermsQuery("firstname", "blake");

            //2个条件必须都满足的记录  全文查询
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery("-hund +blake");

            //满足其中任何一个
            //QueryBuilders.simpleQueryStringQuery("-hund +blake")
            SearchResponse searchResponse = client.prepareSearch("bank")
                    .setQuery(queryStringQueryBuilder)
                    .execute().actionGet();
            searchResponse.getHits().forEach(h -> {
                System.out.println(h.getSourceAsString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCompoundQuery() {
        try {

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("gender", "F"))
                    .mustNot(QueryBuilders.matchQuery("state", "KY"))
                    .should(QueryBuilders.matchQuery("address", "Street"))
                    .filter(QueryBuilders.rangeQuery("age").gte(20).lte(40));
            SearchResponse searchResponse = client.prepareSearch("bank")
                    .setQuery(boolQueryBuilder)
                    .execute().actionGet();
            searchResponse.getHits().forEach(h -> {
                System.out.println(h.getSourceAsString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstantQuery() {
        try {

            ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("firstname", "blake"));
            SearchResponse searchResponse = client.prepareSearch("bank")
                    .setQuery(constantScoreQueryBuilder)
                    .execute().actionGet();
            searchResponse.getHits().forEach(h -> {
                System.out.println(h.getSourceAsString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTermQuery() {
        try {
            TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("terms").field("age");
            SearchResponse searchResponse = client.prepareSearch("bank")
                    .addAggregation(termsAggregationBuilder)
                    .execute().actionGet();
            Terms terms = searchResponse.getAggregations().get("terms");
            terms.getBuckets().stream().forEach(t -> {
                System.out.println(t.getKey() + " " + t.getDocCount());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFilterQuery() {
        try {
            FilterAggregationBuilder filterAggregationBuilder = AggregationBuilders.filter("filter", QueryBuilders.termQuery("age", 36));
            SearchResponse searchResponse = client.prepareSearch("bank")
                    .addAggregation(filterAggregationBuilder)
                    .execute().actionGet();
            Filter filter = searchResponse.getAggregations().get("filter");
            System.out.println(filter.getDocCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRangeQuery() {
        try {
            RangeAggregationBuilder rangeAggregationBuilder = AggregationBuilders.range("range")
                    .field("age")
                    .addUnboundedTo(50)
                    .addRange(30, 50)
                    .addUnboundedFrom(25);

            SearchResponse searchResponse = client.prepareSearch("bank")
                    .addAggregation(rangeAggregationBuilder)
                    .execute().actionGet();
            Range range = searchResponse.getAggregations().get("range");
            range.getBuckets().stream().forEach(r -> {
                System.out.println(r.getKey() + "  " + r.getDocCount());

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMissingQuery() {
        try {
            MissingAggregationBuilder missingAggregationBuilder = AggregationBuilders.missing("missing")
                    .field("age");
            SearchResponse searchResponse = client.prepareSearch("bank")
                    .addAggregation(missingAggregationBuilder)
                    .execute().actionGet();
            Missing missing = searchResponse.getAggregations().get("missing");
            System.out.println(missing.getDocCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testHealth() {
        try {
            ClusterHealthResponse healthResponse = client.admin().cluster().prepareHealth().get();
            System.out.println(healthResponse.getClusterName());
            System.out.println(healthResponse.getNumberOfDataNodes());
            System.out.println(healthResponse.getNumberOfNodes());
            for (String key : healthResponse.getIndices().keySet()){
                ClusterIndexHealth h = healthResponse.getIndices().get(key);
                System.out.println(key +" "+ h.getNumberOfShards() + " "+ h.getNumberOfReplicas() + "  "+ h.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
