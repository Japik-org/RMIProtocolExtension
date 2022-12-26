package com.japik.extensions.rmiprotocol;

import com.japik.Japik;
import com.japik.service.IService;
import com.japik.service.IServiceConnection;
import com.japik.service.ServiceNotFoundException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public final class RMILookup extends UnicastRemoteObject implements IRMILookup {
    private final Japik server;

    public RMILookup(Japik server) throws RemoteException {
        this.server = server;
    }

    @Override
    public boolean existsService(String serviceName) throws RemoteException {
        return server.getServiceLoader().exists(serviceName);
    }

    @Override
    public <SC extends IServiceConnection> SC getServiceConnection(String serviceName) throws RemoteException, ServiceNotFoundException {
        final IService<SC> service = server.getServiceLoader().getServiceOrThrow(serviceName);
        return service.getServiceConnection();
    }
}
