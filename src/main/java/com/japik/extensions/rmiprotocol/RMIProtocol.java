package com.japik.extensions.rmiprotocol;

import com.japik.Japik;
import com.japik.livecycle.AShortLiveCycleImplId;
import com.japik.livecycle.controller.LiveCycleController;
import com.japik.networking.AProtocol;
import com.japik.networking.IProtocolInstance;
import com.japik.settings.Settings;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class RMIProtocol extends AProtocol {
    public static final String protocolName = "RMI";

    private Registry registry;
    private RMILookup lookup;

    public RMIProtocol(Japik server, Settings settings) {
        super(protocolName, server, settings);
    }

    @Override
    protected void initLiveCycleController(LiveCycleController liveCycleController) {
        super.initLiveCycleController(liveCycleController);

        liveCycleController.putImplAll(new RMIProtocolLiveCycleImpl());

        liveCycleController.getStartImplQueue().put("add-protocol", () -> {
            server.getNetworking().getProtocolCollection().add(this);
        });

        liveCycleController.getStopSlowImplQueue().put("remove-protocol", () -> {
            server.getNetworking().getProtocolCollection().remove(this);
        });
        liveCycleController.getStopForceImplQueue().put("remove-protocol", () -> {
            server.getNetworking().getProtocolCollection().remove(this);
        });
    }

    @Override
    protected IProtocolInstance newInstanceImpl(Settings protocolSettings)
            throws RemoteException, NotBoundException {
        return new RMIProtocolInstance(
                protocolSettings.get("host"),
                protocolSettings.getInt("port")
        );
    }

    private final class RMIProtocolLiveCycleImpl extends AShortLiveCycleImplId {

        @Override
        public void init() throws Throwable {
            if (settings.getBoolean("enableLookup")) {
                registry = LocateRegistry.createRegistry(
                        settingsManager.getSettings().getInt("port")
                );
                lookup = new RMILookup(server);
            }
        }

        @Override
        public void start() throws Throwable {
            if (lookup != null) {
                registry.rebind(protocolName + "Protocol", lookup);
            }
        }

        @Override
        public void stopForce() {
            if (lookup != null) {
                try {
                    registry.unbind(protocolName + "Protocol");
                } catch (RemoteException | NotBoundException e) {
                    logger.exception(e, "Failed to unbind lookup");
                }
            }
        }

        @Override
        public void destroy() {
            lookup = null;
            registry = null;
        }
    }

}
