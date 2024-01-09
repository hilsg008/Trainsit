package com.example.newmapsapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.builder.PathBuilder;
import com.example.newmapsapp.builder.TransferPointBuilder;
import com.example.newmapsapp.databinding.MainLayoutBinding;
import com.example.newmapsapp.viewmodel.EndLocationViewModel;
import com.example.newmapsapp.viewmodel.IsStartLocation;
import com.example.newmapsapp.viewmodel.PathBuilderViewModel;
import com.example.newmapsapp.viewmodel.RouteViewModel;
import com.example.newmapsapp.viewmodel.StartLocationViewModel;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainLayoutActivity extends AppCompatActivity {

    private MainLayoutBinding binding;
    private PathBuilderViewModel builderViewModel;
    private StartLocationViewModel startViewModel;
    private EndLocationViewModel endViewModel;
    private RouteViewModel routeViewModel;
    private IsStartLocation isStartLocation;
    private CronetEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewModels();
        testServerConnectionOnPhone();
        grabMTInformation();
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void createViewModels() {
        isStartLocation = new ViewModelProvider(this).get(IsStartLocation.class);
        isStartLocation.setBool(false);
        routeViewModel = new ViewModelProvider(this).get(RouteViewModel.class);
        routeViewModel.setRoute(new Route(new Location[0]));
        builderViewModel = new ViewModelProvider(this).get(PathBuilderViewModel.class);
        builderViewModel.setBuilder(new PathBuilder(TransferPointBuilder.getTransferPoints(ExampleClasses.getRoutes())));
        startViewModel = new ViewModelProvider(this).get(StartLocationViewModel.class);
        startViewModel.setLocation(Location.MINNEAPOLIS);
        endViewModel = new ViewModelProvider(this).get(EndLocationViewModel.class);
        endViewModel.setLocation(Location.SAINT_PAUL);
    }

    private void grabMTInformation() {
        engine = new CronetEngine.Builder(getApplicationContext()).build();
        Executor executor = Executors.newSingleThreadExecutor();
        UrlRequest request = engine.newUrlRequestBuilder("https://svc.metrotransit.org/nextrip/agencies", getCallback(), executor).build();
        request.start();
    }

    private UrlRequest.Callback getCallback() {
        return new UrlRequest.Callback() {

            @Override
            public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
                request.followRedirect();
            }

            @Override
            public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
                int status = info.getHttpStatusCode();
                if(status == 200) {
                    ByteBuffer b = ByteBuffer.allocateDirect(102400);
                    request.read(b);
                    String result = new String(b.array(), StandardCharsets.UTF_8).replaceAll("\0", "");
                } else if(status == 400) {
                    Log.d("ThisIsATag", "Bad Request.");
                } else if(status == 500) {
                    Log.d("ThisIsATag", "Server Error.");
                }
            }

            @Override
            public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
                // You should keep reading the request until there's no more data.
                byteBuffer.clear();
                request.read(byteBuffer);
            }

            @Override
            public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            }

            @Override
            public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
            }

            @Override
            public void onCanceled(UrlRequest request, UrlResponseInfo info) {
            }
        };
    }

    private void testServerConnectionOnPhone() {
        SocketManager socketManager = new SocketManager();
        socketManager.createSocket(new SocketManager.SocketListener() {
            @Override
            public void onSocketCreated(Socket socket) {
                // Handle the created socket
                if (socket != null) {
                    Client reader = new ServerReader(socket);
                    Client writer = new ServerWriter(socket);

                    //Creates threads for connections
                    Thread firstThread = new Thread(reader);
                    Thread secondThread = new Thread(writer);

                    //Starts threads
                    firstThread.start();
                    secondThread.start();
                    try {
                        firstThread.join();
                        secondThread.join();
                    }
                    catch (InterruptedException e) {
                        Log.d("ThisIsATag", "InterruptedException");
                    }

                } else {
                    Log.d("ThisIsATag", "SocketCreationFailure");
                }
            }
        });
    }

    public class Client implements Runnable {
        InputStream input;
        OutputStream output;
        Socket socket;

        public void run() {
            try {
                int inputInt;
                while ((inputInt = input.read()) != -1) {
                    output.write(inputInt);
                }
                output.flush();
            } catch (IOException ioe) {
                System.out.println("Threw IOE: ");
                System.out.println(ioe);
            }

        }
    }

    public class ServerWriter extends Client {
        public ServerWriter(Socket client) {
            try {
                socket = client;
                input = new ByteArrayInputStream("ThisIsATestString".getBytes(StandardCharsets.UTF_8));
                output = client.getOutputStream();
            } catch (IOException ioe) {
                System.out.println("Threw IOE: ");
                System.out.println(ioe);
            }

        }

        @Override
        public void run() {
            super.run();
            try {
                socket.shutdownOutput();
            } catch (IOException ioe) {
                System.out.println("Threw IOE: ");
                System.out.println(ioe);
            }
        }
    }

    /*
     * Grabbing the server's output uses
     * InputStream as its input
     * System.out as its output
     */
    public class ServerReader extends Client {
        ByteArrayOutputStream o = new ByteArrayOutputStream(17);
        public ServerReader(Socket client) {
            try {
                socket = client;
                input = client.getInputStream();
                output = o;
            } catch (IOException ioe) {
                System.out.println("Threw IOE: ");
                System.out.println(ioe);
            }

        }

        @Override
        public void run() {
            super.run();
            try {
                socket.close();
                Log.d("ThisIsATag", new String(o.toByteArray(), StandardCharsets.UTF_8));
            } catch (IOException ioe) {
                System.out.println("Threw IOE: ");
                System.out.println(ioe);
            }
        }
    }


}
