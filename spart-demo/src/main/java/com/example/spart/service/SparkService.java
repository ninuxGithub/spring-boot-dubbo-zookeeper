package com.example.spart.service;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Serializable;
import scala.Tuple2;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author shenzm
 * @date 2019-7-17
 * @description 作用
 */

@Service
public class SparkService implements Serializable {

    @Autowired
    private transient JavaSparkContext sparkContext;

    private static final Pattern SPACE = Pattern.compile(" ");

    public Map<String, Object> wardCount() {
        Map<String, Object> result = new HashMap<>();
        try {
            JavaRDD<String> lines = sparkContext.textFile("D:\\dev\\workspace-sts-3.9.0.RELEASE\\spring-boot-dubbo-zookeeper\\spart-demo\\src\\test\\java\\test.txt").cache();

            System.out.println();
            System.out.println("-------------------------------------------------------");
            System.out.println(lines.count());

            JavaRDD<String> words = lines.flatMap(str -> Arrays.asList(SPACE.split(str)).iterator());

            JavaPairRDD<String, Integer> ones = words.mapToPair(str -> new Tuple2<String, Integer>(str, 1));

            JavaPairRDD<String, Integer> counts = ones.reduceByKey((Integer i1, Integer i2) -> (i1 + i2));

            JavaPairRDD<Integer, String> temp = counts.mapToPair(tuple -> new Tuple2<Integer, String>(tuple._2, tuple._1));

            JavaPairRDD<String, Integer> sorted = temp.sortByKey(false).mapToPair(tuple -> new Tuple2<String, Integer>(tuple._2, tuple._1));

            System.out.println();
            System.out.println("-------------------------------------------------------");
            System.out.println(sorted.count());

            List<Tuple2<String, Integer>> output = sorted.collect();

//            sorted.top(10);

            for (Tuple2<String, Integer> tuple : output) {
                result.put(tuple._1(), tuple._2());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
