package com.service

import com.convert.Convertor
import com.model.Item
import com.model.other.Time
import com.model.other.Request
import scala.collection.JavaConverters._
import scala.io.Source
import com.rpc.Client

import scala.collection.JavaConverters._

class TroDItem(itemname:String,itemobj:String) extends Item(itemname:String,itemobj:String){

  var client:Client = new Client() 

  //TrojanD service
  def SA_KMEANS_JUDGEMENT(time:Time,dnames:List[String]){
     dnames.foreach( dname => dimensions.foreach( p => if (p.name==dname){vector.addValue(p.value)}))
     var request:Request = new Request()
     request.setTime(time)
     request.setMLParameter(vector)
     request.setRequestType("ML")
     request.setRequestMode("DEFAULT")
     //request.insertProblem("Detect Trojan in KMeans Model")
     var external = new Convertor("SA_KMEANS_JUDGEMENT",request)
     var internal:List[Request] = external.getInterface()
     client.send(internal.asJava)
  }
     
  def SA_KMEANS_JUDGEMENT(time:Time,dnames_java:java.util.List[String]){
     val dnames:List[String] = dnames_java.asScala.toList
     
     var strvector = ""
     dnames.foreach( dname => strvector += ( dname + " ") )
     println("  [KMEANS] vector requirment: " + strvector)
     var strdimen = ""
     dimensions.foreach( dname => strdimen += ( dname.name + " ") )
     println("  [KMEANS] dimension list: " + strdimen)

     for (dname <- dnames){
       for (dimension <- dimensions){
         if (dimension.name == dname) vector.addValue(dimension.value.toDouble)
       }
     }

     //dnames.foreach( dname => dimensions.foreach( p => if (p.name==dname){vector.addValue(p.value.toDouble)}))

     var request:Request = new Request()
     request.setTime(time)
     request.setMLParameter(vector)
     request.setRequestType("ML")
     request.setRequestMode("DEFAULT")
     var external = new Convertor("SA_KMEANS_JUDGEMENT",request)
     var internal:List[Request] = external.getInterface()
     var reply:List[Request] = client.send(internal.asJava).asScala.toList
  }

}
