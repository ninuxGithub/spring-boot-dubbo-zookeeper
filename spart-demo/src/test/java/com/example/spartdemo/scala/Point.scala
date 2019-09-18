package com.example.spartdemo.scala

/**
  * @Description: --
  * @Author: shenzm
  * @CreateDate: 2019-9-3 14:08
  * @Version: 1.0
  */
class Point(xx: Int, yx: Int) {

  var x: Int = xx;
  var y: Int = yx;

  def move(dx: Int, dy: Int): Unit = {
    x = x + dx;
    y = y + dy;

    println("移动后的位置：(" + x + "," + y + ")")
  }

}


class Location(/*override val*/ xx: Int, /*override val*/ yx: Int, val zx: Int) extends Point(xx, yx) {
  var z: Int = zx

  def move(dx: Int, dy: Int, dz: Int): Unit = {
    x = x + dx;
    y = y + dy;
    z = z + dz;
    println("移动后的位置：(" + x + "," + y + "," + z + ")")
  }
}






