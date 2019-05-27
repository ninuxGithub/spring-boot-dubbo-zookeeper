
## kibana 启动故障
    Another Kibana instance appears to be migrating the index    
    
    curl -XDELETE 'http://localhost:9200/.kibana_1'  --header "content-type: application/JSON" -u elastic -p
    在linux 里面执行这个命令将.kibana_1文件删除在启动kibana就好了
    
### 启动header
    dos切换到elasticsearch-header目录 grunt server 启动header 
    
    
    
#-------------查询--------------------
#删除index
DELETE /index_china/

#建立index
PUT /index_china/

#ik_max_work 会将文本做最细颗粒度的拆分
#ik_smart 粗颗粒度的拆分

#建立mappering
POST /index_china/fulltext/_mapping
{
  "properties": {
    "content": {
      "type": "text",
      "analyzer": "ik_max_word",
      "search_analyzer": "ik_max_word"
    }
  }
}

#插入数据
POST /index_china/fulltext
{
  "content":"中国是世界上人口最多的国家",
  "title":"中国",
  "tags":["中国","人口"]
  
}


#批量执行
POST /_bulk
{"create":{"_index":"index_china","_type":"fulltext","_id":1}}
{ "title": "周星驰最新电影" }
{"create":{"_index":"index_china","_type":"fulltext","_id":2}}
{ "title": "周星驰最好看的新电影" }
{"create":{"_index":"index_china","_type":"fulltext","_id":3}}
{ "title": "周星驰最新电影，最好，新电影" }
{"create":{"_index":"index_china","_type":"fulltext","_id":4}}
{ "title": "最最最最好的新新新新电影" }
{"create":{"_index":"index_china","_type":"fulltext","_id":5}}
{ "title": "I'm not happy about the foxes" }



GET /index_china/fulltext/_search
{
  "query": {
    "match": {
      "content": "中国"
    }
  }
}
GET /index_china/fulltext/_search
{
  "query": {
    "match": {
      "title": "周星驰"
    }
  }
}


#查询person id=25的记录
GET /person/_doc/25


#ik_max_work 颗粒度更细
GET /_analyze?pretty
{
  "analyzer": "ik_max_word",
  "text": "中华人民共和国国歌"
}


#颗粒度粗糙点
GET /_analyze?pretty
{
  "analyzer": "ik_smart",
  "text": "中华人民共和国国歌"
}


#删除索引
DELETE /ott_test


#建立索引
PUT /ott_test
{
  "mappings": {
    "ott_type": {
      "properties": {
        "title": {
          "type": "text",
          "index": true,
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "date": {
          "type": "date"
        },
        "keyword": {
          "type": "keyword"
        },
        "source": {
          "type": "keyword"
        },
        "link": {
          "type": "keyword"
        }
      }
    }
  }
}



POST /ott_test/ott_type
{
  "title":"微博新规惹争议：用户原创内容版权归属于微博？",
  "link":"http://www.yidianzixun.com/article/0HHoxgVq",
  "date":"2017-09-17",
  "source":"虎嗅网",
  "keyword":"内容"
}

GET /ott_test/_analyze
{
  "field": "title",
  "text": ["内容"]
}


GET /ott_test/ott_type/_search
{
  "query": {
    "match": {
      "title": "内容"
    }
  }
}

#查询title , date 
GET /ott_test/ott_type/_search
{
  "query": {
    "match_all": {
      
    }
  },
  "_source": ["title","date"]
}   


```text
###################mapping 建立的数据类型######################
#字符型： text, keyword (string es5弃用)                      #
#数字型：long,integer,short,byte,double,float                 #
#日期：  date                                                 #
#bool: boolean                                                #
#binary : binary                                              #
#复杂类型： object, nested                                    #
#geo类型： point, geo-shape                                   #
#专业类型： ip, competion                                     #
#                                                             #
#                                                             #
#                                                             #
#属性           描述                          适合类型        #
#store          yes:存储  no:不存储           all             #
#index          yes:分析  no:不分析 默认yes   string          #
#null_value     standard/whitespace/simple/english all        #
#include_in_all 让每个字段被搜索到            all             #
#format         日期格式                      date            #
###############################################################
```


#创建index
PUT /schools/

POST /_bulk
{"index":{"_index":"schools","_type":"school","_id":"1"}}
{"name":"Central School","description":"CBSE Affiliation","street":"Nagan","city":"paprola","state":"HP","zip":"176115","location":[31.8955385,76.8380405],"fees":2000,"tags":["Senior Secondary","beautiful campus"],"rating":"3.5"}
{"index":{"_index":"schools","_type":"school","_id":"2"}}
{"name":"Saint Paul School","description":"ICSE Afiliation","street":"Dawarka","city":"Delhi","state":"Delhi","zip":"110075","location":[28.5733056,77.0122136],"fees":5000,"tags":["Good Faculty","Great Sports"],"rating":"4.5"}
{"index":{"_index":"schools","_type":"school","_id":"3"}}
{"name":"Crescent School","description":"State Board Affiliation","street":"Tonk Road","city":"Jaipur","state":"RJ","zip":"176114","location":[26.8535922,75.7923988],"fees":2500,"tags":["Well equipped labs"],"rating":"4.5"}

PUT /schools_gov

POST /_bulk
{"index":{"_index":"schools_gov","_type":"school","_id":"1"}}
{"name":"Model School","description":"CBSE Affiliation","street":"silk city","city":"Hyderabad","state":"AP","zip":"500030","location":[17.3903703,78.4752129],"fees":200,"tags":["Senior Secondary","beautiful campus"],"rating":"3"}
{"index":{"_index":"schools_gov","_type":"school","_id":"2"}}
{"name":"Government School","description":"State Board Affiliation","street":"Hinjewadi","city":"Pune","state":"MH","zip":"411057","location":[18.599752,73.6821995],"fees":500,"tags":["Great Sports"],"rating":"4"}


POST /school*/_search
{
  "query": {
    "query_string": {
      "query": "CBSE"
    }
  }
}

POST /school*,book_shops/_search?ignore_unavailable=true


POST /schools/_search?pretty=true
{
  "query": {
    "match_all": {}
  }
}

GET /schools/_mapping


#将结果混合起来
POST /_mget
{
  "docs": [
    {
      "_index": "schools",
      "_type": "school",
      "_id": "1"
    },
    {
      "_index": "schools_gov",
      "_type": "school",
      "_id": "2"
    }
  ]
}


#name 包含central
GET /schools/_search?q=name:central
#tags 包含sports
GET /schools/_search?q=tags:sports

GET /_all/_settings


PUT /lib/

GET /lib/_settings

#有id的插入方式
PUT /lib/user/1
{
  "name":"java",
  "age":19,
  "address":"sh"
}

PUT /lib/user/2
{
  "name":"java",
  "age":22,
  "address":"sh2222"
}

#无id  自动生成id
POST /lib/user/
{
  "name":"doc",
  "age":20,
  "address":"bk"
}

GET /lib/user/1


#_source 的选择过滤
GET /lib/user/1?_source=age,address


#更新的方法
PUT /lib/user/1
{
  "name":"java-update",
  "age":88,
  "address":"sh"
}


#修改 的时候要通过doc属性设置
POST /lib/user/1/_update
{
  "doc": {
    "name": "aa",
    "age": 25,
    "address": "sh"
  }
}


#查询
POST /lib/user/_search
{
  "query": {"match_all": {}}
}


#删除
DELETE /lib/user/2



#multi get 一次获取多个索引的查询的结果===============
GET /_mget
{
  "docs": [
    {
      "_index": "lib",
      "_type": "user",
      "_id": 1
    },
    {
      "_index": "lib",
      "_type": "user",
      "_id": "Ofew3moBdB5fAwve0OAo"
    }
  ]
}


#_source 过滤
GET /_mget
{
  "docs": [
    {
      "_index": "lib",
      "_type": "user",
      "_id": 1,
      "_source":["name","address"]
    },
    {
      "_index": "lib",
      "_type": "user",
      "_id": "Ofew3moBdB5fAwve0OAo",
      "_source":["age","address"]
    }
  ]
}


#简单的写法1
GET /lib/user/_mget
{
  "docs": [
    {
      "_id": 1,
      "_source": [
        "name",
        "address"
      ]
    },
    {
      "_id": "Ofew3moBdB5fAwve0OAo",
      "_source": [
        "age",
        "address"
      ]
    }
  ]
}

#简单的写法2
GET /lib/user/_mget
{
  "ids":["1","Ofew3moBdB5fAwve0OAo"]
}


#version 的使用==========================================
# version 乐观锁 如果版本小于记录的实际版本 报错
#如果采用了version_type=external 那么 version 需要高于实际的版本
#version_type=external 用于外部数据导入到es (时间戳的版本)
PUT /lib/user/3?version=8&version_type=external
{
  "name":"versionTest5"
}  


#mapping  ===========================
#

PUT /lib/user/3
{
  "name":"versionTest5",
  "sex":"male"
}  

DELETE /testmapping
PUT /testmapping
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  },
  "mappings": {
    "user": {
      "dynamic": "strict",
      "properties": {
        "name": {
          "type": "text"
        },
        "address": {
          "type": "object",
          "dynamic": true
        }
      }
    }
  }
}


PUT /testmapping/user/1
{
  "name": "aaa",
  "address": {
    "province": "beijing",
    "city": "beijing"
  }
}

GET /testmapping/user/1


#age 属性没有定义在mapping里面 添加不了
PUT /testmapping/user/1
{
  "name": "llll",
  "age":18,
  "address": {
    "province": "bj",
    "city": "bj"
  }
}



PUT /my_index
{
  "mappings": {
    "my_type": {
      "dynamic_templates": [
        {
          "en": {
            "match": "*_en",
            "match_mapping_type": "string",
            "mapping": {
              "type": "text",
              "analyzer": "english"
            }
          }
        }
      ]
    }
  }
}


PUT /my_index/my_type/1
{
  "title_en":"this is a dog"
}

PUT /my_index/my_type/2
{
  "title":"this is a dog"
}

#english 分词器查询不到is
POST /my_index/my_type/_search
{
  "query": {
    "match": {
      "title_en": "is"
    }
  }
}
#standard 分词器
POST /my_index/my_type/_search
{
  "query": {
    "match": {
      "title": "is"
    }
  }
}





#term ==========================================
POST /schools/_search
{
  "query": {
    "match_all": {}
  }
}

#term
POST /schools/school/_search
{
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "fees": 2500
          }
        }
      ]
    }
  }
}

#terms
POST /schools/school/_search
{
  "query": {
    "bool": {
      "filter": [
        {
          "terms": {
            "fees": [2500,5000]
          }
        }
      ]
    }
  }
}

#查看mapping
GET /schools/school/_mapping


#mapping 显示 name 是text类型 是进行分词的 name 内存转存小写
POST /schools/school/_search
{
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "name": "school"
          }
        }
      ]
    }
  }
}


#bool 查询============must , should , must not=========================

POST /schools/school/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "fees": {
              "value": 5000
            }
          }
        }
      ]
    }
  }
}

POST /schools/school/_search
{
  "query": {
    "bool": {
      "must_not": [
        {
          "term": {
            "fees": {
              "value": 5000
            }
          }
        }
      ]
    }
  }
}

POST /schools/school/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "term": {"fees": {"value": 5000}}
        },
        {
          "term": {"fees": {"value": 2500}}
        }
      ]
    }
  }
}


#range 范围=============================gt>  lt <  gte >=  lte <=
POST /schools/school/_search
{
  "query": {
    "bool": {
      "filter": {
        "range": {
          "fees": {
            "gte": 2500,
            "lte": 5000
          }
        }
      }
    }
  }
}

#存在name属性的
POST /schools/school/_search
{
  "query": {
    "bool": {
      "filter": {
        "exists": {
          "field": "name"
        }
      }
    }
  }
}

#复合查询===============================================
#fee 求和  size:0 只查看求总的结果
POST /schools/school/_search
{
  "size": 0, 
  "aggs": {
    "fees_sum": {
      "sum": {
        "field": "fees"
      }
    }
  }
}



POST /schools/school/_search
{
  "size": 0, 
  "aggs": {
    "fees_sum": {
      "cardinality": {
        "field": "fees"
      }
    }
  }
}

#分组
POST /schools/school/_search
{
  "size": 0, 
  "aggs": {
    "fees_group": {
      "terms": {
        "field": "fees"
      }
    }
  }
}


# name = schoool 的根据fees分组， 求fees 的平均值 并且根据平均值进行升序 
POST /schools/school/_search
{
  "size": 0,
  "query": {
    "match": {
      "name": "school"
    }
  },
  "aggs": {
    "state_group": {
      "terms": {
        "field": "fees",
        "order": {
          "fee_avg": "asc"
        }
      },
      "aggs": {
        "fee_avg": {
          "avg": {
            "field": "fees"
          }
        }
      }
    }
  }
}



#es 原


GET /schools/_settings

GET /schools

GET /_cat/health

GET /schools/_mapping

GET /_cluster/health


GET /_cat/master

POST /person/_search
{
  "size": 1,
  "sort": [
    {
      "id": {
        "order": "asc"
      }
    }
  ],
  "aggs": {
    "age_range": {
      "range": {
        "field": "age",
        "ranges": [
          {
            "from": 30,
            "to": 50
          }
        ]
      },
      "aggs": {
        "balance_agg": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
}


POST /person/_search
{
  "size": 2, 
  "query": {
    "match": {
      "gender": "f"
    }
  }
}


#firstname or lastname中包含effie字符串的记录
POST /person/_search
{
  "query": {
    "multi_match": {
      "query": "effie",
      "fields": ["firstname","lastname"]
    }
  }
}

#query_string
POST /person/_search
{
  "query": {
    "query_string": {
      "default_field": "firstname",
      "query": "effie"
    }
  }
}

#term
POST /person/_search
{
  "query": {
    "term": {
      "firstname": {
        "value": "effie"
      }
    }
  }
}


#range
POST /person/_search
{
  "query": {
    "range": {
      "age": {
        "gte": 10,
        "lte": 20
      }
    }
  },
  "_source": ["age", "id", "gender"]
}


# bool
POST /person/_search
{
  "query": {
    "bool": {
      "filter": {
        "range": {
          "age": {
            "gte": 10,
            "lte": 50
          }
        }
      },
      "must": [
        {"match": {
          "id": 172
        }}
      ]
    }
  },
  "_source": ["age", "id", "gender"]
}



GET /_cat/health?v

GET /_cat/health?v

GET /_cat/indices?v

GET /bank/_search
{"query":{"match":{"address":"mill"}}}

GET /bank/_search
{"query":{"match":{"address":"mill lane"}}}

GET /bank/_search
{"query":{"match_phrase":{"address":"mill lane"}}}


#equal to match_phrase
GET /bank/_search
{"query":{"bool":{"must":[{"match":{"address":"mill"}},{"match":{"address":"lane"}}]}}}


#equal to match "mill lane"
GET /bank/_search
{"query":{"bool":{"should":[{"match":{"address":"mill"}},{"match":{"address":"lane"}}]}}}


GET /bank/_search
{"query":{"bool":{"must":[{"match":{"age":"40"}}],"must_not":[{"match":{"state":"ID"}}]}}}


GET /bank/_search
{"query":{"bool":{"must":[{"match_all":{}}],"filter":{"range":{"balance":{"gte":20000,"lte":30000}}}}}}

GET /bank/_search
{"size":0,"aggs":{"group_by_state":{"terms":{"field":"state.keyword","size":20,"order":{"avg_balance":"asc"}},"aggs":{"avg_balance":{"avg":{"field":"balance"}}}}}}


#根据年龄分段  然后再根据状态分类 分段的平均工资
GET /bank/_search
{"size":0,"aggs":{"group_by_age":{"range":{"field":"age","ranges":[{"from":20,"to":30},{"from":30,"to":40},{"from":40,"to":50}]},"aggs":{"group_by_state":{"terms":{"field":"state.keyword","size":20,"order":{"avg_balance":"asc"}},"aggs":{"avg_balance":{"avg":{"field":"balance"}}}}}}}}





#copy_to 提升查询的性能（文本类型的属性）
GET /schools,schools_*/_search

GET /bank/_search?q=effie



GET /myindex/_search


PUT /myindex
PUT /myindex/artical/_mapping
{
  "properties": {
    "post_date": {
      "type": "date"
    },
    "title": {
      "type": "text",
      "copy_to": "fullcontents"
    },
    "content": {
      "type": "text",
      "copy_to": "fullcontents"
    },
    "auth_id": {
      "type": "integer"
    }
  }
}

PUT /myindex/artical/1
{
  "post_date":"2018-01-01",
  "title":"java",
  "content":"java is the best language",
  "auth_id":"1"
}


PUT /myindex/artical/2
{
  "post_date":"2018-01-02",
  "title":"html",
  "content":"html is the best language",
  "auth_id":"2"
}

PUT /myindex/artical/3
{
  "post_date":"2018-01-02",
  "title":"php",
  "content":"php is the best language",
  "auth_id":"3"
}


GET /myindex/artical/_search?q=fullcontents:html,java,php
GET /myindex/artical/1






#字符串排序    title 无法进行排序  text 类型  只有数字类型可以排序
GET /myindex/_search
{
  "query": {
    "match_all": {
      
    }
  },
  "sort": [
    {
      "content.raw": {
        "order": "desc"
      }
    }
  ]
}

DELETE /myindex
PUT /myindex
PUT /myindex/artical/_mapping
{
  "properties": {
    "post_date": {
      "type": "date"
    },
    "title": {
      "type": "text"
    },
    "content": {
      "type": "text",
      "fields": {
        "raw": {
          "type": "keyword"
        }
      },
      "fielddata": true
    },
    "auth_id": {
      "type": "integer"
    }
  }
}

PUT /myindex/artical/1
{
  "post_date":"2018-01-01",
  "title":"java",
  "content":"java is the best language",
  "auth_id":"1"
}


PUT /myindex/artical/2
{
  "post_date":"2018-01-02",
  "title":"html",
  "content":"html is the best language",
  "auth_id":"2"
}

PUT /myindex/artical/3
{
  "post_date":"2018-01-02",
  "title":"php",
  "content":"php is the best language",
  "auth_id":"3"
}
PUT /myindex/artical/4
{
  "post_date":"2018-01-02",
  "title":"php",
  "content":"aa is the best language",
  "auth_id":"4"
}




#得分的算法  tf/idf   term frequency  &  inverse document frequency
POST /bank/_search?explain=true
{
  "query": {
    "match": {
      "firstname": "effie"
    }
  }
}


# id 120 firstname 命中后 _explain解析
GET /bank/_doc/120/_explain
{
  "query": {
    "match": {
      "firstname": "Browning"
    }
  }
}


GET /bank/_doc/120

POST /bank/_search
{
  "query": {
    "match": {
      "firstname": "effie"
    }
  }
}




#doc_value的使用
DELETE /myindex


PUT /myindex

PUT /myindex/artical/_mapping
{
  "properties": {
    "post_date": {
      "type": "date"
    },
    "title": {
      "type": "text"
    },
    "content": {
      "type": "text",
      "fields": {
        "raw": {
          "type": "keyword"
        }
      },
      "fielddata": true
    },
    "auth_id": {
      "type": "integer",
      "doc_values":false
      
    }
  }
}


PUT /myindex/artical/1
{
  "post_date":"2018-01-01",
  "title":"java",
  "content":"java is the best language",
  "auth_id":"1"
}


PUT /myindex/artical/2
{
  "post_date":"2018-01-02",
  "title":"html",
  "content":"html is the best language",
  "auth_id":"2"
}

PUT /myindex/artical/3
{
  "post_date":"2018-01-02",
  "title":"php",
  "content":"php is the best language",
  "auth_id":"3"
}
PUT /myindex/artical/4
{
  "post_date":"2018-01-02",
  "title":"php",
  "content":"aa is the best language",
  "auth_id":"4"
}


#关闭doc_values 后  auth_id 排序失败
POST /myindex/artical/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "auth_id": {
        "order": "desc"
      }
    }
  ]
}

#scroll 的使用  首先建立一个滚动查询  获取scroll_id , 第二次携带scroll_id 去查询就ok了
GET /bank/_doc/_search?scroll=10m
{
  "query": {
    "match_all": {}
  },
  "sort": [
    "_doc"
  ],
  "size": 10
}


GET /_search/scroll
{
  "scroll":"10m",
  "scroll_id":"DnF1ZXJ5VGhlbkZldGNoBQAAAAAAAFJRFmNsOHY0QTFWVEVxOGxPaE5fTVlabFEAAAAAAABSTxZjbDh2NEExVlRFcThsT2hOX01ZWmxRAAAAAAAAUk4WY2w4djRBMVZURXE4bE9oTl9NWVpsUQAAAAAAAFJSFmNsOHY0QTFWVEVxOGxPaE5fTVlabFEAAAAAAABSUBZjbDh2NEExVlRFcThsT2hOX01ZWmxR"
}


#分页
GET /bank/_search
{
  "from": 0,
  "size": 20, 
  "query": {
    "match": {
      "state" : "PA"
    }
  }
}


PUT /index
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 0
  },
  "mappings": {
    "blog": {
      "properties": {
        "id": {
          "type": "long"
        },
        "title": {
          "type": "text",
          "analyzer": "ik_max_word"
        },
        "content": {
          "type": "text",
          "analyzer": "ik_max_word"
        },
        "postdate": {
          "type": "date"
        },
        "url": {
          "type": "text"
        }
      }
    }
  }
}

GET /index/_search

POST /index/blog/1
{
  "id":"1",
  "title":"java虚拟机的实现原理",
  "content":"虚拟机的运行机制，运行方法，系统的底层实现",
  "postdate":"2019-01-23",
  "url":"www"
}
POST /index/blog/2
{
  "id":"2",
  "title":"spring boot 目前有被广泛使用吗？",
  "content":"spring boot 可以快速的搭建spring项目",
  "postdate":"2019-01-23",
  "url":"www"
}
POST /index/blog/3
{
  "id":"3",
  "title":"docker的使用",
  "content":"项目自动部署提供了便捷的方法",
  "postdate":"2019-01-23",
  "url":"www"
}



#批量操作
POST /lib2/books/_bulk
{"index":{"_id":"1"}}
{"title":"java","price":55}
{"index":{"_id":"2"}}
{"title":"html","price":22}
{"index":{"_id":"3"}}
{"title":"php","price":33}
{"index":{"_id":"4"}}
{"title":"python","price":44}

GET /index/books/_mget
{
  "ids":[1,2,3,4]
}


POST /lib2/books/_bulk
{"delete":{"_index":"lib2","_type":"books","_id":"4"}}
{"create":{"_index":"tt","_type":"ttt","_id":"100"}}
{"name":"lisi"}
{"index":{"_index":"tt","_type":"ttt"}}
{"name":"王五"}
{"update":{"_index":"lib2","_type":"books","_id":"3"}}
{"doc":{"price":44}}

GET /tt/ttt/_search


GET /bank/_search


## java对es客户端的操作
    查看EsDemo  PersonController

































































