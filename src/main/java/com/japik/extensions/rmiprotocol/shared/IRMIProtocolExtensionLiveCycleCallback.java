package com.japik.extensions.rmiprotocol.shared;

import com.japik.Japik;
import com.japik.extensions.rmiprotocol.RMILookup;

import java.rmi.registry.Registry;

public interface IRMIProtocolExtensionLiveCycleCallback {
    Japik getServer();
    Registry getRegistry();
    RMILookup getLookup();
    void setLookup(RMILookup lookup);
}
