package com.example.spartdemo;

import com.google.common.base.Joiner;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenzm
 * @date 2019-5-17
 * @description 作用
 */
public class SparkTest {

    public static void main(String[] args) {
        JavaSparkContext sparkContext = new JavaSparkContext("local[4]","JavaWordCount",".",JavaSparkContext.jarOfClass(SparkTest.class));
        System.out.println(sparkContext.getSparkHome());

        String path = "D:\\dev\\workspace-sts-3.9.0.RELEASE\\spring-boot-dubbo-zookeeper\\spart-demo\\src\\test\\java\\test.txt";
        String saveFile = "D:\\dev\\workspace-sts-3.9.0.RELEASE\\spring-boot-dubbo-zookeeper\\spart-demo\\src\\test\\java\\dir";
        JavaRDD<String> lines = sparkContext.textFile(path).cache();

        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println(lines.count());
        List<String> collect = lines.collect();
        for (String s : collect){
            System.out.println(s);
        }

        JavaRDD<String> contains = lines.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String s) throws Exception {
                return s.contains("的");
            }
        });
        System.out.println(Joiner.on(",").skipNulls().join(contains.collect()).toString());

        JavaPairRDD<String, Integer> counts = lines.flatMap(s -> Arrays.asList(s.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b);
        //counts.saveAsTextFile(saveFile);

//        Map<String, Object> result = new HashMap<>();
//        List<Tuple2<String, Integer>> output = counts.top(5);
//        for (Tuple2<String, Integer> tuple : output) {
//            result.put(tuple._1(), tuple._2());
//        }
//        System.out.println(result);

    }
}
