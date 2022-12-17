package com.japik.extensions.rmiprotocol.shared;

import com.japik.extensions.rmiprotocol.RMIProtocolInstance;
import com.japik.service.AServiceConnectionSafe;
import com.japik.service.IServiceConnection;
import com.japik.service.ServiceConnectionException;

import java.rmi.RemoteException;

public final class RMIServiceConnectionSafe <SC extends IServiceConnection> extends AServiceConnectionSafe<SC> {
    private final RMIProtocolInstance protocolInstance;

    public RMIServiceConnectionSafe(String serviceName, RMIProtocolInstance protocolInstance) {
        super(serviceName);
        this.protocolInstance = protocolInstance;
    }

    @Override
    public void refreshConnection() throws RemoteException {
        if (isClosed) throw new IllegalStateException();
        refreshLock.lock();
        try {
            final SC oldSC = serviceConnection;
            final SC newSC = (SC) protocolInstance.getServiceConnection(serviceName);
            if (oldSC != newSC && oldSC != null && !oldSC.isClosed()) {
                oldSC.close();
            }
            serviceConnection = newSC;

            try {
                serviceConnection.ping();
            } catch (Throwable throwable) {
                isClosed = true;
                if (serviceConnection != null) {
                    try {
                        serviceConnection.close();
                    } catch (Throwable ignored){
                    }
                }
                serviceConnection = null;
                throw throwable;
            }
            isClosed = false;

        } catch (RemoteException remoteException) {
            throw remoteException;

        } catch (Throwable throwable){
            throw new ServiceConnectionException(
                    serviceName,
                    throwable
            );

        } finally {
            refreshLock.unlock();
        }
    }
}
