package com.example.spartdemo.scala

/**
  * @Description: --
  * @Author: shenzm
  * @CreateDate: 2019-9-3 10:47
  * @Version: 1.0
  */
object MethodTest {

  def main(args: Array[String]): Unit = {
    sayHello("java", 16);

    sayHelloDefault("java", 22)

    sayHelloDefault(age = 30, name = "java")
    sayHelloDefault(name = "java", age = 23232)

    println("")
    println("0~n的求和" + sum(10))
    println("可变参数求和： " + sum(10, 2, 3, 4, 5))
    //1 1 2 3 5 8
    //f(n) = f(n-1) + f(n-2)
    println("斐波那契数列：" + fbnq(5))


    var z = new Array[String](3);
    z(0) = "java";
    z(1) = "doc";
    z(2) = "scala";
    for (a <- z) {
      println("打印元素： " + a)
    }

    var list = List(1, 2, 3, 4);
    for (x <- list) {
      println(x)
    }
    var set = Set(1, 2, 3, 4);
    for (x <- set) {
      println(x)
    }

    var map = Map("one" -> 1, "two" -> 2, "three" -> 3);
    for (x <- map) {
      println(x._1 + " " + x._2)
    }

    var o = (10, "runnoob", "java");
    println(o._1 + "  " + o._2 + " " + o._3)


    var x: Option[Int] = Some(5);
    for (xx <- x) {
      println("Option element :" + xx)
    }

    //iterator

    var iter = Iterator("j", "k", "l", "m");
    while (iter.hasNext) {
      print(iter.next()+ "\t")
    }

    var it = Iterator(3,2,5,9);
    var it2 = Iterator(3,2,5,9);
    println(it.max)
//    println(it.min)
    println("length : "+it2.length)
    println("size :"+it2.size)



  }

  def sayHelloDefault(name: String, age: Int = 20): Unit = {
    println(name + " " + age)
  }

  def sayHello(name: String, age: Int): Unit = {
    if (age > 18) {
      printf("%s 已成年了", name)
    } else {
      printf("%s 未成年", name)
    }
  }

  def sum(vas: Int*): Int = {
    var s = 0;
    if (vas.length > 0) {
      for (va <- vas) {
        s += va;
      }
    }
    s;
  }

  def sum(n: Int): Int = {
    var summary = 0;
    for (i <- 1 to n) {
      summary += i;
    }
    summary;
  }

  def fbnq(n: Int): Int = {
    if (n <= 1) {
      1;
    } else {
      fbnq(n - 1) + fbnq(n - 2)
    }
  }


}
