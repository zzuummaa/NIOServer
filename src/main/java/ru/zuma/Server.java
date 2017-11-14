package ru.zuma;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Scanner;

import static java.nio.channels.SelectionKey.*;

public class Server {
    private String addr = "http://test-testjavamaven.7e14.starter-us-west-2.openshiftapps.com";
    private int port = 8080;

    public void run() throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(addr, port));
        channel.register(selector, OP_ACCEPT);

        new Thread( ()->{
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.nextLine().equals("q")) {
                    System.exit(0);
                }
            }
        } ).start();

        while (true) {
            selector.select();

            for (SelectionKey selectionKey: selector.selectedKeys()) {
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel clientChannel = serverSocketChannel.accept();
                    clientChannel.configureBlocking(false);
                    //clientChannel.register(selector, OP_READ);
                    System.out.println("New connection: " + clientChannel.getRemoteAddress());
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().run();
    }
}
