package net.frozenorb.camcorder.action;

import net.frozenorb.camcorder.action.type.*;

import java.util.HashMap;
import java.util.Map;

public final class ActionRegistry {

    private static final Map<Integer, Class<? extends Action>> idToClass = new HashMap<>();
    private static final Map<Class<? extends Action>, Integer> classToId = new HashMap<>();

    static {
        register(0x00, ArmAnimationAction.class);
        register(0x01, AttachEntityAction.class);
        register(0x02, BedAction.class);
        register(0x03, BlockActionAction.class);
        register(0x04, BlockBreakAnimationAction.class);
        register(0x05, BlockChangeAction.class);
        register(0x06, CollectAction.class);
        register(0x07, EntityAction.class);
        register(0x08, EntityDestroyAction.class);
        register(0x09, EntityEffectAction.class);
        register(0x0A, EntityHeadRotationAction.class);
        register(0x0B, EntityLookAction.class);
        register(0x0C, EntityMoveLookAction.class);
        register(0x0D, EntityTeleportAction.class);
        register(0x0E, EntityVelocityAction.class);
        register(0x0F, ExplosionAction.class);
        register(0x10, NamedEntitySpawnAction.class);
        register(0x11, RelEntityMoveAction.class);
        register(0x12, RemoveEntityEffectAction.class);
        register(0x13, SoundEffectAction.class);
        register(0x14, SpawnEntityAction.class);
        register(0x15, SpawnEntityExperienceOrbAction.class);
        register(0x16, SpawnEntityLivingAction.class);
        register(0x17, SpawnEntityWeatherAction.class);
        register(0x18, UpdateSignAction.class);
        register(0x19, WorldEventAction.class);
        register(0x1A, WorldParticlesAction.class);
        register(0x1B, EntityEquipmentAction.class);
        register(0x1C, EntityMetadataAction.class);
        register(0x1D, DamageAnimationAction.class);
    }

    public static Class<? extends Action> findAction(int id) {
        return idToClass.get(id);
    }

    public static int findId(Class<? extends Action> actionClass) {
        return classToId.get(actionClass);
    }

    private static void register(int actionId, Class<? extends Action> actionClass) {
        idToClass.put(actionId, actionClass);
        classToId.put(actionClass, actionId);
    }

}