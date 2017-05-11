package com.execute

import scala.io.Source

class Helper{

  def loadProblems(filename:String):Array[Int]={
    var problemList:Array[Int] = Array()
    val rawData = Source.fromFile(filename)
    rawData.getLines().toList.apply(0).split(" ")
           .foreach( item => { problemList = problemList :+ item.toInt })
    problemList
  } 

  def loadMean():Array[Double]={
    var meanList:Array[Double] = Array()
    val rawData = Source.fromFile("Metadata/Statistic.txt")
    val str:List[String] = rawData.getLines().toList
    val meanstr:String = str.apply(0)
    meanstr.split(" ").foreach( item => { meanList = meanList :+ item.toDouble })
    meanList
  }

  def loadDev():Array[Double]={
    var devList:Array[Double] = Array()
    val rawData = Source.fromFile("Metadata/Statistic.txt")
    val str:List[String] = rawData.getLines().toList
    val devstr:String = str.apply(1)
    devstr.split(" ").foreach( item => { devList = devList :+ item.toDouble })
    devList
  }

}
