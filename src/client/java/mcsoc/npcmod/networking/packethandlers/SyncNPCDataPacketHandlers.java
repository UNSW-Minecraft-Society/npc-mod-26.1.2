package mcsoc.npcmod.networking.packethandlers;

public class SyncNPCDataPacketHandlers {
    
    public static void registerHandlers() {
        SyncDialogueDataPacketEvent.registerHandler();
        SyncModelDataPacketEvent.registerHandler();
        SyncNPCDataPacketEvent.registerHandler();
        SyncMovingNPCDataPacketEvent.registerHandler();
    }
}
