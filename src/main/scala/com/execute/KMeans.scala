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

  var modelPath = "file://" + sys.env("TROJAND_HOME")+"/KMeansModel"
  var metaPath = sys.env("TROJAND_HOME")+"/MetaData/KMeansModel.txt"
  var sc:SparkContext = sparkContext
  var helper:ProblemHelper = new ProblemHelper()
  var model:KMeansModel = KMeansModel.load(sc,modelPath)
  var meanList:Array[Double] = Helper.getMean()
  var devList:Array[Double] = Helper.getDev()
  var problemList:Array[Int] = helper.loadProblems(metaPath)
  
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

