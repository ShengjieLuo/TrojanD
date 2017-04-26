package com.service

import com.convert.Convertor
import com.model.Item
import com.model.other.Time
import com.model.other.Request
import scala.collection.JavaConverters._
import scala.io.Source
import com.rpc.Client

class TroItem(itemname:String,itemobj:String) extends Item(itemname:String,itemobj:String){

  var client:Client = new Client() 

  //TrojanD service
  def SA_KMEANS_JUDGEMENT(time:Time,dnames:List[String]){
     dnames.foreach( dname => dimensions.foreach( if (_.name==dname){vector.addValue(_.value)}))
     var request:Request = new Request()
     request.setTime(time)
     request.setMLParameter(vector)
     request.setRequestType("ML")
     request.setRequestMode("DEFAULT")
     request.setProblem("Detect Trojan in KMeans Model")
     var external = new Convertor("SA_KMEANS_JUDGEMENT",request)
     var internal:List[Request] = external.getInterface()
     client.send(internal.asJava)
  }   
}
