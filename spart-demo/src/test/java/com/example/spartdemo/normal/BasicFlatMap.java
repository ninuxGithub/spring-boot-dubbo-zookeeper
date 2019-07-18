/**
 * Illustrates a simple flatMap in Java to extract the words
 */
package com.example.spartdemo.normal;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class BasicFlatMap {
    public static void main(String[] args) throws Exception {

        JavaSparkContext sc = new JavaSparkContext(
                "local", "basicflatmap", System.getenv("SPARK_HOME"), System.getenv("JARS"));
        JavaRDD<String> rdd = sc.textFile("D:\\dev\\workspace-sts-3.9.0.RELEASE\\spring-boot-dubbo-zookeeper\\spart-demo\\src\\main\\resources\\application-dev.yml");
        JavaRDD<String> words = rdd.flatMap(
                new FlatMapFunction<String, String>() {
                    public Iterator<String> call(String x) {
                        return Arrays.asList(x.split(" ")).iterator();
                    }
                });
        Map<String, Long> result = words.countByValue();
        for (Entry<String, Long> entry : result.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
