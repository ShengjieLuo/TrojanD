package com

import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.sql.SparkSession
import org.apache.spark._

object KmeansTrain{

  def main(args : Array[String]):Unit = {
    val sparkconf = new SparkConf().setAppName("KMeansTrain")
    val sc = new SparkContext(sparkconf)
    val spark = SparkSession
        .builder
        .appName(s"${this.getClass.getSimpleName}")
        .getOrCreate()
  
    //Load Data
    val dataset = spark.read.format("libsvm").load("file:///usr/local/spark/data/mllib/sample_kmeans_data.txt")

    // Trains a k-means model.
    val kmeans = new KMeans().setK(2).setSeed(1L)
    val model = kmeans.fit(dataset)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = model.computeCost(dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)
  }
}
