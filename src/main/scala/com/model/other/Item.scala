package com.model.other

import com.service.TCPservice

class dimension(dname,dvalue){
  var name:String = dname
  var value:Double = dvalue
}


class Item(itemname:String,itemobj:String) extends TCPservice(itemobj:String) {
  
  var name:String = itemname
  var problems:List[String] = List()
  override val obj:String = itemobj


  def getname():String = {return name}
  //def setname(itemname:String){name=itemname}

  def getobj():String = {return obj}
  //def setobj(objname:String){obj = objname}

  def insertProblem(problem:String){problems = problem::problems}

  def insertProblems(problemList:List[String]){problemList.foreach(p => this.insertProblem(p))}

  def getProblems():List[String]={return problems;}

  def printProblems():String = {
    var str = ""
    problems.foreach(p => str=str+p)
    return str
  }

  //New Variable set for TrojanD
  var syn:Int = null
  var up_count:Int = null
  var up_size:Int = null
  var up_small:Int = null
  var psh:Int = null
  var down_count:Int = null
  var down_size:Int = null
  var down_small:Int = null
  var dns:Int = null
  var dimensions:List[Dimension] = List()

  def setSYN(a:Int){syn=a}
  def setUP(a:Int,b:Int,c:Int){up_count=a;up_size=b;up_small=c;}
  def setPSH(a:Int){psh=a}
  def setDOWN(a:Int,b:Int,c:Int){down_count=a;down_size=b;down_small=c;}
  def setDNS(a:Int){dns=a}

  def addDimension(di:Dimension){dimensions = dimensions :+ di}

}
