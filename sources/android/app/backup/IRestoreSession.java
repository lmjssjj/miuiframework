package android.app.backup;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRestoreSession extends IInterface {

    public static class Default implements IRestoreSession {
        public int getAvailableRestoreSets(IRestoreObserver observer, IBackupManagerMonitor monitor) throws RemoteException {
            return 0;
        }

        public int restoreAll(long token, IRestoreObserver observer, IBackupManagerMonitor monitor) throws RemoteException {
            return 0;
        }

        public int restorePackages(long token, IRestoreObserver observer, String[] packages, IBackupManagerMonitor monitor) throws RemoteException {
            return 0;
        }

        public int restorePackage(String packageName, IRestoreObserver observer, IBackupManagerMonitor monitor) throws RemoteException {
            return 0;
        }

        public void endRestoreSession() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRestoreSession {
        private static final String DESCRIPTOR = "android.app.backup.IRestoreSession";
        static final int TRANSACTION_endRestoreSession = 5;
        static final int TRANSACTION_getAvailableRestoreSets = 1;
        static final int TRANSACTION_restoreAll = 2;
        static final int TRANSACTION_restorePackage = 4;
        static final int TRANSACTION_restorePackages = 3;

        private static class Proxy implements IRestoreSession {
            public static IRestoreSession sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getAvailableRestoreSets(IRestoreObserver observer, IBackupManagerMonitor monitor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (monitor != null) {
                        iBinder = monitor.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getAvailableRestoreSets(observer, monitor);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int restoreAll(long token, IRestoreObserver observer, IBackupManagerMonitor monitor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(token);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (monitor != null) {
                        iBinder = monitor.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().restoreAll(token, observer, monitor);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int restorePackages(long token, IRestoreObserver observer, String[] packages, IBackupManagerMonitor monitor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(token);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeStringArray(packages);
                    if (monitor != null) {
                        iBinder = monitor.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().restorePackages(token, observer, packages, monitor);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int restorePackage(String packageName, IRestoreObserver observer, IBackupManagerMonitor monitor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (monitor != null) {
                        iBinder = monitor.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().restorePackage(packageName, observer, monitor);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void endRestoreSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().endRestoreSession();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRestoreSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRestoreSession)) {
                return new Proxy(obj);
            }
            return (IRestoreSession) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getAvailableRestoreSets";
            }
            if (transactionCode == 2) {
                return "restoreAll";
            }
            if (transactionCode == 3) {
                return "restorePackages";
            }
            if (transactionCode == 4) {
                return "restorePackage";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "endRestoreSession";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i == 1) {
                parcel.enforceInterface(descriptor);
                int _result = getAvailableRestoreSets(android.app.backup.IRestoreObserver.Stub.asInterface(data.readStrongBinder()), android.app.backup.IBackupManagerMonitor.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                parcel2.writeInt(_result);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(descriptor);
                int _result2 = restoreAll(data.readLong(), android.app.backup.IRestoreObserver.Stub.asInterface(data.readStrongBinder()), android.app.backup.IBackupManagerMonitor.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                parcel2.writeInt(_result2);
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(descriptor);
                int _result3 = restorePackages(data.readLong(), android.app.backup.IRestoreObserver.Stub.asInterface(data.readStrongBinder()), data.createStringArray(), android.app.backup.IBackupManagerMonitor.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                parcel2.writeInt(_result3);
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(descriptor);
                int _result4 = restorePackage(data.readString(), android.app.backup.IRestoreObserver.Stub.asInterface(data.readStrongBinder()), android.app.backup.IBackupManagerMonitor.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                parcel2.writeInt(_result4);
                return true;
            } else if (i == 5) {
                parcel.enforceInterface(descriptor);
                endRestoreSession();
                reply.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IRestoreSession impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRestoreSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void endRestoreSession() throws RemoteException;

    int getAvailableRestoreSets(IRestoreObserver iRestoreObserver, IBackupManagerMonitor iBackupManagerMonitor) throws RemoteException;

    int restoreAll(long j, IRestoreObserver iRestoreObserver, IBackupManagerMonitor iBackupManagerMonitor) throws RemoteException;

    int restorePackage(String str, IRestoreObserver iRestoreObserver, IBackupManagerMonitor iBackupManagerMonitor) throws RemoteException;

    int restorePackages(long j, IRestoreObserver iRestoreObserver, String[] strArr, IBackupManagerMonitor iBackupManagerMonitor) throws RemoteException;
}
