package com.deadlyduo.deadlyworkout;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String HELP_PATH = "/help";

    private TextView mTextView;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


    }

    private void sendMessageToPhone(String node) {
        Wearable.MessageApi.sendMessage(googleApiClient, node, HELP_PATH, null)
                .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                                       @Override
                                       public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                           if (!sendMessageResult.getStatus().isSuccess()) {
                                               Log.i(TAG, "Failed to send message with status code: "
                                                       + sendMessageResult.getStatus().getStatusCode());
                                           } else {
                                               Log.i(TAG, "Sent message successfully ");
                                           }
                                       }
                                   }
                );
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "client connected");
        new GetNodesAndSendRequest().execute();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "connection suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "connection failed" + connectionResult.getErrorCode() + " " + connectionResult.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();

        if (nodes != null) {
            for (Node node : nodes.getNodes()) {
                results.add(node.getId());
            }
        }

        return results;
    }

    private class GetNodesAndSendRequest extends AsyncTask<Void, Void, Void> {
        Collection<String> nodes;

        @Override
        protected Void doInBackground(Void... args) {
            Log.i(TAG, "Retrieving nodes...");
            nodes = getNodes();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (nodes.isEmpty()) {
                // nada
            } else {
                for (String node : nodes) {
                    Log.i(TAG, "Sending message to: " + node);
                    sendMessageToPhone(node);
                }
            }
        }
    }

}
