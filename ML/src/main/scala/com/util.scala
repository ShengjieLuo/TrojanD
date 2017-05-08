package com;

import java.io._
import scala.io.Source
import scala.util.matching.Regex
import org.apache.spark.rdd.RDD

class Summer(sname:String){

  val name:String = sname
  var value:Double = 0

  def addValue(va:Double){ value += va}
  def addValue(va:Int) {value += va}
  def getValue():Double =  value

  def print():String = {
    val str:String = "Item: " + name + " Sum: "+ value.toString
    println(str)
    str
  }
}

class Statistic(){

  var counter:Int = 0
  var summerList:List[Summer] = List()
  var meanList:Array[Double] = Array(0,0,0,0,0,0,0)
  var devList:Array[Double] = Array(0,0,0,0,0,0,0)
  summerListInit()

  def summerListInit(){
    summerList = summerList :+ new Summer("Session")
    summerList = summerList :+ new Summer("dns")
    summerList = summerList :+ new Summer("Size-Ratio")
    summerList = summerList :+ new Summer("Count-Ratio")
    summerList = summerList :+ new Summer("SYN-Ratio")
    summerList = summerList :+ new Summer("PSH-Ratio")
    summerList = summerList :+ new Summer("Small-Ratio")
  }

  def addValue(index:Int,va:Double){ summerList.apply(index).addValue(va)}
  def addValue(index:Int,va:Int){ summerList.apply(index).addValue(va)}
  def addDev(index:Int,va:Double){ devList(index) = devList(index) + (va-meanList(index))*(va-meanList(index))}
  def addCount(){counter += 1}

  def updateMean():Array[Double] = {
    for (i <- 0 to 6) {meanList(i) = summerList.apply(i).getValue()/counter}
    return meanList
  }

  def updateDev():Array[Double] = {
    for (i <- 0 to 6) {devList(i) = math.sqrt(devList(i)/counter)}
    return devList
  }

  def print(){
    summerList.foreach(_.print())
    meanList.foreach(println)
    devList.foreach(println)
  }

  def record(writer:PrintWriter){
    var meanstr:String = "Mean: "
    var devstr:String = "Dev: "
    meanList.foreach( item => meanstr += item.toString + " ")
    devList.foreach( item => devstr += item.toString + " ")
    writer.write( meanstr + "\n" )
    writer.write( devstr + "\n" )
  }
}

