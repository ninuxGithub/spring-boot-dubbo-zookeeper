/**
 * Illustrates a wordcount in Java
 */
package com.example.spartdemo.normal;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;


public class WordCount {
    public static void main(String[] args) throws Exception {
        String master;
        if (args.length > 0) {
            master = args[0];
        } else {
            master = "local";
        }
        JavaSparkContext sc = new JavaSparkContext(
                master, "wordcount", System.getenv("SPARK_HOME"), System.getenv("JARS"));
        JavaRDD<String> rdd = sc.textFile("D:\\dev\\workspace-sts-3.9.0.RELEASE\\spring-boot-dubbo-zookeeper\\spart-demo\\src\\main\\resources\\application-dev.yml");
        JavaPairRDD<String, Integer> counts = rdd.flatMap(
                new FlatMapFunction<String, String>() {
                    public Iterator<String> call(String x) {
                        return Arrays.asList(x.split(" ")).iterator();
                    }
                }).mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2<String, Integer> call(String x) {
                return new Tuple2(x, 1);
            }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer x, Integer y) {
                return x + y;
            }
        });
        counts.saveAsTextFile("c://aaaaaa");
    }
}
