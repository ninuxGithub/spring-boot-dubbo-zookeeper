package com.example.spartdemo.sql;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.spark.sql.functions.col;

/**
 * @author shenzm
 * @date 2019-7-18
 * @description 作用
 */
public class SqlTest {
    public static class Person implements Serializable {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }


    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("example")
                .config("spark.property", "value")
                .getOrCreate();
        jsonDataFrame(spark);
        beanDataFrame(spark);
        txtDataFrame(spark);

    }

    private static void txtDataFrame(SparkSession spark) {
        JavaRDD<Person> rdd = spark.read().textFile("spart-demo/src/main/resources/people.txt")
                .javaRDD()
                .map(line -> {
                    String[] arr = line.split(",");
                    Person p = new Person();
                    p.setName(arr[0]);
                    p.setAge(Integer.valueOf(arr[1].trim()));
                    return p;
                });

        Dataset<Row> persondf = spark.createDataFrame(rdd, Person.class);
        persondf.createOrReplaceGlobalTempView("person");
        Dataset<Row> sql = spark.sql("select name from global_temp.person where age between 13 and 19");
        Encoder<String> es = Encoders.STRING();
        //row.getString
        sql.map((MapFunction<Row, String>) row -> "name : " + row.getString(0), es).show();

        //row.getAs
        sql.map((MapFunction<Row, String>) row -> "name : " + row.getAs("name"), es).show();


        JavaRDD<String> prdd = spark.read().textFile("spart-demo/src/main/resources/people.txt")
                .toJavaRDD();
        JavaRDD<Row> rowRdd = prdd.map((Function<String, Row>) record -> {
            String[] split = record.split(",");
            return RowFactory.create(split[0], split[1].trim());
        });
        String schemaString = "name age";

        // Generate the schema based on the string of schema
        List<StructField> fields = new ArrayList<>();
        for (String fieldName : schemaString.split(" ")) {
            StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
            fields.add(field);
        }
        StructType schema = DataTypes.createStructType(fields);
        spark.createDataFrame(rowRdd, schema).createOrReplaceGlobalTempView("person2");
        Dataset<Row> result = spark.sql("select * from global_temp.person2");
        result.map((MapFunction<Row,String>) row -> row.getAs("name"),Encoders.STRING()).show();

        result.map((MapFunction<Row,String>) row -> row.getAs("name"),Encoders.STRING())
                .javaRDD().saveAsObjectFile("spart-demo/src/main/resources/aaa.txt");

    }

    private static void beanDataFrame(SparkSession spark) {
        //创建person对象
        Person person = new Person();
        person.setAge(18);
        person.setName("doc");

        //dataFrame添加单个集合
        Dataset<Person> personDataset = spark.createDataset(Collections.singletonList(person), Encoders.bean(Person.class));
        personDataset.show();

        Encoder<Integer> anInt = Encoders.INT();
        //添加多个集合元素
        Dataset<Integer> dataset = spark.createDataset(Arrays.asList(1, 2, 3, 4), anInt);
        dataset.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer call(Integer value) throws Exception {
                return value + 1;
            }
        }, anInt).show();

        //encoder，根据json来构建dataframe
        String path = "spart-demo/src/main/resources/people.json";
        Dataset<Person> personDataSet = spark.read().json(path).as(Encoders.bean(Person.class));
        personDataSet.show();
    }


    private static void jsonDataFrame(SparkSession spark) {
        //df 一些列的方法
        Dataset<Row> df = spark.read().json("spart-demo/src/main/resources/people.json");
        df.show();
        df.printSchema();
        df.select("name").show();
        df.select(col("name"), col("age").plus(1)).show();
        df.filter(col("age").gt(21)).show();
        df.groupBy("age").count().show();
        df.createOrReplaceGlobalTempView("people");
        //表名称需要加global_temp
        Dataset<Row> sql = spark.sql("SELECT name as aname, age as aage FROM global_temp.people");
        sql.show();


        //根据json, schema 来构建dataFrame
        String path = "spart-demo/src/main/resources/people.json";
        String schemaString = "name age";
        List<StructField> fields = new ArrayList<>();
        for (String filed : schemaString.split(" ")) {
            fields.add(DataTypes.createStructField(filed, DataTypes.StringType, true));
        }
        StructType schema = DataTypes.createStructType(fields);
        Dataset<Row> dataset = spark.read().schema(schema).json(path).cache();
        dataset.show();
    }


}
