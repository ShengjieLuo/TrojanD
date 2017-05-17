package com.execute

import scala.io.Source

class ProblemHelper{

  def loadProblems(filename:String):Array[Int]={
    var problemList:Array[Int] = Array()
    val rawData = Source.fromFile(filename)
    rawData.getLines().toList.apply(0).split(" ")
           .foreach( item => { problemList = problemList :+ item.toInt })
    problemList
  }

}


object Helper{

  val mean = loadMean()
  val dev = loadDev()
  val ten = loadTenPercentage()
  val one = loadOnePercentage()
  
  def getMean():Array[Double] = mean
  def getDev():Array[Double] = dev
  def getTen():Array[Double] = ten
  def getOne():Array[Double] = one

  def loadMean():Array[Double]={
    var meanList:Array[Double] = Array()
    val rawData = Source.fromFile("MetaData/Statistic.txt")
    val str:List[String] = rawData.getLines().toList
    val meanstr:String = str.apply(0)
    meanstr.split(" ").foreach( item => { meanList = meanList :+ item.toDouble })
    meanList
  }

  def loadDev():Array[Double]={
    var devList:Array[Double] = Array()
    val rawData = Source.fromFile("MetaData/Statistic.txt")
    val str:List[String] = rawData.getLines().toList
    val devstr:String = str.apply(1)
    devstr.split(" ").foreach( item => { devList = devList :+ item.toDouble })
    devList
  }

  def loadTenPercentage():Array[Double] = {
    var tenList:Array[Double] = Array()
    val rawData = Source.fromFile("MetaData/Statistic.txt")
    val str:List[String] = rawData.getLines().toList
    val tenstr:String = str.apply(2)
    tenstr.split(" ").foreach( item => { tenList = tenList :+ item.toDouble })
    tenList
  }
  
  def loadOnePercentage():Array[Double] = {
    var oneList:Array[Double] = Array()
    val rawData = Source.fromFile("MetaData/Statistic.txt")
    val str:List[String] = rawData.getLines().toList
    val onestr:String = str.apply(3)
    onestr.split(" ").foreach( item => { oneList = oneList :+ item.toDouble })
    oneList
  }

}
