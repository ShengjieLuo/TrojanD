package com.execute

import com.model.other.Request
import com.internal.InternalTrigger
import org.apache.spark.SparkContext 
import org.apache.spark.SparkConf
import scala.io.Source

class NormalExecutor(sparkContext:SparkContext){

  val sc = sparkContext
  val kMeans = new KMeans(sc)  
  val bisect = new Bisect(sc)
  val gmm = new GMM(sc)

  def execute(req:Request) {
    req.name match {
      case "_SA_KMEANS_JUDGEMENT" => execute_SA_KMEANS_JUDGEMENT(req)
      case "_SA_BISECT_JUDGEMENT" => execute_SA_BISECT_JUDGEMENT(req)
      case "_SA_GMM_JUDGEMENT" => execute_SA_GMM_JUDGEMENT(req)
    }
  }

  def execute_SA_KMEANS_JUDGEMENT(req:Request){
    var status:Int = InternalTrigger.queryStatus(req)
    // status = 0: A new task
    // status = 1: An old task
    if (status == 0) {
      kMeans.setModel()
      kMeans.print()
      val result:Boolean = kMeans.predict(req.ml.last.vector.values)
      if (result) { req.ml.last.setProblem("KMeans Judge Trojan Detection")}
      InternalTrigger.insertEvent(req)
    } else {
      InternalTrigger.updateStatus(req)
    }
  }
  
  def execute_SA_BISECT_JUDGEMENT(req:Request){
    var status:Int = InternalTrigger.queryStatus(req)
    // status = 0: A new task
    // status = 1: An old task
    if (status == 0) {
      bisect.print()
      val result:Boolean = bisect.predict(req.ml.last.vector.values)
      if (result) { req.ml.last.setProblem("Bisect Judge Trojan Detection")}
      InternalTrigger.insertEvent(req)
    } else {
      InternalTrigger.updateStatus(req)
    }
  }

  def execute_SA_GMM_JUDGEMENT(req:Request){
    var status:Int = InternalTrigger.queryStatus(req)
    // status = 0: A new task
    // status = 1: An old task
    if (status == 0) {
      gmm.print()
      val result:Boolean = gmm.predict(req.ml.last.vector.values)
      if (result) { req.ml.last.setProblem("GMM Judge Trojan Detection")}
      InternalTrigger.insertEvent(req)
    } else {
      InternalTrigger.updateStatus(req)
    }
  }

}
