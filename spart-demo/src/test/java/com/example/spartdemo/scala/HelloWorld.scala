package com.example.spartdemo.scala


/**
  * @Description: --
  * @Author: shenzm
  * @CreateDate: 2019-8-22 18:40
  * @Version: 1.0
  */
class HelloWorld {


  def main(args: Array[String]): Unit = {
    println("Hello World is the first program")
  }


  class Inner {
    private def f(): Unit = {
      println("f")
    }

    class InnerMost {
      f();
    }

  }


}
