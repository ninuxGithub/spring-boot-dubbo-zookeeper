package com.example.spartdemo.scala


object HelloWord {

  /**
    * 主方法
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    println("Hello World")
    val a = 1;
    println(a);

    //注释
    val name: String = null
    println(name)

    val name2 = "java";
    println(name2)

    //call scala class
    new HelloWorld();

    //call java class
    new A().sayHello();

    //range
    println(1 to 10)
    println(Math.pow(2, 2))


    var p = "aaa";
    p = "bbb";
    println(p);

    var x = 20;
    if (x > 10) {
      println("x > 10")
    } else {
      println("x <=10")
    }

    while (x > 0) {
      println("x 的值为： " + x)
      x -= 1;
    }

    println("x while 之后的值为=========>" + x)

    do {
      println("x 的值为=：" + x)
      x += 1;
    } while (x <= 20)


    for (a <- 1 to 10) {
      println("variable is : " + a)
    }

    for (a <- 1 until (10)) {
      println("variable until is : " + a)
    }

    //    var a = 0;
    var b = 0;
    for (a <- 1 to 3; b <- 1 to 3) {
      println("a is : " + a + " b is ：" + b)
    }

    val list = List(1, 2, 3, 4, 5);
    var retVal = for {a <- list
                      if a % 2 == 0
    } yield a


    for (a <- retVal) {
      println("yield  " + a)
    }


  }
}