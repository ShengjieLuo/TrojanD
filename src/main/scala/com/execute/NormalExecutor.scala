package com.execute

import com.model.other.Request
import com.internal.InternalTrigger
import org.apache.spark.SparkContext 
import org.apache.spark.SparkConf

class NormalExecutor{

  val sparkconf = new SparkConf().setAppName("TrojanNormalExecutor")
  val sc = new SparkContext(sparkconf)

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
      //TODO:Use the random number now
      var randnum = (new util.Random).nextInt(10)
      if (randnum==0){println(" [Result] Detect Trojan wihtin KMeans Model")}
      InternalTrigger.insertEvent(req)
    } else {
      InternalTrigger.updateStatus(req)
    }
  }
}
