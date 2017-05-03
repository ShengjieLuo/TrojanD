package com

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.sql.SparkSession
import org.apache.spark._
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD
import java.io._

object KmeansMLlibTrain{

  def main(args : Array[String]):Unit = {
    val sparkconf = new SparkConf().setAppName("KMeansTrain")
    val sc = new SparkContext(sparkconf)

    val data = sc.textFile("file:///usr/local/TrojanD/sample/MLlib.txt")
    val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()

    val model = KMeans.train(parsedData, 15, 100)
    val WSSSE = model.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSSE)
    model.save(sc, "file:///usr/local/TrojanD/KMeansModel")

    /*// Shows the result.
    val metaDataWriter = new PrintWriter(new File("../sample/MetaData.txt")) 
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)
    println(model.summary.k)
    val accum1 = sc.accumulator(0)
    val accum2 = sc.accumulator(0) 
    model.summary.clusterSizes.foreach(x => accum1 += x.toInt)
    model.summary.clusterSizes.foreach(csize => { 
      println("Kind: " + accum2.value + " Number: "+csize)
      if (csize < 0.01 * accum1.value) {metaDataWriter.write("Kind: " + accum2.value + " Number: "+csize)}
      accum2 += 1
    })
    //model.summary.predictions.foreach(println)
    metaDataWriter.close()*/
  }
}
