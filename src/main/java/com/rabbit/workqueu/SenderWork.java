package com.rabbit.workqueu;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderWork {
    private static String NAME_QUEUE = "WORK";

    public static void main(String[] args0) throws Exception{

        //primeiro criar a conexão
        //setar as informações para cria-la
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);

        try( Connection connection = factory.newConnection()) {
            //System.out.println(connection.hashCode());

            // criar um novo canal
            Channel channel = connection.createChannel();
            System.out.println(channel);

            // declarar a fila que será utilizada
            //nome da fila, exclusiva, autodelete, durable, map(args)
            channel.queueDeclare(NAME_QUEUE, false, false, false, null);

            //criar a mensagem
            String message = ".....";

            //enviar a mensagem
            int i = 0;
            while (i != 5) {
                channel.basicPublish("", NAME_QUEUE, null, message.getBytes());
                i++;
            }

            System.out.print("[x] Sent '" + message + "'");
        }
    }
}