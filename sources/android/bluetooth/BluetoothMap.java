package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothMap.Stub;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothMap implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.map.profile.action.CONNECTION_STATE_CHANGED";
    private static final boolean DBG = true;
    public static final int RESULT_CANCELED = 2;
    public static final int RESULT_FAILURE = 0;
    public static final int RESULT_SUCCESS = 1;
    public static final int STATE_ERROR = -1;
    private static final String TAG = "BluetoothMap";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter;
    private final BluetoothProfileConnector<IBluetoothMap> mProfileConnector = new BluetoothProfileConnector(this, 9, TAG, IBluetoothMap.class.getName()) {
        public IBluetoothMap getServiceInterface(IBinder service) {
            return Stub.asInterface(Binder.allowBlocking(service));
        }
    };

    BluetoothMap(Context context, ServiceListener listener) {
        Log.d(TAG, "Create BluetoothMap proxy object");
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mProfileConnector.connect(context, listener);
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    public synchronized void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothMap getService() {
        return (IBluetoothMap) this.mProfileConnector.getService();
    }

    public int getState() {
        IBluetoothMap service = getService();
        String str = TAG;
        if (service != null) {
            try {
                str = service.getState();
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        } else {
            Log.w(str, "Proxy not attached to service");
            log(Log.getStackTraceString(new Throwable()));
            return -1;
        }
    }

    public BluetoothDevice getClient() {
        IBluetoothMap service = getService();
        String str = TAG;
        if (service != null) {
            try {
                str = service.getClient();
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        } else {
            Log.w(str, "Proxy not attached to service");
            log(Log.getStackTraceString(new Throwable()));
            return null;
        }
    }

    public boolean isConnected(BluetoothDevice device) {
        IBluetoothMap service = getService();
        String str = TAG;
        if (service != null) {
            try {
                str = service.isConnected(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        } else {
            Log.w(str, "Proxy not attached to service");
            log(Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public boolean connect(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("connect(");
        stringBuilder.append(device);
        stringBuilder.append(")not supported for MAPS");
        log(stringBuilder.toString());
        return false;
    }

    @UnsupportedAppUsage
    public boolean disconnect(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("disconnect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothMap service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.disconnect(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public static boolean doesClassMatchSink(BluetoothClass btClass) {
        int deviceClass = btClass.getDeviceClass();
        if (deviceClass == 256 || deviceClass == 260 || deviceClass == 264 || deviceClass == 268) {
            return true;
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        log("getConnectedDevices()");
        IBluetoothMap service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            str = service.getConnectedDevices();
            return str;
        } catch (RemoteException e) {
            Log.e(str, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        log("getDevicesMatchingStates()");
        IBluetoothMap service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            str = service.getDevicesMatchingConnectionStates(states);
            return str;
        } catch (RemoteException e) {
            Log.e(str, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getConnectionState(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothMap service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getConnectionState(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return 0;
    }

    public boolean setPriority(BluetoothDevice device, int priority) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setPriority(");
        stringBuilder.append(device);
        stringBuilder.append(", ");
        stringBuilder.append(priority);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothMap service = getService();
        String str = TAG;
        if (service == null || !isEnabled() || !isValidDevice(device)) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return false;
        } else if (priority != 0 && priority != 100) {
            return false;
        } else {
            try {
                str = service.setPriority(device, priority);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
    }

    public int getPriority(BluetoothDevice device) {
        IBluetoothMap service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getPriority(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return 0;
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    private boolean isEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.getState() == 12) {
            return true;
        }
        log("Bluetooth is Not enabled");
        return false;
    }

    private static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }
}
