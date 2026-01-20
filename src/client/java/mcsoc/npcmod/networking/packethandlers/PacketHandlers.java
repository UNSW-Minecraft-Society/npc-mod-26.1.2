package mcsoc.npcmod.networking.packethandlers;

public class PacketHandlers {
    
    public static void registerHandlers() {
        SyncDialogueDataPacketEvent.registerHandler();
        SyncModelDataPacketEvent.registerHandler();
        SyncNPCDataPacketEvent.registerHandler();
        SyncMovingNPCDataPacketEvent.registerHandler();

        SyncCameraPositionPacketEvent.registerHandler();
    }
}
