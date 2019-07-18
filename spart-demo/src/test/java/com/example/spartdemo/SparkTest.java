package com.example.spartdemo;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.io.Serializable;
import java.util.*;

/**
 * @author shenzm
 * @date 2019-5-17
 * @description 作用
 */
public class SparkTest {

    public static void main(String[] args) {
        JavaSparkContext sparkContext = new JavaSparkContext("local[4]","JavaWordCount",".",JavaSparkContext.jarOfClass(SparkTest.class));
        System.out.println(sparkContext.getSparkHome());

        String path = "spart-demo/src/test/java/test.txt";
        long time = System.currentTimeMillis();
        String saveFile = "spart-demo/src/test/java/dir/" + time;
        JavaRDD<String> lines = sparkContext.textFile(path).cache();

        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println(lines.count());
        List<String> collect = lines.collect();
        for (String s : collect){
            System.out.println("collect : " + s);
        }

        JavaRDD<String> contains = lines.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String s) throws Exception {
                return s.contains("的");
            }
        });
        System.out.println("join : " + Joiner.on(",").skipNulls().join(contains.collect()).toString());

        JavaPairRDD<String, Integer> counts = lines.flatMap(s -> Arrays.asList(s.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> (Integer) a + (Integer) b);
        counts.saveAsTextFile(saveFile);


        List<Integer> list = ImmutableList.of(1, 2, 3, 4, 5);
        List<Integer> top3 = sparkContext.parallelize(list).top(3);
        System.out.println(top3);

        List<Tuple2<String, Integer>> tuples = counts.collect();
        for (Tuple2<String, Integer> tuple : tuples) {
            System.out.println(tuple._1() + "----- " + tuple._2());
        }

        //对JavaPairRDD运行top抛异常 不知道为何
//        List<Tuple2<String, Integer>> output = counts.top(2, new Comparator<Tuple2<String, Integer>>() {
//
//            @Override
//            public int compare(Tuple2<String, Integer> o1, Tuple2<String, Integer> o2) {
//                System.out.println("compare : " + o1 + "  "+ o2);
//                return Integer.compare(o1._2(), o2._2());
//            }
//        });
//
//        Map<String, Object> result = new HashMap<>();
//        for (Tuple2<String, Integer> tuple : output) {
//            result.put(tuple._1(), tuple._2());
//        }
//        System.out.println(result);

    }

}
