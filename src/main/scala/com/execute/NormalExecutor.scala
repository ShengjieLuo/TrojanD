package com.execute

import com.model.other.Request
import com.internal.InternalTrigger
import org.apache.spark.SparkContext 
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.KMeansModel
import scala.io.Source

class NormalExecutor(sparkContext:SparkContext){

  val sc = sparkContext
  val kMeans = new KMeans(sc)  

  def execute(req:Request) {
    if (req.name == "_SA_KMEANS_JUDGEMENT") {
       execute_SA_KMEANS_JUDGEMENT(req)
    }
  }

  def execute_SA_KMEANS_JUDGEMENT(req:Request){
    var status:Int = InternalTrigger.queryStatus(req)
    // status = 0: A new task
    // status = 1: An old task
    if (status == 0) {
      kMeans.setKMeansModel()
      kMeans.print()
      val result:Boolean = kMeans.predict(req.ml.last.vector.values)
      if (result) { req.ml.last.setProblem("KMeans Judge Trojan Detection")}
      InternalTrigger.insertEvent(req)
    } else {
      InternalTrigger.updateStatus(req)
    }
  }
}
