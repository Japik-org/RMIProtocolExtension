package com.japik.extensions.rmiprotocol;

import com.japik.service.IServiceConnection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMILookup extends Remote {
    boolean existsService(String serviceName) throws RemoteException;
    IServiceConnection getServiceConnection(String serviceName) throws RemoteException;
}
