package com.example.newmapsapp;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SocketManager {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final int portNumber = 6013;
    private String server = "192.168.0.12";

    public void createSocket(final SocketListener listener) {
        Callable<Socket> socketCallable = new Callable<Socket>() {
            @Override
            public Socket call() throws IOException {
                // Create the socket
                Socket socket = new Socket(server, portNumber);
                return socket;
            }
        };

        Future<Socket> future = executorService.submit(socketCallable);

        executorService.execute(() -> {
            try {
                Socket socket = future.get(); // This will block until the socket is created
                if (listener != null) {
                    listener.onSocketCreated(socket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public interface SocketListener {
        void onSocketCreated(Socket socket);
    }
}

