package com.japik.extensions.rmiprotocol;

import com.japik.service.IServiceConnection;
import com.japik.service.ServiceNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMILookup extends Remote {
    boolean existsService(String serviceName) throws RemoteException;
    <SC extends IServiceConnection> SC getServiceConnection(String serviceName) throws RemoteException, ServiceNotFoundException;
}
