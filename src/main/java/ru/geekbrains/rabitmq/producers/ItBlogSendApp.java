package ru.geekbrains.rabitmq.producers;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class ItBlogSendApp {
    private static final String EXCHANGE_NAME = "DoubleDirect";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        while (true) {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));

            System.out.println("Welcome to IT Blog");
            System.out.println("Send your message in form of: <topic> <message>");
            System.out.println("Type quit() to end session");
            System.out.print("--> ");
            String[] tokens = reader.readLine().split("\\s", 2);
            if (tokens[0].equals("quit()")) {
                return;
            }
            String topic = tokens[0];
            String message = tokens[1];

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

                channel.basicPublish(EXCHANGE_NAME, topic, null, message.getBytes("UTF-8"));
            }
        }

    }
}
