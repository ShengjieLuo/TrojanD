package com.model

import com.service.TCPservice

class Dimension(dname:String,dvalue:Double){
  var name:String = dname
  var value:Double = dvalue
}

class Problem(pname:String){
  var name:String = pname
}

class Vector(){
  var values:List[Double] = List()
  var addValue(a:Double){values = values :+ a}
}

class Item(itemname:String,itemobj:String) {
  
  var name:String = itemname
  override val obj:String = itemobj 
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
  var problems:List[Problem] = List()
  var vector:Vector = null

  private def _setSYN(a:Int){syn=a}
  private def _setUP(a:Int,b:Int,c:Int){up_count=a;up_size=b;up_small=c;}
  private def _setPSH(a:Int){psh=a}
  private def _setDOWN(a:Int,b:Int,c:Int){down_count=a;down_size=b;down_small=c;}
  private def _setDNS(a:Int){dns=a}

  def getName():String = name
  def getObj():String = obj
  def getSYN():Int = syn
  def getUpCount():Int = up_count
  def getUpSize():Int = up_size
  def getPSH():Int = syn
  def getDownCount():Int = down_count
  def getDownSize():Int = down_size
  def getDNS():Int  = dns
  def getUpSmall():Int = up_small
  def getDownSmall():Int = down_small
  def getDimensions():List[Dimension] = dimensions
  def getDimension(i:Int):Dimension = dimensions.apply(i)
  def getProblems():List[Problem] = problems
  def getProblem(i:Int):Problem = problems.apply(i)
  def getVector():Vector = vector

  def addDimension(di:Dimension){dimensions = dimensions :+ di}
  def insertProblem(problem:String){problems = problems::Problem(problem)}
  def insertProblems(problemList:List[String]){problemList.foreach(p => this.insertProblem(p))}
  def printProblems():String = { var str = "";problems.foreach(p => str = str+p.name);str}

  def setFromDataLine(data:Array[String]){
    //TODO 
  }

}
