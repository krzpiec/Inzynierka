package polsl.pl.devicesimulator.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
public class MqttSubscriber {

    @Autowired
    MqttMessageHandler mqttMessageHandler;



    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883", "Simulator",
                        "topic1");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        addTestTopics(adapter);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                 mqttMessageHandler.resolveMessageAndSend(message.getHeaders().get("mqtt_receivedTopic").toString(), message.getPayload().toString());
            }

        };
    }


    private void addTestTopics(MqttPahoMessageDrivenChannelAdapter adapter){
        adapter.addTopic("00:00:00:00:02:00/1/get");
        adapter.addTopic("00:00:00:00:00:00/0/get");
        adapter.addTopic("00:0A:E6:3E:FD:E1/0/get");
        adapter.addTopic("00:00:00:00:01:00/1/get");
        adapter.addTopic("00:00:00:00:00:03/0/get");
    }
    }

