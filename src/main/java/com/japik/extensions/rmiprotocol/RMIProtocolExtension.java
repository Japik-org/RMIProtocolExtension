package com.japik.extensions.rmiprotocol;

import com.japik.extension.AExtension;
import com.japik.extension.ExtensionConnectionParams;
import com.japik.extension.ExtensionParams;
import com.japik.extensions.rmiprotocol.shared.IRMIProtocolExtensionConnection;
import com.japik.livecycle.controller.ILiveCycleImplId;
import com.japik.livecycle.controller.LiveCycleController;
import com.japik.livecycle.controller.LiveCycleImplId;
import lombok.Getter;
import lombok.Setter;

public final class RMIProtocolExtension extends AExtension<IRMIProtocolExtensionConnection> {

    private final RMIProtocol protocol = new RMIProtocol(server, settings);
    private final ProtocolLiveCycleImplId protocolLiveCycleImplId = new ProtocolLiveCycleImplId();

    public RMIProtocolExtension(ExtensionParams extensionParams) {
        super(extensionParams);
    }

    @Override
    public IRMIProtocolExtensionConnection createExtensionConnection(ExtensionConnectionParams params) {
        return new RMIProtocolExtensionConnection(this, params);
    }

    @Override
    protected void initLiveCycleController(LiveCycleController liveCycleController) {
        super.initLiveCycleController(liveCycleController);
        liveCycleController.putImplAll(protocolLiveCycleImplId, protocol.getLiveCycle());

        liveCycleController.getInitImplQueue().put(
                new LiveCycleImplId("add-protocol", LiveCycleController.PRIORITY_LOWER), () -> {
            server.getNetworking().getProtocolCollection().add(protocol);
        });

        liveCycleController.getDestroyImplQueue().put(
                new LiveCycleImplId("remove-protocol", LiveCycleController.PRIORITY_HIGHEST), () -> {
            server.getNetworking().getProtocolCollection().remove(protocol);
        });
    }

    @Getter
    private static final class ProtocolLiveCycleImplId implements ILiveCycleImplId {
        private final String name = "protocol";
        @Setter
        private int priority = LiveCycleController.PRIORITY_NORMAL;
    }
}
