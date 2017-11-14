package ru.zuma;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static java.nio.channels.SelectionKey.OP_CONNECT;
import static java.nio.channels.SelectionKey.OP_WRITE;

public class Client {
    private String addr = "http://test-testjavamaven.7e14.starter-us-west-2.openshiftapps.com/";
    private int port = 8080;

    public void run() throws IOException {
        Selector selector = Selector.open();

        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.register(selector, OP_CONNECT);
        channel.connect(new InetSocketAddress(port));

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
                if (selectionKey.isConnectable()) {
                    channel.finishConnect();
                    selectionKey.interestOps(OP_WRITE);
                    System.out.println("Connection success: " + channel.getLocalAddress());
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Client().run();
    }
}
