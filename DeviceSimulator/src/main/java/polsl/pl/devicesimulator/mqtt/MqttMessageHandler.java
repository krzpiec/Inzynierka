package polsl.pl.devicesimulator.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageHandler {

    @Autowired
    MqttPublisher mqttPublisher;

public void resolveMessageAndSend(String topic, String payLoad){
    String[] topicSegments = topic.split("/");
    String newTopic = topicSegments[0] + "/" + topicSegments[1] + "/receive";
    System.out.println(newTopic);
    mqttPublisher.publish(newTopic, "12");
}
}
