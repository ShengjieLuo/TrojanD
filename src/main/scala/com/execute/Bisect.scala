package com.execute

import com.model.other.Request
import com.internal.InternalTrigger
import org.apache.spark.SparkContext 
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.BisectingKMeansModel
import scala.io.Source
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vector

class Bisect(sparkContext:SparkContext){

  var sc:SparkContext = sparkContext
  var helper:ProblemHelper = new ProblemHelper()
  var model:BisectingKMeansModel = BisectingKMeansModel.load(sc,"file:///usr/local/TrojanD/BisectingKMeansModel")
  var meanList:Array[Double] = Helper.getMean()
  var devList:Array[Double] = Helper.getDev()
  var problemList:Array[Int] = helper.loadProblems("MetaData/BisectingKMeansModel.txt")
  
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

