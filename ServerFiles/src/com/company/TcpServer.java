package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class TcpServer {


    private final int port; // initialize in constructor
    private boolean stopServer;
    private ThreadPoolExecutor threadPool; // handle each client in a separate thread
    private IHandler requestHandler; // what is the type of clients' tasks

    public TcpServer(int port){
        this.port = port;
        this.threadPool = null;
        stopServer = false;
    }

    public void supportClients(IHandler handler) {
        this.requestHandler = handler;

        Runnable mainServerLogic = () -> {
            this.threadPool = new ThreadPoolExecutor(3,5,
                    10, TimeUnit.SECONDS, new LinkedBlockingQueue());

            try {
                ServerSocket serverSocket = new ServerSocket(this.port); // bind
                /*
                listen to incoming connection and accept if possible
                be advised: accept is a blocking call
                 */
                while(!stopServer){
                    System.out.println("Server: waiting a clients...");
                    Socket serverClientConnection = serverSocket.accept();
                    // define a task and submit to our threadPool
                    Runnable clientHandling = ()->{
                        System.out.println("Server: Handling a client");
                        try {
                            requestHandler.handle(serverClientConnection.getInputStream(),
                                    serverClientConnection.getOutputStream());
                        } catch (IOException | ClassNotFoundException ioException) {
                            ioException.printStackTrace();
                        }

                    };
                    threadPool.execute(clientHandling);
                }
                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };
        new Thread(mainServerLogic).start();
    }

    public void stop(){
        if(!stopServer){
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stopServer = true;
            threadPool.shutdown();
        }

    }

    public static void main(String[] args) {
        TcpServer webServer = new TcpServer(8010);
        webServer.supportClients(new SudokuIHandler());
//        webServer.stop(); // stop the server after 20 seconds
    }
}
