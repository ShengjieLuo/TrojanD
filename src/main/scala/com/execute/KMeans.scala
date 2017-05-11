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

  var sc:SparkContext = sparkContext
  var helper:Helper = new Helper()
  var model:KMeansModel = KMeansModel.load(sc,"file:///usr/local/TrojanD/KMeansModel")
  var meanList:Array[Double] = helper.loadMean()
  var devList:Array[Double] = helper.loadDev()
  var problemList:Array[Int] = helper.loadProblems("MetaData/KMeansModel.txt")
  
  def print(){
    var meanStr = "  [Bisect] Mean: "
    meanList.foreach( item => {meanStr = meanStr + item.toString + " "})
    var devStr = "  [Bisect] Dev: "
    devList.foreach( item => {devStr = devStr + item.toString + " "})
    var problemStr = "  [Bisect] Problem: "
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

