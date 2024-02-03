package com.example.newmapsapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainLayoutActivity extends AppCompatActivity {

    private MainLayoutBinding binding;
    private PathBuilderViewModel builderViewModel;
    private StartLocationViewModel startViewModel;
    private EndLocationViewModel endViewModel;
    private RouteViewModel routeViewModel;
    private IsStartLocation isStartLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRoutes();
        createViewModels();
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void createViewModels() {
        isStartLocation = new ViewModelProvider(this).get(IsStartLocation.class);
        isStartLocation.setBool(false);
        routeViewModel = new ViewModelProvider(this).get(RouteViewModel.class);
        routeViewModel.setRoute(new Route(new Location[0]));
        startViewModel = new ViewModelProvider(this).get(StartLocationViewModel.class);
        startViewModel.setLocation(Location.MINNEAPOLIS);
        endViewModel = new ViewModelProvider(this).get(EndLocationViewModel.class);
        endViewModel.setLocation(Location.SAINT_PAUL);
        builderViewModel = new ViewModelProvider(this).get(PathBuilderViewModel.class);
        builderViewModel.setBuilder(new PathBuilder(TransferPointBuilder.getTransferPoints(ExampleClasses.getRoutes())));
    }

    private void getRoutes() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        RouteCollector collector = new RouteCollector(mainThreadHandler, executorService);
        collector.getRoutesFromServer(new Callback<Route[]>() {
            @Override
            public void onComplete(Route[] result) {
                Log.d("ThisIsATag", "setting builder");
                builderViewModel.setBuilder(new PathBuilder(TransferPointBuilder.getTransferPoints(result)));
            }
        });
    }

    interface Callback<T> {
        void onComplete(T result);
    }

    public class RouteCollector {
        private final Executor collectionExecutor;
        private final Handler resultHandler;

        public RouteCollector(Handler handler, Executor readerExecutor) {
            resultHandler = handler;
            collectionExecutor = readerExecutor;
        }

        public void getRoutesFromServer(Callback<Route[]> callback) {
            collectionExecutor.execute(new ServerReader(callback));
        }

        private void postRoutesBackToMain(Route[] result, Callback<Route[]> callback) {
            resultHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("ThisIsATag", "postingRoutes");
                    callback.onComplete(result);
                }
            });
        }

        private class ServerReader implements Runnable {
            private int bytesCounted = 0;
            private String readInfo = "";
            private InputStream input;
            private Callback<Route[]> resultToCallBack;

            public ServerReader(Callback<Route[]> callback) {
                resultToCallBack = callback;
            }

            @Override
            public void run() {
                ArrayList<Route> routes = new ArrayList<>();
                try {
                    Socket socket = new Socket("192.168.0.50", 6013);
                    Log.d("ThisIsATag", "connected");
                    input = socket.getInputStream();
                    while(bytesCounted != -1) {
                        routes.add(new Route(getNextRouteString()));
                    }
                    routes.remove(routes.size()-1);
                    postRoutesBackToMain(routes.toArray(new Route[0]), resultToCallBack);
                } catch(IOException e) {
                    Log.d("ThisIsATag", e.toString());
                }
            }

            private String getNextRouteString() throws IOException {
                byte[] buffer = new byte[100];
                // Convert to a do - while at some point.
                while((bytesCounted = input.read(buffer)) != -1) {
                    readInfo += new String(buffer, StandardCharsets.UTF_8).substring(0,bytesCounted);
                    if(readInfo.contains(";")) {
                        int endIndex = readInfo.indexOf(";");
                        String temp = readInfo.substring(0,endIndex);
                        if(endIndex != readInfo.length()) {
                            readInfo = readInfo.substring(readInfo.indexOf(";")+1);
                        }
                        return temp;
                    }
                }
                return "";
            }
        }
    }
}
