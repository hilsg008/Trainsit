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
            private static final String TAG = "MyUrlRequestCallback";

            @Override
            public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
                Log.i(TAG, "onRedirectReceived method called.");
                request.followRedirect();
            }

            @Override
            public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
                Log.i(TAG, "onResponseStarted method called.");
                int status = info.getHttpStatusCode();
                if(status == 200) {
                    ByteBuffer b = ByteBuffer.allocateDirect(102400);
                    request.read(b);
                    String result = new String(b.array(), StandardCharsets.UTF_8).replaceAll("\0", "");
                    Log.i(TAG, result);
                    Log.i(TAG, ""+result.length());
                } else if(status == 400) {
                    Log.i(TAG, "Bad Request.");
                } else if(status == 500) {
                    Log.i(TAG, "Server Error.");
                }
            }

            @Override
            public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
                Log.i(TAG, "onReadCompleted method called.");
                // You should keep reading the request until there's no more data.
                byteBuffer.clear();
                request.read(byteBuffer);
            }

            @Override
            public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
                Log.i(TAG, "onSucceeded method called.");
            }

            @Override
            public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
                Log.i(TAG, "onFailed method called.");
            }

            @Override
            public void onCanceled(UrlRequest request, UrlResponseInfo info) {
                Log.i(TAG, "onCanceled method called.");
            }
        };
    }
}
