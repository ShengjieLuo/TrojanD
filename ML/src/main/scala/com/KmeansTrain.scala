package com

import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.sql.SparkSession
import org.apache.spark._
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD
import java.io._

object KmeansTrain{

  def main(args : Array[String]):Unit = {
    val sparkconf = new SparkConf().setAppName("KMeansTrain")
    val sc = new SparkContext(sparkconf)
    val spark = SparkSession
        .builder
        .appName(s"${this.getClass.getSimpleName}")
        .getOrCreate()
  
    //Load Data
    val dataset = spark.read.format("libsvm").load("file:///usr/local/TrojanD/sample/Train.txt")

    // Trains a k-means model.
    //Train1 Parameter: 
    //val kmeans = new KMeans().setK(15).setSeed(1L).setMaxIter(100)
    //Train2 Parameter: 
    val kmeans = new KMeans().setK(15)
    
    val model = kmeans.fit(dataset)
    model.save("file:///usr/local/TrojanD/Model")

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = model.computeCost(dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
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
    metaDataWriter.close()
  }
}
