/**
 * Copyright 2017 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncleus.aethermud.command.commands.admin;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.syncleus.aethermud.command.commands.Command;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.ItemBuilder;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.npc.NpcBuilder;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.ItemData;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ManifestCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("manifest");
    final static String description = "Manifest an item.";
    final static String correctUsage = "manifest <item id> | spawn";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public ManifestCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
                List<? extends ItemData> itemsFromDb = tx.getStorage().getAllItems();
                if (originalMessageParts.size() == 1) {
                    write(getHeader());
                    for (ItemData itemData : itemsFromDb) {
                        write( itemData.getInternalItemName() +": " + itemData.getItemName() + "\r\n");
                    }
                } else {
                    originalMessageParts.remove(0);
                    String itemName = Joiner.on(" ").join(originalMessageParts);

                    Optional<ItemData> optionalItemData = tx.getStorage().getItem(itemName);
                    if(!optionalItemData.isPresent()) {
                        write("Item " + itemName + " not found.\r\n");
                        return;
                    }

                    ItemData itemData = optionalItemData.get();
                    ItemInstance item = new ItemBuilder().from(ItemData.copyItem(itemData)).create();
                    gameManager.getEntityManager().saveItem(item);
                    gameManager.placeItemInRoom(currentRoom.getRoomId(), item.getItemId());
                    write("Item " + itemName + " manifested.\r\n");
                }
            }
        });
    }

    public String getHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append(Color.MAGENTA + "-+=[ " + Color.RESET).append("Item").append(Color.MAGENTA + " ]=+- " + Color.RESET).append("\r\n");
        sb.append(Color.MAGENTA + "AvailableItems-----------------------" + Color.RESET).append("\r\n");
        return sb.toString();
    }
}
