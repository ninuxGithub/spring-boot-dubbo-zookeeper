package com.example.provider.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.provider.entity.Person;
import com.example.provider.repository.PersonRepository;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 *
 * 参考：https://blog.csdn.net/kingice1014/article/details/72899776
 */

@RestController
@RequestMapping(value = "/es/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/init")
    public void initData() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("a.json");
        BufferedInputStream bis = new BufferedInputStream(in);
        byte[] bytes = new byte[1024];
        int len = 0;
        StringBuffer buffer = new StringBuffer();
        while ((len = bis.read(bytes)) != -1) {
            buffer.append(new String(bytes, 0, len));
        }
        String json = buffer.toString();
        logger.info(json);

        JSONObject jsonObject = JSONObject.parseObject(json);
        Object hits = jsonObject.get("hits");
        JSONArray jarr = JSONArray.parseArray(hits.toString());
        for (int i = 0; i < jarr.size(); i++) {
            JSONObject dataJson = JSONObject.parseObject(jarr.get(i).toString());
            String _id = dataJson.get("_id").toString();
            String _source = dataJson.get("_source").toString();
            JSONObject sourceObj = JSONObject.parseObject(_source);
            int account_number = Integer.valueOf(sourceObj.get("account_number").toString());
            long balance = Long.valueOf(sourceObj.get("balance").toString());
            String firstname = sourceObj.get("firstname").toString();
            String lastname = sourceObj.get("lastname").toString();
            int age = Integer.valueOf(sourceObj.get("age").toString());
            String gender = sourceObj.get("gender").toString();
            String address = sourceObj.get("address").toString();
            String employer = sourceObj.get("employer").toString();
            String email = sourceObj.get("email").toString();
            String city = sourceObj.get("city").toString();
            String state = sourceObj.get("state").toString();
            Person person = new Person(Long.valueOf(_id), account_number, balance, firstname, lastname, age, gender, address, employer, email, city, state);
            personRepository.save(person);
        }
    }

    @RequestMapping("/matchQuery")
    public List<Person> matchQuery() throws IOException {
        //id 排序； firstname 符合 Blake 的所有的记录
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("age", 30);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(matchQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }

    /**
     * terms 测试  term 的value 如果为中文ok没问题， 如果是英文  首字母大写  需要通过小写的单词 去匹配
     *
     * 例如 写成这样 QueryBuilders.termsQuery("firstname", "Vera", "Kari", "Blake");是匹配不到记录的
     *
     * 参考了：https://www.cnblogs.com/shaosks/p/7813729.html
     *
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/termsQuery")
    public List<Person> termsQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("firstname", "vera", "kari", "blake");
        //term 需要指定为字符的小写形式
        //TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("firstname", "Vera", "Kari", "Blake");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(termsQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }

    @RequestMapping("/termQuery")
    public List<Person> termQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("firstname", "vera");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(termQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }


    /**
     * 在id , age 中找包含44 的记录
     * @return
     * @throws IOException
     */
    @RequestMapping("/multimatchQuery")
    public List<Person> multimatchQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("44", "id", "age");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }

    /**
     * <pre>
    GET /person/_search
    {
        "query": {
            "bool": {
                "must": [
                {
                    "match": {
                    "firstname": "Effie"
                }
                }
          ],
                "must_not": [
                {
                    "match": {
                    "gender": "M"
                }
                }
          ],
                "should": [
                {"match": {
                    "balance": "3607"
                }}
          ]

            }
        }
    }
     *</pre>
     *
     *
     * multimatch 用于判断多个字段 进行条件的筛选
    */
    @RequestMapping("/boolQuery")
    public List<Person> boolQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("firstname", "Effie"))
                .mustNot(QueryBuilders.matchQuery("gender", "M"))
                .should(QueryBuilders.matchQuery("balance", "3607"));

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }


//    GET /person/_search
//    {
//        "query": {
//        "ids": {
//            "values": [
//            "25",
//                    "44",
//                    "126"
//      ]
//        }
//    }
//    }
    /**
     * 查询id : ... id 可变参数
     *
     * idsQuery的type 传递"id" 查询不到记录？
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/idsQuery")
    public List<Person> idsQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        //传递 type对应的值
        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery().addIds("25", "44", "126");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(idsQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }


    /**

     <pre>

        GET /person/_search
        {
            "query": {
            "constant_score": {
                "filter": {
                    "term": {
                        "gender": "m"
                    }
                },
                "boost": 1.2
            }
        }
        }
     </pre>
     */
    @RequestMapping("/constantScoreQuery")
    public List<Person> constantScoreQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("gender", "m")).boost(2.0f);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(constantScoreQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }


//    GET /person/_search
//    {
//        "query": {
//        "dis_max": {
//            "tie_breaker": 0.7,
//                    "boost": 1.2,
//                    "queries": [
//            {
//                "term": {
//                "firstname": {
//                    "value": "Effie"
//                }
//            }
//            },
//            {
//                "match": {
//                "gender": "m"
//            }
//            }
//      ]
//        }
//    }
//    }
    /***
     *相比使用bool查询，我们可以使用dis_max查询(Disjuction Max Query)。
     * Disjuction的意思"OR"(而Conjunction的意思是"AND")，
     * 因此Disjuction Max Query的意思就是返回匹配了任何查询的文档，并且分值是产生了最佳匹配的查询所对应的分值：
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/disMaxQuery")
    public List<Person> disMaxQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);

        DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("firstname", "Effie"))
                .add(QueryBuilders.matchQuery("gender", "m"))
                .boost(1.2f)
                .tieBreaker(0.7f);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(disMaxQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }

    /**
     * 模糊匹配
     * @return
     * @throws IOException
     */
    @RequestMapping("/fuzzyQuery")
    public List<Person> fuzzyQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);

        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("firstname", "effie");

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(fuzzyQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }


    //TODO
    @RequestMapping("/moreLikeThisQuery")
    public List<Person> moreLikeThisQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        //QueryBuilders.moreLikeThisQuery({"firstname"},{"lastname"},new ,)
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(null)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }



    @RequestMapping("/prefixQuery")
    public List<Person> prefixQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        //匹配姓名  以小写字母开头
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("firstname", "b");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(prefixQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }


    @RequestMapping("/rangeQuery")
    public List<Person> rangeQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").from(20).to(30).includeLower(true).includeUpper(false);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(rangeQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }


    @RequestMapping("/spanFirstQuery")
    public List<Person> spanFirstQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        //address: "398 Dearborn Court",  有3个term  查询court 结束的位置为 3
        SpanFirstQueryBuilder spanFirstQueryBuilder = QueryBuilders.spanFirstQuery(QueryBuilders.spanTermQuery("address", "court"), 3);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(spanFirstQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }


    @RequestMapping("/testQuery")
    public List<Person> paginationQuertestQueryy() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("gender", "f"))
                .should(QueryBuilders.termQuery("address", "court"))
                .should(QueryBuilders.termQuery("state","md"))
                .filter(QueryBuilders.rangeQuery("age").gte(30))
                .filter(QueryBuilders.rangeQuery("balance").gte(2726));

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        List<Person> content = personRepository.search(nativeSearchQuery).getContent();
        return content;
    }


    /**
     *
     *
    GET /person/_search
    {
        "query": {
            " function_score": {
                "query": {
                    "match": {
                        "gender": "F"
                    }
                },
                "field_value_factor": {
                    "field": "balance",
                            "modifier": "log1p",
                            "factor": 0.5
                }
          , "boost_mode": "sum"
            }
        }
    }
     */
    @RequestMapping("/scoreFunctionQuery")
    public List<Person> scoreFunctionQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);

        //bool query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("gender", "F"));

        //feild_value_factor
        FieldValueFactorFunctionBuilder functionBuilder = ScoreFunctionBuilders.fieldValueFactorFunction("balance")
                .modifier(FieldValueFactorFunction.Modifier.LOG1P)
                .factor(0.5f);

        //构建builder
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder, functionBuilder).boostMode(CombineFunction.SUM);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(functionScoreQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        Page<Person> page = personRepository.search(nativeSearchQuery);
        logger.info("page: "+page.toString());
        List<Person> content = page.getContent();
        return content;
    }


    /**

     GET /person/_search
     {
         "query": {
             "function_score": {
                 "query": {
                     "match": {
                         "gender": "F"
                    }
             },
             "gauss": {
                 "balance": {
                     "origin": "43951",
                     "scale": "100",
                     "offset": "10"
                 }
             },
             "boost_mode": "sum"
             }
         }
     }
     */
    @RequestMapping("/guessQuery")
    public List<Person> guessQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);

        //bool query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("gender", "F"));
        GaussDecayFunctionBuilder gaussDecayFunctionBuilder = ScoreFunctionBuilders.gaussDecayFunction("balance", "43951", "100", "10");

        //构建builder
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder, gaussDecayFunctionBuilder).boostMode(CombineFunction.SUM);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(functionScoreQueryBuilder)
                .withPageable(pageRequest)
                //.withSort(idSortBuilder)
                .build();

        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        Page<Person> page = personRepository.search(nativeSearchQuery);
        logger.info("page: "+page.toString());
        List<Person> content = page.getContent();
        return content;
    }


    /**   function_score weight query
    GET /person/_search
    {
        "query": {
            "function_score": {
                    "query": {
                        "bool": {
                            "must": [
                            {
                                "match": {
                                "gender": "F"
                            }
                            },{
                                "range": {
                                    "age": {
                                        "gte": 25,
                                                "lte": 30
                                    }
                                }
                            }
                  ]
                        }
                    },
                    "functions": [
                    {
                        "weight": 2
                    }
                ]
            }
        }
    }
     **/
    @RequestMapping("/weighQuery")
    public List<Person> weighQuery() throws IOException {
        PageRequest pageRequest = PageRequest.of(0, 10);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);

        //bool query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("gender", "F"))
                .must(QueryBuilders.rangeQuery("age").gte(25).lte(30));

        //weight buil
        WeightBuilder weightBuilder = ScoreFunctionBuilders.weightFactorFunction(2.0f);

        //构建builder
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder, weightBuilder).boostMode(CombineFunction.SUM);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(functionScoreQueryBuilder)
                .withPageable(pageRequest)
                //.withSort(idSortBuilder)
                .build();

        String query = nativeSearchQuery.getQuery().toString();
        logger.info(query);
        Page<Person> page = personRepository.search(nativeSearchQuery);
        logger.info("page: "+page.toString());
        List<Person> content = page.getContent();
        return content;
    }
}
