package com.model

import com.service.TroDItem
import scala.collection.JavaConverters._
import scala.util.matching.Regex

class Dimension(dname:String,dvalue:Double){
  var name:String = dname
  var value:Double = dvalue
}

class Problem(pname:String){
  var name:String = pname
}

class Vector(){
  var values:List[Double] = List()
  def addValue(a:Double){values = values :+ a}
  def getValue():List[Double] = {values}
  def getValue(option:String):java.util.List[java.lang.Double] = {
    var result:java.util.List[java.lang.Double] = null
    if (option=="java") {result = values.map(new java.lang.Double(_)).toBuffer.asJava}
    result
  }
}

class Item(itemname:String,itemobj:String) {
  
  var name:String = itemname
  val obj:String = itemobj 
  var syn:Int = -1
  var up_count:Int = -1
  var up_size:Int = -1
  var up_small:Int = -1
  var psh:Int = -1
  var down_count:Int = -1
  var down_size:Int = -1
  var down_small:Int = -1
  var dns:Int = -1
  var session_total:Int = -1
  var session_count:Int = -1
  var dimensions:List[Dimension] = List()
  var problems:List[Problem] = List()
  var vector:Vector = new Vector()
  var valid:Boolean = false
  var status:Int = 0

  private def _setSYN(a:Int){syn=a}
  private def _setUP(a:Int,b:Int,c:Int){up_count=a;up_size=b;up_small=c;}
  private def _setUP(a:String,b:String,c:String){up_count=a.toInt;up_size=b.toInt;up_small=c.toInt;}
  private def _setPSH(a:Int){psh=a}
  private def _setDOWN(a:Int,b:Int,c:Int){down_count=a;down_size=b;down_small=c;}
  private def _setDOWN(a:String,b:String,c:String){down_count=a.toInt;down_size=b.toInt;down_small=c.toInt;}
  private def _setDNS(a:Int){dns=a}
  private def _setSession(a:Int,b:Int){session_count=a;session_total=b;}
  private def _setSession(a:String,b:String){session_count=a.toInt;session_total=b.toInt;}

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
  
  def setValid(){valid = true}
  def setUnvalid(){valid = false}
  def isValid():Boolean = valid
  def setStatus(a:Int){status = a}
  def addDimension(di:Dimension):Unit = {dimensions = dimensions :+ di}
  def addDimension(str:String,va:Double):Unit = {dimensions = dimensions :+ new Dimension(str,va)}
  def addDimension(str:String,va:Int):Unit = {dimensions = dimensions :+ new Dimension(str,va.toDouble)}
  def insertProblem(problem:String){problems = problems :+ new Problem(problem)}
  def insertProblems(problemList:List[String]){problemList.foreach(p => this.insertProblem(p))}
  def printProblems():String = { var str = "";problems.foreach(p => str = str+p.name);str}

  def setFromDataLine(data:String){
    //Use Regular Experssion Here
    //Data Sample1: 88.238.138.180 syn 2 2 up 2 128 small 2
    val pattern1 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} syn (\\d{1,}) \\d{1,} up (\\d{1,}) (\\d{1,}) small (\\d{1,})".r
    //Data Sample2: 24.128.223.54 dns 1 0
    val pattern2 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} dns (\\d{1,}) \\d{1,}".r
    //Data Sample3: 62.70.118.37 psh 2 2 down 2 132 small 0
    val pattern3 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} psh (\\d{1,}) \\d{1,} down (\\d{1,}) (\\d{1,}) small (\\d{1,})".r
    //Data Sample4: 46.182.157.101 syn 6 15 up 15 2419 small 7 psh 2 2 down 2 130 small 1 com 3 10 2
    val pattern4 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} syn (\\d{1,}) \\d{1,} up (\\d{1,}) (\\d{1,}) small (\\d{1,}) psh (\\d{1,}) \\d{1,} down (\\d{1,}) (\\d{1,}) small (\\d{1,}) com (\\d{1,}) (\\d{1,}) \\d{1,}".r
    //Data Sample5: 123.127.121.245 syn 13 26 up 26 1664 small 26 psh 3 3 down 3 198 small 0 dns 3 0
    val pattern5 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} syn (\\d{1,}) \\d{1,} up (\\d{1,}) (\\d{1,}) small (\\d{1,}) psh (\\d{1,}) \\d{1,} down (\\d{1,}) (\\d{1,}) small (\\d{1,}) dns (\\d{1,}) \\d{1,}".r
    //Data Sample6: 61.135.186.42 syn 1 14 up 14 1986 small 12 com 12 5 1
    val pattern6 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} syn (\\d{1,}) \\d{1,} up (\\d{1,}) (\\d{1,}) small (\\d{1,}) com (\\d{1,}) (\\d{1,}) \\d{1,}".r
    //Data Sample7: 61.135.186.42 psh 1 14 down 14 1986 small 12 com 12 5 1
    val pattern7 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} psh (\\d{1,}) \\d{1,} down (\\d{1,}) (\\d{1,}) small (\\d{1,}) com (\\d{1,}) (\\d{1,}) \\d{1,}".r
    //Data Sample8: 123.110.142.124 syn 24 24 up 24 1536 small 24 psh 1 1 down 1 64 small 1
    val pattern8 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} syn (\\d{1,}) \\d{1,} up (\\d{1,}) (\\d{1,}) small (\\d{1,}) psh (\\d{1,}) \\d{1,} down (\\d{1,}) (\\d{1,}) small (\\d{1,})".r
    //Data Sample9: 202.121.66.121 syn 35 104 up 104 7173 small 42 psh 199 199 down 199 15216 small 106 dns 2255 0 com 3 0 14
    val pattern9 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} syn (\\d{1,}) \\d{1,} up (\\d{1,}) (\\d{1,}) small (\\d{1,}) psh (\\d{1,}) \\d{1,} down (\\d{1,}) (\\d{1,}) small (\\d{1,}) dns (\\d{1,}) \\d{1,} com (\\d{1,}) (\\d{1,}) \\d{1,}".r
    //Data Sample10: 183.232.93.106 syn 8 8 up 8 624 small 0 dns 30 0
    val pattern10 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} syn (\\d{1,}) \\d{1,} up (\\d{1,}) (\\d{1,}) small (\\d{1,}) dns (\\d{1,}) 0".r
    //Data Sample11: 114.81.254.175 psh 7 7 down 7 4487 small 0 dns 1 0
    val pattern11 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} psh (\\d{1,}) \\d{1,} down (\\d{1,}) (\\d{1,}) small (\\d{1,}) dns (\\d{1,}) 0".r
    //Data Sample12: 180.76.15.29 syn 1 192 up 192 12934 small 129 dns 1 0 com 190 36 1
    val pattern12 = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3} syn (\\d{1,}) \\d{1,} up (\\d{1,}) (\\d{1,}) small (\\d{1,}) dns (\\d{1,}) 0 com (\\d{1,}) (\\d{1,}) \\d{1,}".r

    //Match-Case
    data match {
      case pattern1(syn,up_count,up_size,up_small) => 
           {_setSYN(syn.toInt);
            _setUP(up_count,up_size,up_small);}
      case pattern2(dns) => 
           {_setDNS(dns.toInt);}
      case pattern3(psh,down_count,down_size,down_small) => 
           {_setPSH(psh.toInt);
           _setDOWN(down_count,down_size,down_small);}
      case pattern4(syn,up_count,up_size,up_small,psh,down_count,down_size,down_small,session_count,session_total) => 
           {_setSYN(syn.toInt);_setPSH(psh.toInt);
            _setUP(up_count,up_size,up_small);
            _setDOWN(down_count,down_size,down_small);
            _setSession(session_count,session_total)}
      case pattern5(syn,up_count,up_size,up_small,psh,down_count,down_size,down_small,dns) => 
           {_setSYN(syn.toInt);_setPSH(psh.toInt);
            _setUP(up_count,up_size,up_small);
            _setDOWN(down_count,down_size,down_small);
            _setDNS(dns.toInt);}               
      case pattern6(syn,up_count,up_size,up_small,session_count,session_total) => 
           {_setSYN(syn.toInt);
            _setUP(up_count,up_size,up_small);
            _setSession(session_count,session_total)}
      case pattern7(psh,down_count,down_size,down_small,session_count,session_total) => 
           {_setPSH(psh.toInt);
            _setDOWN(down_count,down_size,down_small);
            _setSession(session_count,session_total)}
      case pattern8(syn,up_count,up_size,up_small,psh,down_count,down_size,down_small) => 
           {_setSYN(syn.toInt);
            _setUP(up_count,up_size,up_small);
            _setPSH(psh.toInt);
            _setDOWN(down_count,down_size,down_small);}
      case pattern9(syn,up_count,up_size,up_small,psh,down_count,down_size,down_small,dns,session_count,session_total) => 
           {_setSYN(syn.toInt);
            _setUP(up_count,up_size,up_small);
            _setPSH(psh.toInt);_setDOWN(down_count,down_size,down_small);
            _setDNS(dns.toInt);_setSession(session_count,session_total)}
      case pattern10(syn,up_count,up_size,up_small,dns) => 
           {_setSYN(syn.toInt);
            _setUP(up_count,up_size,up_small);
            _setDNS(dns.toInt);}
      case pattern11(psh,down_count,down_size,down_small,dns) => 
           {_setPSH(psh.toInt);
            _setDOWN(down_count,down_size,down_small);
            _setDNS(dns.toInt);}
      case pattern12(syn,up_count,up_size,up_small,dns,session_count,session_total) =>
           {_setSYN(syn.toInt);
            _setUP(up_count,up_size,up_small);
            _setDNS(dns.toInt);
            _setSession(session_count,session_total);}
      case _ => println(data)
    } 
  }

}
