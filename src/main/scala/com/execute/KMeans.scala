package com.execute

import com.model.other.Request
import com.internal.InternalTrigger
import org.apache.spark.SparkContext 
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.KMeansModel
import scala.io.Source
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vector

class KMeans(sparkContext:SparkContext){

  var model:KMeansModel = null
  var flag:Boolean = false
  var meanList:Array[Double] = Array()
  var devList:Array[Double] = Array()
  var problemList:Array[Int] = Array()
  var sc:SparkContext = sparkContext

  private def _loadMeanAndDev(){
    val rawData = Source.fromFile("sample/KMeansMeta/metadata.txt")
    val str:List[String] = rawData.getLines().toList
    val meanstr:String = str.apply(0)
    val devstr:String = str.apply(1)
    meanstr.split(" ").foreach( item => { meanList = meanList :+ item.toDouble })
    devstr.split(" ").foreach( item => { devList = devList :+ item.toDouble })
  } 

  private def _loadProblems(){
    val rawData = Source.fromFile("sample/KMeansMeta/ProblemClusters.txt")
    rawData.getLines().toList.apply(0).split(" ")
           .foreach( item => { problemList = problemList :+ item.toInt })  
  }

 def setKMeansModel(){
    if ( flag == false){
        model = KMeansModel.load(sc,"file:///usr/local/TrojanD/KMeansModel")
        _loadMeanAndDev()
        _loadProblems()
        flag = true
      }
  }

  def print(){
    var meanStr = "  [KMeans] Mean: "
    meanList.foreach( item => {meanStr = meanStr + item.toString + " "})
    var devStr = "  [KMeans] Dev: "
    devList.foreach( item => {devStr = devStr + item.toString + " "})
    var problemStr = "  [KMeans] Problem: "
    problemList.foreach( item => {problemStr = problemStr + item.toString + " "})
    println(meanStr)
    println(devStr)
    println(problemStr)
  }

  def predict(data:List[Double]):Boolean={
    var i:Int = 0
    data.foreach( item => {
      val ele:Double = (item - meanList(i))/devList(i)
      i = i + 1
      ele
    })
    val vector:Vector = Vectors.dense(data.toArray)
    val cluster:Int = model.predict(vector)
    if (problemList.contains(cluster)) {return true}
    return false
  }
}

