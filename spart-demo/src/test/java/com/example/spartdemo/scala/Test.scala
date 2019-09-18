package com.example.spartdemo.scala

/**
  * @Description: --
  * @Author: shenzm
  * @CreateDate: 2019-9-3 14:11
  * @Version: 1.0
  */
object Test {
  def main(args: Array[String]): Unit = {

    var point = new Point(10,20);
    point.move(5,3);

    var up = new Location(10,10,10);
    up.move(2,3,5);
  }
}
