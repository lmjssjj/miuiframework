package com.android.server.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkRequest;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;

public class NetworkPinner extends NetworkCallback {
    private static final String TAG = NetworkPinner.class.getSimpleName();
    @GuardedBy({"sLock"})
    private static ConnectivityManager sCM;
    @GuardedBy({"sLock"})
    private static Callback sCallback;
    @VisibleForTesting
    protected static final Object sLock = new Object();
    @GuardedBy({"sLock"})
    @VisibleForTesting
    protected static Network sNetwork;

    private static class Callback extends NetworkCallback {
        private Callback() {
        }

        public void onAvailable(Network network) {
            synchronized (NetworkPinner.sLock) {
                if (this != NetworkPinner.sCallback) {
                    return;
                }
                if (NetworkPinner.sCM.getBoundNetworkForProcess() == null && NetworkPinner.sNetwork == null) {
                    NetworkPinner.sCM.bindProcessToNetwork(network);
                    NetworkPinner.sNetwork = network;
                    String access$200 = NetworkPinner.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Wifi alternate reality enabled on network ");
                    stringBuilder.append(network);
                    Log.d(access$200, stringBuilder.toString());
                }
                NetworkPinner.sLock.notify();
            }
        }

        public void onLost(Network network) {
            synchronized (NetworkPinner.sLock) {
                if (this != NetworkPinner.sCallback) {
                    return;
                }
                if (network.equals(NetworkPinner.sNetwork) && network.equals(NetworkPinner.sCM.getBoundNetworkForProcess())) {
                    NetworkPinner.unpin();
                    String access$200 = NetworkPinner.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Wifi alternate reality disabled on network ");
                    stringBuilder.append(network);
                    Log.d(access$200, stringBuilder.toString());
                }
                NetworkPinner.sLock.notify();
            }
        }
    }

    private static void maybeInitConnectivityManager(Context context) {
        if (sCM == null) {
            sCM = (ConnectivityManager) context.getSystemService("connectivity");
            if (sCM == null) {
                throw new IllegalStateException("Bad luck, ConnectivityService not started.");
            }
        }
    }

    public static void pin(Context context, NetworkRequest request) {
        synchronized (sLock) {
            if (sCallback == null) {
                maybeInitConnectivityManager(context);
                sCallback = new Callback();
                try {
                    sCM.registerNetworkCallback(request, sCallback);
                } catch (SecurityException e) {
                    Log.d(TAG, "Failed to register network callback", e);
                    sCallback = null;
                }
            }
        }
    }

    public static void unpin() {
        synchronized (sLock) {
            if (sCallback != null) {
                try {
                    sCM.bindProcessToNetwork(null);
                    sCM.unregisterNetworkCallback(sCallback);
                } catch (SecurityException e) {
                    Log.d(TAG, "Failed to unregister network callback", e);
                }
                sCallback = null;
                sNetwork = null;
            }
        }
    }
}
