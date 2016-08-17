package com.comandante.creeper.command;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PickUpCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("pickup", "pick", "p");
    final static String description = "Pick up an item.";
    final static String correctUsage = "pickup <item name>";

    public PickUpCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description,correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            Set<String> itemIds = currentRoom.getItemIds();
            originalMessageParts.remove(0);
            String desiredPickUpItem = Joiner.on(" ").join(originalMessageParts);
            for (String next : itemIds) {
                Item itemEntity = entityManager.getItemEntity(next);
                if (itemEntity != null) {
                    if (itemEntity.getItemTriggers().contains(desiredPickUpItem)) {
                        if (gameManager.acquireItemFromRoom(player, next)) {
                            String playerName = player.getPlayerName();
                            gameManager.roomSay(currentRoom.getRoomId(), playerName + " picked up " + itemEntity.getItemName(), playerId);
                            return;
                        } else {
                            return;
                        }
                    }
                }
            }
        });
    }
}
