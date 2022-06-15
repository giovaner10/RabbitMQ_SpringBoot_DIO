package com.rabbit.workqueu;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveWork {
    private static String NAME_QUEUE = "WORK";

    private static void doWork(String task) throws InterruptedException {
        for (char ch:task.toCharArray()){
            if(ch == '.') Thread.sleep(1000);
        }
    };

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

        // declarar a fila que será utilizada
        //nome da fila, exclusiva, autodelete, durable, map(args)
        channel.queueDeclare(NAME_QUEUE, false, false, false, null);

        DeliverCallback deliverycallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(),"UTF-8");
            System.out.println("[*] Received message: '"+ message + "'");
            try{
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("[x] Done");
            }
        };

        //enviar a mensagem
        boolean Autoack = true; //ack is false
        channel.basicConsume(NAME_QUEUE, Autoack, deliverycallback, ConsumerTag->{});
    }
}