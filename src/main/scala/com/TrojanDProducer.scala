import java.util.HashMap
import org.apache.kafka.clients.producer.{ProducerConfig, KafkaProducer, ProducerRecord}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.SparkConf
import scala.io.Source
import java.util.concurrent.{Executors, ExecutorService}

object TrojanDProducer{
  
  class Message(props:HashMap[String,Object],source:String,topic:String,speed:String) extends Runnable{
    def run(){
      val producer = new KafkaProducer[String, String](props)// Send some messages
      val lines = Source.fromFile(source,"iso-8859-1").getLines.toList
      val sleeptime = speed.toInt
      for (line<-lines){
        //println(topic+": "+line)
        val message = new ProducerRecord[String, String](topic, null, line)
        producer.send(message)
        Thread.sleep(sleeptime)
      }
    }
  }

  def main(args: Array[String]) {
    val threadPool:ExecutorService=Executors.newFixedThreadPool(1)
    val Array(brokers,speed,trojanD) = args
    val sourcepool = Array(trojanD)
    val topicpool = Array("trojanD")
    val props = new HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    threadPool.execute(new Message(props,sourcepool(0),topicpool(0),speed))
  }
}
