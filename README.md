# Welcome to the kafka-elasticsearch-standalone-consumer wiki!

## Illustration of kafka-elasticsearch-standalone-consumer usage

### The consumer is positioned in the middle.

![](https://raw.githubusercontent.com/reachkrishnaraj/kafka-elasticsearch-standalone-consumer/master/img/Kafka_ES_Illustration_New.png)


# Introduction

### **Kafka Standalone Consumer will read the messages from Kafka, processes and index them in ElasticSearch.**

### **Easily Scaleable & Extendable !**

### _As described in the illustration above, here is how the StandAlone Consumer works:_

* Kafka has a topic named, say `Topic_1`

* Lets say, `Topic_1` has 5 partitions.

* In the configuration file, kafka-es-indexer.properties, set firstPartition=0 and lastPartition=4 properties 

* start the standalone indexer application as described below 

* there will be 5 threads started, one for each consumer from each of the partitions

* when a new partition is added to the kafka topic - configuration has to be updated and the indexer application has to be restarted


# How to use ? 

### Running as a standard Jar 

**1. Download the code into a `$CONSUMER_HOME` dir.

**2. cp `$CONSUMER_HOME`/src/main/resources/kafkaESConsumer.properties.template /your/absolute/path/kafkaESConsumer.properties file - update all relevant properties as explained in the comments

**3. cp `$CONSUMER_HOME`/src/main/resources/logback.xml.template /your/absolute/path/logback.xml

 specify directory you want to store logs in:
	<property name="LOG_DIR" value="/tmp"/>
	
 adjust values of max sizes and number of log files as needed

**4. build/create the app jar:

		cd $CONSUMER_HOME
     	mvn clean package
     	
	The kafka-es-consumer-0.2.jar will be created in the $CONSUMER_HOME/bin, with all dependencies included into the JAR

**5. run the app [use JDK1.8] :  

		java -Dlogback.configurationFile=/your/absolute/path/logback.xml -jar $CONSUMER_HOME/bin/kafka-es-consumer-0.2.jar /your/absolute/path/kafkaESConsumer.properties

 
# Versions:

### Kafka Version: 0.8.2.1

### ElasticSearch: > 1.5.1

### Scala Version for Kafka Build: 2.10.0

# Configuring the Consumer Instance:

The details of each config property can be seen in the template file (below)

[Config File with details about each property](https://github.com/ppine7/kafka-elasticsearch-standalone-consumer/blob/master/src/main/resources/kafka-es-indexer.properties.template)

# Message Handler Class

*  `org.elasticsearch.kafka.consumer.MessageHandler` is an Abstract class that has most of the functionality of reading data from Kafka and batch-indexing into ElasticSearch already implemented. It has one abstract method, `transformMessage()`, that can be overwritten in the concrete sub-classes to customize message transformation before posting into ES

* `org.elasticsearch.kafka.consumer.messageHandlers.RawMessageStringHandler` is a simple concrete sub-class of the MessageHAndler that sends messages into ES with no additional transformation, as is, in the 'UTF-8' format

* Usually, its effective to Index the message in JSON format in ElasticSearch. This can be done using a Mapper Class and transforming the message from Kafka by overriding/implementing the `transformMessage()` method. An example can be found here: `org.elasticsearch.kafka.consumer.messageHandlers.AccessLogMessageHandler`

* _**Do remember to set the newly created message handler class in the `messageHandlerClass` config property of the consumer instance.**_

# IndexHandler Interface and basic implementation

*  `org.elasticsearch.kafka.consumer.IndexHandler` is an interface that defines two methods: getIndexName(params) and getIndexType(params). 

* `org.elasticsearch.kafka.consumer.BasicIndexHandler` is a simple imlementation of this interface that returnes indexName and indexType values as configured in the kafkaESConsumer.properties file. 

* one might want to create a custom implementation of IndexHandler if, for example, index name and type are not static for all incoming messages but depend on the event data - for example customerId, orderId, etc. In that case, pass all info that is required to perform that custom index determination logic as a Map of parameters into the getIndexName(params) and getIndexType(params) methods (or pass NULL if no such data is required)

* _**Do remember to set the index handler class in the `indexHandlerClass` property in the kafkaESConsumer.properties file. By default, BasicIndexHandler is used**_

# License

kafka-elasticsearch-standalone-consumer

	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at

	     http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing,
	software distributed under the License is distributed on an
	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
	KIND, either express or implied.  See the License for the
	specific language governing permissions and limitations
	under the License.

# Contributors

 - [Krishna Raj](https://github.com/reachkrishnaraj)
 - [Chandrasekar Ramalingam](https://github.com/cramal1)
 - [Marina Popova](https://github.com/ppine7)
 - [Dhyan ](https://github.com/dhyan-yottaa)
