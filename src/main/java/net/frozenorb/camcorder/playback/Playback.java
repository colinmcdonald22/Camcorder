package net.frozenorb.camcorder.playback;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.camcorder.Camcorder;
import net.frozenorb.camcorder.action.Action;
import net.frozenorb.camcorder.action.ActionRegistry;
import net.frozenorb.camcorder.recording.Recording;
import net.frozenorb.camcorder.utils.ByteBufUtils;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import net.minecraft.util.org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.zip.GZIPInputStream;

public final class Playback {

    @Getter private final Recording recording;
    @Getter private final Instant started;
    @Getter @Setter private double speed;
    private final Set<UUID> viewers;

    public Playback(File file, Set<UUID> viewers) {
        this.recording = new Recording(file);
        this.started = Instant.now();
        this.speed = 1.0;
        this.viewers = new HashSet<>(viewers);
    }

    public Set<UUID> getViewers() {
        return ImmutableSet.copyOf(viewers);
    }

}