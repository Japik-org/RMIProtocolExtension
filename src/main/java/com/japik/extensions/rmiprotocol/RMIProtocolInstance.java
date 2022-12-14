package com.japik.extensions.rmiprotocol;

import com.japik.extensions.rmiprotocol.shared.RMIServiceConnectionSafe;
import com.japik.networking.AProtocolInstance;
import com.japik.service.IServiceConnection;
import com.japik.service.IServiceConnectionSafe;
import com.japik.service.ServiceNotFoundException;
import lombok.Getter;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class RMIProtocolInstance extends AProtocolInstance {
    private Registry registry;
    @Getter
    private IRMILookup remoteLookup;

    public RMIProtocolInstance(String host, int port)
            throws RemoteException, NotBoundException {
        super(RMIProtocol.protocolName);
        registry = LocateRegistry.getRegistry(host, port);
        remoteLookup = (IRMILookup) registry.lookup(RMIProtocol.protocolName+"Protocol");
    }

    @Override
    protected boolean existsServiceImpl(String serviceName) throws RemoteException {
        return remoteLookup.existsService(serviceName);
    }

    @Override
    protected <SC extends IServiceConnection> SC getServiceConnectionImpl(String serviceName) throws RemoteException, ServiceNotFoundException {
        return remoteLookup.getServiceConnection(serviceName);
    }

    @Override
    protected <SC extends IServiceConnection> IServiceConnectionSafe<SC> createServiceConnectionSafeImpl(String serviceName) {
        return new RMIServiceConnectionSafe<>(serviceName, this);
    }

    @Override
    protected void onClose() {
        registry = null;
        remoteLookup = null;
    }
}
