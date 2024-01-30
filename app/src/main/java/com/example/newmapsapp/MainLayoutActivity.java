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

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainLayoutActivity extends AppCompatActivity {

    private MainLayoutBinding binding;
    private PathBuilderViewModel builderViewModel;
    private StartLocationViewModel startViewModel;
    private EndLocationViewModel endViewModel;
    private RouteViewModel routeViewModel;
    private IsStartLocation isStartLocation;
    private Thread routeGrabber;
    private Route[] routesFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testServerConnectionOnPhone();
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

    private void testServerConnectionOnPhone() {
        routeGrabber = new Thread(new ServerReader());
        routeGrabber.start();
    }

    public class ServerReader implements Runnable {
        private int bytesCounted = 0;
        private String readInfo = "";
        private InputStream input;

        @Override
        public void run() {
            ArrayList<Route> routes = new ArrayList<>();
            try {
                Socket socket = new Socket("192.168.0.50", 6013);
                input = socket.getInputStream();
                while(bytesCounted != -1) {
                    routes.add(new Route(getNextRouteString()));
                }
                routesFromServer = routes.toArray(new Route[0]);
            } catch(IOException e) {
                Log.d("ThisIsATag", e.toString());
            }
            for(Route r: routesFromServer) {
                Log.d("ThisIsATag", r.toString());
            }
        }

        private String getNextRouteString() throws IOException {
            byte[] buffer = new byte[100];
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
