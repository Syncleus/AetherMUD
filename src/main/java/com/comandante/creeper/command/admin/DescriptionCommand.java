package com.comandante.creeper.command.admin;

import com.comandante.creeper.CreeperEntry;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.PlayerRole;
import com.comandante.creeper.server.MultiLineInputManager;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.*;

public class DescriptionCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("desc", "description");
    final static String description = "Edit the description for the current room.";
    final static String correctUsage = "description";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);


    public DescriptionCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (creeperSession.getGrabMultiLineInput().isPresent()) {
                MultiLineInputManager multiLineInputManager = gameManager.getMultiLineInputManager();
                UUID uuid = creeperSession.getGrabMultiLineInput().get().getKey();
                String multiLineInput = multiLineInputManager.retrieveMultiLineInput(uuid);
                currentRoom.setRoomDescription(multiLineInput);
                creeperSession.setGrabMultiLineInput(Optional.<CreeperEntry<UUID, Command>>absent());
                return;
            }
            if (originalMessageParts.size() > 1) {
                String possibleNotable = originalMessageParts.get(1);
                for (Map.Entry<String, String> notable : currentRoom.getNotables().entrySet()) {
                    if (notable.getKey().equalsIgnoreCase(possibleNotable)) {
                        if (originalMessageParts.size() > 2) {
                            originalMessageParts.remove(0);
                            originalMessageParts.remove(0);
                            String newDesc = Joiner.on(" ").join(originalMessageParts);
                            notable.setValue(newDesc);
                            write("Notable: "  + notable.getKey() + " description is set to: " + newDesc);
                            return;
                        }
                    }
                }
            }
            write("You are now in multi-line mode.  Type \"done\" on an empty line to exit and save.\r\n");
            creeperSession.setGrabMultiLineInput(Optional.of(
                    new CreeperEntry<UUID, Command>(gameManager.getMultiLineInputManager().createNewMultiLineInput(), this)));
        } finally {
           super.messageReceived(ctx, e);
        }
    }
}
