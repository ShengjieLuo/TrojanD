package com.execute;

import com.model.other.Request
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

class Executor {

  val sparkconf = new SparkConf().setAppName("TrojanDExecutor")
  val sc = new SparkContext(sparkconf)
  sc.setLogLevel("ERROR")

  val hiveExecutor = new HiveExecutor(sc)
  val normalExecutor = new NormalExecutor(sc)

  def execute(req:Request){
    if (req.requestMode == "OPTIMIZED") {
      val hiveCmd:HiveCmd = HiveConvertor.toCmd(req)
      hiveCmd.print()
      hiveExecutor.execute(hiveCmd)
      //val result:HiveResult = HiveExecutor.execute(hiveCmd)
    } else if (req.requestMode == "DEFAULT"){
      normalExecutor.execute(req)
    }
  }
}
