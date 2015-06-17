package org.elasticsearch.kafka.consumer;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerConfig {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerConfig.class);
	private Properties prop = new Properties();
	private final int kafkaFetchSizeBytesDefault = 10 * 1024 * 1024;

	// Kafka ZooKeeper's IP Address/HostName : port list
	public final String kafkaZookeeperList;
	// Zookeeper session timeout in MS
	public final int zkSessionTimeoutMs;
	// Zookeeper connection timeout in MS
	public final int zkConnectionTimeoutMs;
	// Zookeeper number of retries when creating a curator client
	public final int zkCuratorRetryTimes;
	// Zookeeper: time in ms between re-tries when creating a Curator
	public final int zkCuratorRetryDelayMs;
	// Full class path and name for the concrete message handler class 
	public final String messageHandlerClass;
	// Full class name of a custom IndexHandler implementation class
	public final String indexHandlerClass;
	// Kafka Broker's IP Address/HostName : port list
	public final String kafkaBrokersList;
	// Kafka Topic from which the message has to be processed
	public final String topic;
	// Partition in the Kafka Topic from which the message has to be processed
	public final short partition;
	// Option from where the message fetching should happen in Kafka
	// Values can be: CUSTOM/EARLIEST/LATEST/RESTART.
	// If 'CUSTOM' is set, then 'startOffset' has to be set as an int value
	public String startOffsetFrom;
	// int value of the offset from where the message processing should happen
	public final int startOffset;
	// Name of the Kafka Consumer Group
	public final String consumerGroupName;
	// SimpleConsumer socket bufferSize
	public final int kafkaSimpleConsumerBufferSizeBytes;
	// SimpleConsumer socket timeout in MS
	public final int kafkaSimpleConsumerSocketTimeoutMs;
	// FetchRequest's minBytes value
	public final int kafkaFetchSizeMinBytes;
	// Preferred Message Encoding to process the message before posting it to ElasticSearch
	public final String messageEncoding;
	// Name of the ElasticSearch Cluster
	public final String esClusterName;
	// Name of the ElasticSearch Host Port List
	public final String esHostPortList;
	// IndexName in ElasticSearch to which the processed Message has to be posted
	public final String esIndex;
	// IndexType in ElasticSearch to which the processed Message has to be posted
	public final String esIndexType;
	// flag to enable/disable performance metrics reporting
	public boolean isPerfReportingEnabled;
	
	// Log property file for the consumer instance
	public final String logPropertyFile;

	// determines whether the consumer will post to ElasticSearch or not:
	// If set to true, the consumer will read events from Kafka and transform them,
	// but will not post to ElasticSearch
	public final String isDryRun;

	// Wait time in seconds between consumer job rounds
	public final int consumerSleepBetweenFetchsMs;
	//wait time before stop Consumer Job regardless it finished 
	public final int timeLimitToStopConsumerJob = 10;
	
	public String getStartOffsetFrom() {
		return startOffsetFrom;
	}

	public void setStartOffsetFrom(String startOffsetFrom) {
		this.startOffsetFrom = startOffsetFrom;
	}

	public ConsumerConfig(String configFile) throws Exception {
		
	try {
			logger.info("configFile : " + configFile);
			prop.load(new FileInputStream(configFile));
			logger.info("Properties : " + prop);
		} catch (Exception e) {
			logger.error("Error reading/loading configFile: " + e.getMessage(), e);
			throw e; 
		}

		kafkaZookeeperList = (String) prop.getProperty("kafkaZookeeperList", "localhost:2181");
		zkSessionTimeoutMs = Integer.parseInt(prop.getProperty("zkSessionTimeoutMs", "10000"));
		zkConnectionTimeoutMs = Integer.parseInt(prop.getProperty("zkConnectionTimeoutMs", "15000"));
		zkCuratorRetryTimes = Integer.parseInt(prop.getProperty("zkCuratorRetryTimes", "3"));
		zkCuratorRetryDelayMs = Integer.parseInt(prop.getProperty("zkCuratorRetryDelayMs", "2000"));
		
		messageHandlerClass = prop.getProperty("messageHandlerClass",
						"org.elasticsearch.kafka.consumer.messageHandlers.RawMessageStringHandler");
		indexHandlerClass = prop.getProperty("indexHandlerClass",
						"org.elasticsearch.kafka.consumer.BasicIndexHandler");
		kafkaBrokersList = prop.getProperty("kafkaBrokersList", "localhost:9092");
		topic = prop.getProperty("topic", "");
		partition = Short.parseShort(prop.getProperty("partition", "0"));
		startOffsetFrom = prop.getProperty("startOffsetFrom", "0");
		startOffset = Integer.parseInt(prop.getProperty("startOffset", "LATEST"));
		consumerGroupName = prop.getProperty("consumerGroupName", "ESKafkaConsumerClient");
		kafkaFetchSizeMinBytes = Integer.parseInt(prop.getProperty(
				"kafkaFetchSizeMinBytes",
				String.valueOf(kafkaFetchSizeBytesDefault)));
		kafkaSimpleConsumerBufferSizeBytes = Integer.parseInt(prop.getProperty(
				"kafkaSimpleConsumerBufferSizeBytes",
				String.valueOf(kafkaFetchSizeBytesDefault)));
		kafkaSimpleConsumerSocketTimeoutMs = Integer.parseInt(prop.getProperty(
				"kafkaSimpleConsumerSocketTimeoutMs", "10000"));
		messageEncoding = prop.getProperty("messageEncoding", "UTF-8");

		esClusterName = prop.getProperty("esClusterName", "");
		esHostPortList = prop.getProperty("esHostPortList", "localhost:9300");
		esIndex = prop.getProperty("esIndex", "kafkaConsumerIndex");
		esIndexType = prop.getProperty("esIndexType", "kafka");
		isPerfReportingEnabled=Boolean.getBoolean(prop.getProperty(
				"isPerfReportingEnabled", "false"));
		logPropertyFile = prop.getProperty("logPropertyFile",
				"log4j.properties");
		isDryRun = prop.getProperty("isDryRun", "false");
		consumerSleepBetweenFetchsMs = Integer.parseInt(prop.getProperty(
				"consumerSleepBetweenFetchsMs", "25"));

		logger.info("Config reading complete !");
	}

	public boolean isPerfReportingEnabled() {
		return isPerfReportingEnabled;
	}

}
