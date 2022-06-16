package com.rabbit.Routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiverRT {
    private static String BINDKEY_NAME = "routingKeyTest";
    private static String NAME_EXCHANGE = "directExchange";

    public static void main(String[] args0) throws Exception{
        //primeiro criar a conexão
        //setar as informações para cria-la
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        //System.out.println(connection.hashCode());

        // criar um novo canal
        Channel channel = connection.createChannel();
        System.out.println(channel);

        //o servidor irá determinar um nome randomico para esta fila
        //consequentemente ela será temporária
        String nameQueue = channel.queueDeclare().getQueue();

        //declaração da exchange
        channel.exchangeDeclare(NAME_EXCHANGE,"direct");
        channel.queueBind(nameQueue, NAME_EXCHANGE, BINDKEY_NAME);

        DeliverCallback deliverycallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(),"UTF-8");
            System.out.println("[*] Received message: '"+ message + "'");
        };

        channel.basicConsume(nameQueue, true, deliverycallback, ConsumerTag->{});
    }
}
