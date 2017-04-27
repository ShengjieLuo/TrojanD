package com.execute;

import com.model.other.Request

class Executor {

  val hiveExecutor = new HiveExecutor()
  val normalExecutor = new NormalExecutor()

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
