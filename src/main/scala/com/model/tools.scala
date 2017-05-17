package com.model

import scala.collection.JavaConverters._

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

  def setValue(values:java.util.List[java.lang.Double]) = {
    for (i <- 0 to values.size()-1 ){ addValue( values.get(i).doubleValue() ) }
  }

  def getValue(option:String):java.util.List[java.lang.Double] = {
    var result:java.util.List[java.lang.Double] = null
    if (option=="java") {result = values.map(new java.lang.Double(_)).toBuffer.asJava}
    result
  }

}

