// 1.7.10 Protocol Docs: http://wiki.vg/index.php?title=Protocol&oldid=6003
http://wiki.vg/index.php?title=Entities&oldid=6052

    // TODO: Actually record data
    // TODO: Code to 'init' a recording -- record entities, chunk data, etc. etc.
    // TODO: Code to be able to end a recording -- reset blocks, entities, etc.
    // TODO: Finish partially done + need to do packets.
    // TODO: Save even more space by pre-converting doubles+floats+stuff into bytes

    // players joining in to a recording

        /*
            Done -----------------------------

            ENTITY_EQUIPMENT
            COLLECT
            UPDATE_SIGN
            REMOVE_ENTITY_EFFECT
            ENTITY_DESTROY
            ENTITY_EFFECT
            ENTITY
            SPAWN_ENTITY_WEATHER
            BED
            ANIMATION
            ENTITY_VELOCITY
            WORLD_PARTICLES
            NAMED_SOUND_EFFECT
            WORLD_EVENT
            REL_ENTITY_MOVE
            ENTITY_MOVE_LOOK
            ENTITY_LOOK
            ENTITY_HEAD_ROTATION
            ENTITY_TELEPORT
            BLOCK_ACTION
            BLOCK_BREAK_ANIMATION
            BLOCK_CHANGE
            SPAWN_ENTITY_EXPERIENCE_ORB
            NAMED_ENTITY_SPAWN
            SPAWN_ENTITY_LIVING

            Partially done -----------------------------

            EXPLOSION (Possibly store player data, possibly store records?)
            ATTACH_ENTITY (Verify how us<->NMS should work, as the constructor is *really* weird.)
            SPAWN_ENTITY (Needs entity type byte and object data stuff.)
            ENTITY_METADATA (It crashed my client. Yeah.)

            Need to do -----------------------------

            SPAWN_ENTITY_PAINTING
            ENTITY_STATUS
            MULTI_BLOCK_CHANGE
     */