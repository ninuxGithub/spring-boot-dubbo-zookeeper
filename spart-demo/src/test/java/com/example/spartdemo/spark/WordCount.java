package com.example.spartdemo.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author shenzm
 * @date 2019-7-18
 * @description 作用
 */
public class WordCount {

    static transient JavaSparkContext sparkContext = new JavaSparkContext("local[4]", "JavaWordCount", ".", JavaSparkContext.jarOfClass(WordCount.class));

    public static void main(String[] args) {
        SparkConf sparkConf = sparkContext.getConf();
        sparkConf.set("spark.serializer", "com.esotericsoftware.kryo.KryoSerializable");
        sparkConf.set("spark.kryo.registrationRequired", "true");
        sparkConf.registerKryoClasses(new Class[]{WordCount.class});
        System.out.println(sparkContext.getSparkHome());

        String path = "spart-demo/src/test/java/test.txt";
        long time = System.currentTimeMillis();
        String saveFile = "spart-demo/src/test/java/dir/" + time;
        JavaRDD<String> lines = sparkContext.textFile(path).cache();
        JavaPairRDD<String, Integer> counts = lines.flatMap(s -> Arrays.asList(s.split("")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b);

//        JavaPairRDD<String, Integer> mapToPair = counts.mapToPair(tuple -> new Tuple2<>(tuple._2, tuple._1))
//                .sortByKey(new Comparator<Integer>() {
//                    @Override
//                    public int compare(Integer o1, Integer o2) {
//                        return Integer.compare(o1, o2);
//                    }
//                }).mapToPair(tuple -> new Tuple2<>(tuple._2, tuple._1));
//        mapToPair.foreach(new VoidFunction<Tuple2<String, Integer>>() {
//            @Override
//            public void call(Tuple2<String, Integer> tuple2) throws Exception {
//                System.out.println(tuple2._1 + " " + tuple2._2);
//            }
//        });
        counts.saveAsTextFile(saveFile);
    }


}
