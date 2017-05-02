import java.io._
import scala.io.Source
import scala.util.matching.Regex

object ProduceTrain{
 
  val counter1:Int = 0
  val counter2:Int = 0  

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
      case pattern4(syn,up_count,up_size,up_small,psh,down_count,down_size,down_small,session_count,session_total) => {
        train_data =            List((session_count.toInt/session_total.toInt).toDouble,
                                    (up_size.toInt/down_size.toInt).toDouble,
                                    (down_count.toInt/down_count.toInt).toDouble,
                                    (syn.toInt/up_count.toInt).toDouble,
                                    (psh.toInt/down_count.toInt).toDouble,
                                    (up_small.toInt/up_count.toInt).toDouble)

      }
      case pattern9(syn,up_count,up_size,up_small,psh,down_count,down_size,down_small,dns,session_count,session_total) => {
        train_data =            List((session_count.toInt/session_total.toInt).toDouble,
                                    dns.toDouble,
                                    (up_size.toInt/down_size.toInt).toDouble,
                                    (down_count.toInt/down_count.toInt).toDouble,
                                    (syn.toInt/up_count.toInt).toDouble,
                                    (psh.toInt/down_count.toInt).toDouble,
                                    (up_small.toInt/up_count.toInt).toDouble)
      }
      case _ => {return train_data}
    }

    return train_data
  }

  def _saveToTrain(trainList:List[Double],writer1:PrintWriter,writer2:PrintWriter){
    var result:String = ""
    var tmpcount = 1
    if (trainList.length == 7) {
      result += counter1.toString + " "
      trainList.foreach( di => {result += tmpcount.toString + ":" + di.toString + " "; tmpcount += 1;})
      writer1.write(result+"\n")
    } else if (trainList.length == 6){
      result += counter1.toString + " "
      trainList.foreach( di => {result += tmpcount.toString + ":" + di.toString + " "; tmpcount += 1;})
      writer2.write(result+"\n")
    }

  }
  def main(ars:Array[String]):Unit = {
    val writer1 = new PrintWriter(new File("file:///usr/local/TrojanD/sample/Train1.txt"))
    val writer2 = new PrintWriter(new File("file:///usr/local/TrojanD/sample/Train2.txt"))
    val lines = Source.fromFile("file:///usr/local/TrojanD/sample/TrojanD.txt","iso-8859-1").getLines.toList
    lines.map(line => _loadFromString(line))
         .foreach(trainList => _saveToTrain(trainList,writer1,writer2))
    writer1.close()
    writer2.close()
  }
}
