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

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [*] Received '" + message + "'");
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
        });

        String curTopic;
        String prevTopic = null;
        System.out.println("Welcome to IT Blog");
        System.out.println("Available commands are: \n1. To set topic -> set_topic <topic>\n2. To change topic -> change_topic <new topic>");
        System.out.println("Available topics are:\n1. php\n2. java\n3. c++");

        while (true) {
            String input = reader.readLine();
            String[] tokens = input.split("\\s+", 2);
            String command = tokens[0];
            curTopic = tokens[1];

            if (prevTopic == null) {
                prevTopic = curTopic;
            }

            switch (command) {
                case "set_topic":
                    channel.queueBind(queueName, EXCHANGE_NAME, curTopic);
                    break;
                case "change_topic":
                    channel.queueUnbind(queueName, EXCHANGE_NAME, curTopic);
                    prevTopic = curTopic;
                    channel.queueBind(queueName, EXCHANGE_NAME, curTopic);
                    break;
            }
        }
    }
}
