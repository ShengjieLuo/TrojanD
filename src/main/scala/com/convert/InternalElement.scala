package com.convert

//import com.convert.Relation
//import com.convert.ExternalElement

import com.model.other.Request

object InternalNumber{
  var value = 0
  def getValue():Int={value=value+1;return value;}
}

class InternalElement (elementName:String,req:Request){

  var name:String = elementName
  var num:Int = InternalNumber.getValue()
  //var para:List[String] = parameter
  //var request:Request = req
  var interface:Request = buildInterface(req)

  def buildInterface(request:Request):Request = {
    var req:Request = new Request()
    //TODO:Need a graceful implement
    //req.name = request.name
    //req.requestMode = request.requestMode
    //req.beginTime = request.beginTime
    //req.endTime = request.endTime
    //req.num = request.num
    //req.parent = request.parent    

    //Graceful Implementation Here
    request.copyTo(req)
    req.setParent(request.num)
    req.setNum(num)
    req.setName(name)
    println("  [Request] Build Internal Interface: "+name)
    req
  }

  def getInterface():Request = {return interface}

  def show(){
    println("Internal Service Number:" + num)
    println("\n Internal Service Name:" + name)
    println("\n Internal Service Show:\n"+interface)
  }
}


