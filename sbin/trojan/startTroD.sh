/usr/local/spark/bin/spark-submit \
--class "com.rpc.Broker" \
target/scala-2.11/simple-project_2.11-1.0.jar &

sleep 3

/usr/local/spark/bin/spark-submit \
--class "com.TrojanDBackend" \
target/scala-2.11/simple-project_2.11-1.0.jar &

sleep 10

/usr/local/spark/bin/spark-submit \
--class "com.TrojanDTest" \
target/scala-2.11/simple-project_2.11-1.0.jar \
> result.txt
