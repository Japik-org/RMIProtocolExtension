package com.japik.extensions.rmiprotocol;

import com.japik.extension.AExtensionConnection;
import com.japik.extension.ExtensionConnectionParams;
import com.japik.extensions.rmiprotocol.shared.IRMIProtocolExtensionConnection;
import org.jetbrains.annotations.NotNull;

public class RMIProtocolExtensionConnection
        extends AExtensionConnection<RMIProtocolExtension, IRMIProtocolExtensionConnection>
        implements IRMIProtocolExtensionConnection {

    public RMIProtocolExtensionConnection(@NotNull RMIProtocolExtension extension,
                                          ExtensionConnectionParams params) {
        super(extension, params);
    }

}
