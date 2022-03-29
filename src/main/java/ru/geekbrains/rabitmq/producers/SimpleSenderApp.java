package ru.geekbrains.rabitmq.producers;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SimpleSenderApp {
    private static final String QUEUE_NAME = "simpleApp";
    private static final String EXCHANGER_NAME = "simpleApp_exchanger";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGER_NAME, "java");

            String message = "Hello World!";
            channel.basicPublish(EXCHANGER_NAME, "java", null, message.getBytes());
            System.out.println(" [*] Sent '" + message + "'");
        }
    }
}
