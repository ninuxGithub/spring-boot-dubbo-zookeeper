package com.example.spartdemo.spark;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.DoubleFunction;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.util.StatCounter;
import scala.Tuple2;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shenzm
 * @date 2019-7-18
 * @description 作用
 */
public class BasicSum {
    static JavaSparkContext sparkContext = new JavaSparkContext("local", "basicSum",
            System.getenv(""),
            System.getenv(""));

    public static void main(String[] args) {
        sum();
    }

    private static void sum() {

        //fold
        Integer fold = sparkContext.parallelize(Arrays.asList(1, 2, 3, 4))
                .fold(0, new Function2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer v1, Integer v2) throws Exception {
                        return v1 + v2;
                    }
                });
        System.out.println(fold);


        //map
        JavaRDD<Integer> map = sparkContext.parallelize(Arrays.asList(1, 2, 3, 4))
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer call(Integer x) throws Exception {
                        return x * x;
                    }
                }).filter(new Function<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer x) throws Exception {
                        return x > 1;
                    }
                });

        System.out.println(map.collect().stream().collect(Collectors.toList()));

        //map to double
        JavaDoubleRDD javaDoubleRDD = sparkContext.parallelize(Arrays.asList(1, 2, 3, 4))
                .mapToDouble(new DoubleFunction<Integer>() {
                    @Override
                    public double call(Integer x) throws Exception {
                        return x * x * 1d;
                    }
                });

        System.out.println(javaDoubleRDD.collect().stream().collect(Collectors.toList()));


        //aggregate
        AvgCount aggregate = sparkContext.parallelize(Arrays.asList(1, 2, 3, 4))
                .aggregate(new AvgCount(0, 0), new Function2<AvgCount, Integer, AvgCount>() {
                    @Override
                    public AvgCount call(AvgCount a, Integer b) throws Exception {
                        a.total += b;
                        a.num += 1;
                        return a;
                    }
                }, new Function2<AvgCount, AvgCount, AvgCount>() {
                    @Override
                    public AvgCount call(AvgCount a, AvgCount b) throws Exception {
                        a.total += b.total;
                        a.num += b.num;
                        return a;
                    }
                });

        System.out.println(aggregate.avg());


        //迭代器里面迭代，返回迭代类型，然后reduce
        AvgCount mapPartitions = sparkContext.parallelize(Arrays.asList(1, 2, 3, 4))
                .mapPartitions(new FlatMapFunction<Iterator<Integer>, AvgCount>() {
                    @Override
                    public Iterator<AvgCount> call(Iterator<Integer> iterator) throws Exception {
                        AvgCount a = new AvgCount();
                        while (iterator.hasNext()) {
                            a.total += iterator.next();
                            a.num++;
                        }
                        List<AvgCount> list = new ArrayList<>();
                        list.add(a);
                        return list.iterator();
                    }
                }).reduce(new Function2<AvgCount, AvgCount, AvgCount>() {
                    @Override
                    public AvgCount call(AvgCount a, AvgCount b) throws Exception {
                        a.total += b.total;
                        a.num += b.num;
                        return a;
                    }
                });
        System.out.println(mapPartitions.avg());

        String fileName = "spart-demo/src/main/resources/application-dev.yml";

        JavaRDD<String> file = sparkContext.textFile(fileName);
        Map<String, Long> letterCountMap = file.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split("")).iterator();
            }
        }).countByValue();
        System.out.println(letterCountMap);


        //TODO
//        String fileName2 = "D:\\dev\\workspace-sts-3.9.0.RELEASE\\spring-boot-dubbo-zookeeper\\spart-demo\\src\\test\\java\\test.txt";
//        JavaPairRDD<Text, IntWritable> input = sparkContext.sequenceFile(fileName2, Text.class, IntWritable.class);
//        List<Tuple2<String, Integer>> collect = input.mapToPair(new PairFunction<Tuple2<Text, IntWritable>, String, Integer>() {
//            @Override
//            public Tuple2<String, Integer> call(Tuple2<Text, IntWritable> rd) throws Exception {
//                return new Tuple2<>(rd._1.toString(), rd._2.get());
//            }
//        }).collect();
//
//        System.out.println(collect);

        List<Tuple2<String, Integer>> list = new ArrayList<>();
        list.add(new Tuple2<>("java", 1));
        list.add(new Tuple2<>("php", 2));
        list.add(new Tuple2<>("c++", 3));
        JavaPairRDD<String, AvgCount> stringAvgCountJavaPairRDD = sparkContext.parallelizePairs(list)
                .combineByKey(new Function<Integer, AvgCount>() {
                    @Override
                    public AvgCount call(Integer x) throws Exception {
                        return new AvgCount(x, 1);
                    }
                }, new Function2<AvgCount, Integer, AvgCount>() {
                    @Override
                    public AvgCount call(AvgCount a, Integer v) throws Exception {
                        a.total += v;
                        a.num += 1;
                        return a;
                    }
                }, new Function2<AvgCount, AvgCount, AvgCount>() {
                    @Override
                    public AvgCount call(AvgCount a, AvgCount b) throws Exception {
                        a.total += b.total;
                        a.num += b.num;
                        return a;
                    }
                });

        Map<String, AvgCount> stringAvgCountMap = stringAvgCountJavaPairRDD.collectAsMap();
        for (String key : stringAvgCountMap.keySet()) {
            System.out.println(key + "---" + stringAvgCountMap.get(key).avg());
        }

        JavaDoubleRDD rdd = sparkContext.parallelizeDoubles(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0,1000.0));
        StatCounter stats = rdd.stats();
        double sqrt = Math.sqrt(stats.variance());
        JavaDoubleRDD filter = rdd.filter(new Function<Double, Boolean>() {
            @Override
            public Boolean call(Double v) throws Exception {
                return Math.abs(v - stats.mean()) < 3 * sqrt;
            }
        });
        System.out.println(filter.collect().stream().collect(Collectors.toList()));

        //final
        sparkContext.stop();

    }

    private static class AvgCount implements Serializable {
        private int total;
        private int num;

        public AvgCount() {
        }

        public AvgCount(int total, int num) {
            this.total = total;
            this.num = num;
        }

        public float avg() {
            return total / (float) num;
        }
    }
}
