package net.frozenorb.camcorder.action;

import net.frozenorb.camcorder.action.actions.*;

import java.util.HashMap;
import java.util.Map;

public final class ActionRegistry {

    private static final Map<Integer, Class<? extends Action>> idToClass = new HashMap<>();
    private static final Map<Class<? extends Action>, Integer> classToId = new HashMap<>();

    static {
        register(0x04, EntityEquipmentAction.class); // NEEDS RECORDING
        register(0x0B, AnimationAction.class); // NEEDS RECORDING
        register(0x0C, SpawnPlayerAction.class); // NEEDS RECORDING
        register(0x0D, CollectItemAction.class);
        register(0x0E, SpawnObjectAction.class); // NEEDS RECORDING
        register(0x12, EntityVelocityAction.class); // NEEDS RECORDING
        register(0x13, EntityDestroyAction.class); // NEEDS RECORDING
        register(0x17, EntityLookAndRelativeMoveAction.class); // NEEDS RECORDING
        register(0x18, EntityTeleportAction.class); // NEEDS RECORDING
        register(0x19, EntityHeadLookAction.class); // NEEDS RECORDING
        register(0x1A, EntityStatusAction.class);
        register(0x1C, EntityMetadataAction.class); // NEEDS RECORDING
        register(0x1D, EntityEffectAction.class); // NEEDS RECORDING
        register(0x1E, RemoveEntityEffectAction.class); // NEEDS RECORDING
        register(0x2C, SpawnGlobalEntityAction.class);
    }

    public static Class<? extends Action> findAction(int id) {
        return idToClass.get(id);
    }

    public static int findId(Class<? extends Action> actionClass) {
        return classToId.get(actionClass);
    }

    private static void register(int packetId, Class<? extends Action> actionClass) {
        idToClass.put(packetId, actionClass);
        classToId.put(actionClass, packetId);
    }

}