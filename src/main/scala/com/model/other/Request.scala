package com.model.other;

import com.model.Vector;
import com.rpc.Interface.IRequest._;
import com.rpc.Interface._;

class Request(){

  var requestType:String = ""
  var name:String = ""
  var parent:Int = -1
  var beginTime:Int = 0
  var endTime:Int = 0
  var requestMode:String = ""
  var num:Int = -1
  var binding:String = ""
  var order:Int = 0

  var compare:List[CompareParameter] = List()
  var all:List[AllParameter] = List()
  var single:List[SingleParameter] = List()
  var tool:List[ToolParameter] = List()
  var ml:List[ML] = List() 

  def setTime(time:Time){
    beginTime = time.getbeginTime();
    endTime = time.getendTime();
  }
  def setNum(obj:Int) { num = obj;}
  def setRequestType(obj:String) { requestType = obj;}
  def setName(obj:String) { name = obj;}
  def setParent(obj:Int) { parent = obj;}
  def setRequestMode(obj:String) { requestMode = obj;}
  def setBeginTime(obj:Int) {beginTime = obj}
  def setEndTime(obj:Int) {endTime = obj}
  def setBinding(obj:String){binding = obj}
  def setOrder(obj:Int) {order=obj}

  def getNum():Int = {num}
  def getName():String = {name}
  def getParent():Int = {parent}
  def getRequestType():String = {requestType}
  def getRequestMode():String = {requestMode}
  def getBeginTime():Int = {beginTime}
  def getEndTime():Int = {endTime}
  def getBinding():String = {binding}
  def getOrder():Int = {order}

  def setAllParameter(obj1:String,obj2:String,obj3:String){
      var obj:AllParameter = new AllParameter(obj1,obj2,obj3)
      all = all :+ obj
  }
 
  def setSingleParameter(obj1:String,obj2:String,obj3:String){
      var obj:SingleParameter = new SingleParameter(obj1,obj2,obj3)
      single = single :+ obj
  }
  
  def setCompareParameter(obj1:String,obj2:String,obj3:String,obj4:String,obj5:String,obj6:String,obj7:String){
      var obj:CompareParameter = new CompareParameter(obj1,obj2,obj3,obj4,obj5,obj6,obj7)
      compare = compare :+ obj
  }
  
  def setToolParameter(obj1:String,obj2:String,obj3:String){
      var obj:ToolParameter = new ToolParameter(obj1,obj2,obj3)
      tool = tool :+ obj
  }

  def setMLParameter(obj1:Vector){
      var obj:ML = new ML(obj1)
      ml = ml :+ obj
  }

  def getAllParameter():AllParameter = {return all.last}
  def getToolParameter():ToolParameter = {return tool.last}
  def getSingleParameter():SingleParameter = { return single.last}
  def getCompareParameter():CompareParameter = { return compare.last}  
  def getMLParameter():ML = { return ml.last}

  def copyTo():Request = {
    var obj = new Request()
    obj.setName(name)
    obj.setNum(num)
    obj.setParent(parent)
    obj.setRequestType(requestType)
    obj.setRequestMode(requestMode)
    obj.setBeginTime(beginTime)
    obj.setEndTime(endTime)
    obj.setBinding(binding)
    obj.setOrder(order)
    if (all.length>0) {obj.all = this.all}
    if (tool.length>0) {obj.tool = this.tool}
    if (single.length>0) {obj.single = this.single}
    if (compare.length>0) {obj.compare = this.compare}
    if (ml.length>0) {obj.ml = this.ml}
    return obj
  }

  def copyFromInterface(ireq:IRequest){
    setName(ireq.getName());
    setBeginTime(ireq.getBeginTime());
    setEndTime(ireq.getEndTime());
    setNum(ireq.getNum());
    setParent(ireq.getParent());

    ireq.getType().getNumber() match {
      case 0 =>
      {
        setRequestType("ALL");
        var allobj:com.rpc.Interface.AllParameter = ireq.getAll();
        val obj1:String = allobj.getContent();
        val obj2:String = allobj.getKind();
        val obj3:String = allobj.getMethod();
        setAllParameter(obj1,obj2,obj3);
      }
      case 2 =>
      {
        setRequestType("SINGLE");
        var sinobj:com.rpc.Interface.SingleParameter = ireq.getSingle();
        val obj1:String = sinobj.getContent();
        val obj2:String = sinobj.getKind();
        val obj3:String = sinobj.getObject();
        setSingleParameter(obj1,obj2,obj3);
      }
      case 3 =>
      {
        setRequestType("TOOL");
        var toolobj:com.rpc.Interface.ToolParameter = ireq.getTool();
        val obj1:String = toolobj.getContent();
        val obj2:String = toolobj.getKind();
        val obj3:String = toolobj.getObject();
        setToolParameter(obj1,obj2,obj3);
      }
      case 1 =>
      {
        setRequestType("COMPARE");
        var comobj:com.rpc.Interface.CompareParameter = ireq.getCompare();
        val obj1:String = comobj.getObjectAType();
        val obj2:String = comobj.getObjectA();
        val obj3:String = comobj.getObjectBType();
        val obj4:String = comobj.getObjectB();
        val obj5:String = comobj.getMethod();
        val obj6:String = comobj.getIndex();
        val obj7:String = comobj.getProblem();
        setCompareParameter(obj1,obj2,obj3,obj4,obj5,obj6,obj7);
      }
      case 4 =>
      {
        setRequestType("ML");
        var mlobj:com.rpc.Interface.MLParameter = ireq.getMl();
        var vectorobj:com.rpc.Interface.Vector = mlobj.getVector();
        var obj1:com.model.Vector = new com.model.Vector();
        obj1.setValue(vectorobj.getValueList());
        setMLParameter(obj1);
      }
    }

    ireq.getMode().getNumber() match {
        case 0 => setRequestMode("DEFAULT");
        case 1 => setRequestType("OPTIMIZED");
        case 2 => setRequestType("SIMPLE");
    }      
  }

  def copyToInterface():IRequest={
    val iRequest:IRequest.Builder = IRequest.newBuilder();
    iRequest.setName(name);
    iRequest.setNum(num);
    iRequest.setParent(parent);
    iRequest.setBeginTime(beginTime);
    iRequest.setEndTime(endTime);
    
    if ( requestType.equals("ALL")){
      iRequest.setType(RequestType.ALL);
      var iall:com.rpc.Interface.AllParameter.Builder = com.rpc.Interface.AllParameter.newBuilder();
      var all:com.model.other.AllParameter = getAllParameter();
      iall.setContent(all.getContent());
      iall.setKind(all.getKind());
      iall.setMethod(all.getMethod());
      iRequest.setAll(iall.build());
    } else if ( requestType.equals("SINGLE")){
      iRequest.setType(RequestType.SINGLE);
      var isin:com.rpc.Interface.SingleParameter.Builder = com.rpc.Interface.SingleParameter.newBuilder();
      var sin:com.model.other.SingleParameter = getSingleParameter();
      isin.setContent(sin.getContent());
      isin.setKind(sin.getKind());
      isin.setObject(sin.getObj());
      iRequest.setSingle(isin.build());
    } else if ( requestType.equals("COMPARE")){
      iRequest.setType(RequestType.COMPARE);
      var icom:com.rpc.Interface.CompareParameter.Builder = com.rpc.Interface.CompareParameter.newBuilder();
      var ccom:com.model.other.CompareParameter = getCompareParameter();
      icom.setObjectAType(ccom.getObjectAType());
      icom.setObjectBType(ccom.getObjectBType());
      icom.setObjectA(ccom.getObjectA());
      icom.setObjectB(ccom.getObjectB());
      icom.setMethod(ccom.getMethod());
      icom.setIndex(ccom.getMethod());
      icom.setProblem(ccom.getProblem());
      iRequest.setCompare(icom.build());
    } else if ( requestType.equals("TOOL")){
      iRequest.setType(RequestType.TOOL);
      var itool:com.rpc.Interface.ToolParameter.Builder = com.rpc.Interface.ToolParameter.newBuilder();
      var tool:com.model.other.ToolParameter = getToolParameter();
      itool.setContent(tool.getContent());
      itool.setKind(tool.getKind());
      itool.setObject(tool.getObj());
      iRequest.setTool(itool.build());
    } else if ( requestType.equals("ML")){
      iRequest.setType(RequestType.ML);
      var iml:com.rpc.Interface.MLParameter.Builder = com.rpc.Interface.MLParameter.newBuilder();
      var ivector:com.rpc.Interface.Vector.Builder = com.rpc.Interface.Vector.newBuilder();
      var ml:com.model.other.ML  = getMLParameter();
      var vector:com.model.Vector = ml.getVector();
      var values:java.util.List[java.lang.Double] = vector.getValue("java");
      for (j <- 0 to values.size()-1 ){ivector.addValue(values.get(j));};
      iml.setVector(ivector.build());
      iRequest.setMl(iml.build());
     }

     if ( requestMode.equals("DEFAULT")){
       iRequest.setMode(RequestMode.DEFAULT);
     } else if ( requestMode.equals("OPTIMIZED")){
       iRequest.setMode(RequestMode.OPTIMIZED);
     } else if ( requestMode.equals("SIMPLE")){
       iRequest.setMode(RequestMode.SIMPLE);
     }

     return iRequest.build()
  }

  def print(){
      println("============Service Request============")
      println("  [Request] Request Number: "+num);
      println("  [Request] Request name: "+name);
      println("  [Request] Request Begin Time:"+beginTime)
      println("  [Request] Request End Time:"+endTime)
      println("  [Request] Request Mode:"+requestMode)
      println("  [Request] Request Type:"+requestType)
      println("  [Request] Request Parent:"+parent)
      if (requestType == "ALL"){
        all.last.print()
      } else if (requestType == "SINGLE"){
        single.last.print()
      } else if (requestType == "COMPARE"){
        compare.last.print()
      } else if (requestType == "TOOL"){
        tool.last.print()
      } else if (requestType == "ML"){
        ml.last.print()
      }
      println("=======================================")
  }

  def equals(obj:Request):Boolean = {
    if ( requestType == obj.requestType && name == obj.name && beginTime == obj.beginTime && endTime == obj.endTime && requestMode == obj.requestMode){
      if (requestType == "ALL"){
        return all.last.equals(obj.all.last)
      } else if (requestType == "SINGLE") {
        return single.last.equals(obj.single.last)
      } else if (requestType == "COMPARE") {
        return compare.last.equals(obj.compare.last)
      } else if (requestType == "TOOL"){
        return tool.last.equals(obj.tool.last)
      } else if (requestType == "ML"){
        return ml.last.equals(obj.ml.last)
      }
        else {
        return false
      }
    } else {
      return false
    }
  }
}
