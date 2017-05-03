package com;

import java.io._
import scala.io.Source
import scala.util.matching.Regex
import org.apache.spark.rdd.RDD

class Summer(sname:String){

  val name:String = sname
  var value:Double = 0

  def addValue(va:Double){ value += va}
  def addValue(va:Int) {value += va}
  def getValue():Double =  value

  def print():String = {
    val str:String = "Item: " + name + " Sum: "+ value.toString
    println(str)
    str
  }
}

class Statistic(){

  var counter:Int = 0
  var summerList:List[Summer] = List()
  var meanList:Array[Double] = Array(0,0,0,0,0,0,0)
  var devList:Array[Double] = Array(0,0,0,0,0,0,0)
  summerListInit()

  def summerListInit(){
    summerList = summerList :+ new Summer("Session")
    summerList = summerList :+ new Summer("dns")
    summerList = summerList :+ new Summer("Size-Ratio")
    summerList = summerList :+ new Summer("Count-Ratio")
    summerList = summerList :+ new Summer("SYN-Ratio")
    summerList = summerList :+ new Summer("PSH-Ratio")
    summerList = summerList :+ new Summer("Small-Ratio")
  }

  def addValue(index:Int,va:Double){ summerList.apply(index).addValue(va)}
  def addValue(index:Int,va:Int){ summerList.apply(index).addValue(va)}
  def addDev(index:Int,va:Double){ devList(index) = devList(index) + (va-meanList(index))*(va-meanList(index))}
  def addCount(){counter += 1}

  def updateMean():Array[Double] = {
    for (i <- 0 to 6) {meanList(i) = summerList.apply(i).getValue()/counter}
    return meanList
  }

  def updateDev():Array[Double] = {
    for (i <- 0 to 6) {devList(i) = math.sqrt(devList(i)/counter)}
    return devList
  }
  def print(){
    summerList.foreach(_.print())
    meanList.foreach(println)
    devList.foreach(println)
  }
}

object ProduceTrain{
 
  var stat= new Statistic()
  var counter = 1

  def _loadFromString(data:String):List[Double] = {

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
    var train_data:List[Double] = List()
    data match {
      case pattern9(syn,up_count,up_size,up_small,psh,down_count,down_size,down_small,dns,session_count,session_total) => {
        if (session_count.toInt==0) {return train_data}
        println("Pattern9",syn,up_count,up_size,up_small,psh,down_count,down_size,down_small,dns,session_count,session_total)
        train_data =            List((session_total.toDouble/session_count.toInt).toDouble,
                                    dns.toDouble,
                                    (up_size.toDouble/down_size.toLong).toDouble,
                                    (up_count.toDouble/down_count.toInt).toDouble,
                                    (syn.toDouble/up_count.toInt).toDouble,
                                    (psh.toDouble/down_count.toInt).toDouble,
                                    (up_small.toDouble/up_count.toInt).toDouble)
      }
      case pattern4(syn,up_count,up_size,up_small,psh,down_count,down_size,down_small,session_count,session_total) => {
        if (session_count.toInt==0) {return train_data}
	val dns:Double = 0
        println("Pattern4",syn,up_count,up_size,up_small,psh,down_count,down_size,down_small,session_count,session_total)
        train_data =            List((session_total.toDouble/session_count.toInt).toDouble,
                                    dns,
                                    (up_size.toDouble/down_size.toLong).toDouble,
                                    (up_count.toDouble/down_count.toInt).toDouble,
                                    (syn.toDouble/up_count.toInt).toDouble,
                                    (psh.toDouble/down_count.toInt).toDouble,
                                    (up_small.toDouble/up_count.toInt).toDouble)

      }
      case _ => {return train_data}
    }

    return train_data
  }

  def _stat(data:List[List[Double]]){
    // Stat1 : Calculate the sum and count 
    data.foreach( row =>{
      stat.addCount()
      var i = 0
      row.foreach(di =>{stat.addValue(i,di);i+=1;})
    }) 
    // Stat2 : Calculate the mean average
    stat.updateMean()
    // Sata3 : Calculate the standard dev
    data.foreach( row => {
      var i = 0
      row.foreach(di =>{stat.addDev(i,di); i += 1;})
    })
    stat.updateDev()
    stat.print()
  }

  def _saveToTrain(trainList:List[Double],writer:PrintWriter){
    var result:String = ""
    var tmpcount = 1
    result += counter.toString + " "
    trainList.foreach( di => {
      var tmp:Double = 0
      if (stat.devList(tmpcount-1) > 0.000001){
        tmp = (di - stat.meanList(tmpcount-1))/stat.devList(tmpcount-1)
      }
      result += tmpcount.toString + ":" + tmp.toString + " ";
      tmpcount += 1;
    })
    writer.write(result+"\n")
    counter += 1
  }

  def main(ars:Array[String]):Unit = {
    val writer = new PrintWriter(new File("../sample/Train.txt"))
    val lines = Source.fromFile("../sample/TrojanD.txt","iso-8859-1").getLines.toList
    var data:List[List[Double]] = lines.map(line => _loadFromString(line)).filter(_.size == 7)
    _stat(data)
    stat.print()
    data.foreach(trainList => _saveToTrain(trainList,writer))
    writer.close()
  }
}
