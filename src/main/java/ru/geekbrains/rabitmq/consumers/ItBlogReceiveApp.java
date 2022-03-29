package ru.geekbrains.rabitmq.consumers;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class ItBlogReceiveApp {
    //    private final static String QUEUE_NAME = "itBlog";
    private static final String EXCHANGE_NAME = "DoubleDirect";

    public static void main(String[] args) throws IOException, TimeoutException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        String topic;
        System.out.println("Welcome to IT Blog");
        System.out.println("Available commands are: \n1. To set topic -> set_topic <topic>\n2. To change topic -> change_topic <new topic>");
        System.out.println("Available topics are:\n1. php\n2. java\n3. c++");

        while (true) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String input = reader.readLine();
            topic = input.split("\\s+", 2)[1];

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, topic);

            System.out.println(" [*] Waiting for Messages with topic: " + topic);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [*] Received '" + message + "'");
            };
            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
            });


        }


    }
}
