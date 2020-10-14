package com.kafka;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaPost {
  private KafkaProducer<String, String> producer;
  private final static String TOPIC = "logs";

  public void init(){
    Properties props = new Properties();
    props.put("bootstrap.servers", "192.168.1.144:9092");
    props.put("group.id", "bigData");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("batch.size", 16384);
    props.put("linger.ms", 1);
    props.put("buffer.memory", 33554432);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    //设置分区类,根据key进行数据分区
    producer = new KafkaProducer<String, String>(props);
  }

  public static void main(String[] args) throws InterruptedException {
    KafkaPost kafkaPost = new KafkaPost();
    kafkaPost.init();
    kafkaPost.produce();
  }

  public void produce() throws InterruptedException {
    while (true) {
      String key = String.valueOf(1);
      String dataTimes = "";
      DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
      LocalDateTime now = LocalDateTime.now();

      String data = "{attributeId: 13, value: {\"uid\":102,\"tid\":0,\"cid\":0,\"sid\":0,\"tp\":\"ip\",\"fav\":0,\"ip\":\"59.108.79.80\",\"ts\":\""+now.format(ofPattern)+"\"}}";
      producer.send(new ProducerRecord<String, String>(TOPIC,key,data));
      Thread.sleep(1000);
    }
  }
}
