package com.comandante.creeper.command.admin;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TagRoomCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("tagRoom", "tr");
    final static String description = "Sets a tag on a room.";
    final static String correctUsage = "tag <tag> | tag list";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public TagRoomCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            originalMessageParts.remove(0);
            if (originalMessageParts.get(0).equalsIgnoreCase("list")) {
                StringBuilder sb = new StringBuilder();
                Iterator<String> iterator = currentRoom.getRoomTags().iterator();
                while (iterator.hasNext()) {
                    String tag = iterator.next();
                    sb.append(tag).append("\n");
                }
                write("tag\n---");
                write(sb.toString());
                return;
            }
            currentRoom.addTag(originalMessageParts.get(0));
            write(String.format("tagged world with tag: \"%s\".", originalMessageParts.get(0)));
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
