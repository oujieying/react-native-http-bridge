package me.alwx.HttpServer;

import android.content.Context;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactMethod;

import java.io.File;
import java.io.IOException;

import android.util.Log;

import fi.iki.elonen.SimpleWebServer;

public class HttpServerModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    ReactApplicationContext reactContext;

    private static final String MODULE_NAME = "HttpServer";

    private static int port;
    private static Server server = null;
    private static SimpleWebServer webServer = null;
    private static int webServerPort;

    public HttpServerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void start(int port, String serviceName) {
        Log.d(MODULE_NAME, "Initializing server...");
        this.port = port;

        startServer();
    }



    @ReactMethod
    public void stop() {
        Log.d(MODULE_NAME, "Stopping server...");

        stopServer();
    }

    @ReactMethod
    public void respond(String requestId, int code, String type, String body) {
        if (server != null) {
            server.respond(requestId, code, type, body);
        }
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        stopServer();
    }

    private void startServer() {
        if (this.port == 0) {
            return;
        }

        if (server == null) {
            server = new Server(reactContext, port);
        }
        try {
            server.start();
        } catch (IOException e) {
            Log.e(MODULE_NAME, e.getMessage());
        }
    }

    private void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
            port = 0;
        }
    }


    @ReactMethod
    public void startWebServer(String host,int port, String filePath) {
        Log.d(MODULE_NAME, "Initializing server...");
        this.webServerPort = port;
        if (this.webServerPort == 0) {
            return;
        }
        File  file = new File(filePath);
        if(!file.exists()){
            Log.d("File is not exist",filePath);
            file =  reactContext.getFilesDir();
        }

       // String host = "10.0.16.1";
        if (webServer == null) {
        //    File file = reactContext.getFilesDir();
            webServer = new SimpleWebServer(host,webServerPort,file,false);
            try {
                webServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @ReactMethod
    public void stopWebServefr() {
        Log.d(MODULE_NAME, "Stopping server...");
    }
}
