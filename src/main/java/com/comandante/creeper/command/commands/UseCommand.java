/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.comandante.creeper.command.commands;


import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.Item;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UseCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("use");
    final static String description = "Use an item.";
    final static String correctUsage = "use <item name>";

    public UseCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {

            UseItemOn useItemOn = new UseItemOn(originalMessageParts);

            if (Strings.isNullOrEmpty(useItemOn.getItem())) {
                write("No item specified.");
                return;
            }

            Optional<Item> inventoryItemOptional = player.getInventoryItem(useItemOn.getItem());
            if (!inventoryItemOptional.isPresent()) {
                write("Useable item is not found in your inventory.\r\n");
                return;
            }

            gameManager.getItemUseHandler().handle(player, inventoryItemOptional.get(), useItemOn);
        });
    }

    public static class UseItemOn {

        public static String ON_KEYWORD = "on";

        private final Optional<String> target;
        private final String item;

        public UseItemOn(List<String> originalMessageParts) {
            originalMessageParts.remove(0);
            String fullCommand = Joiner.on(" ").join(originalMessageParts);
            item = getItem(fullCommand);
            target = getItemTarget(fullCommand);
        }

        private static String getItem(String fullCommand) {
            if (!StringUtils.containsIgnoreCase(fullCommand, ON_KEYWORD)) {
                return fullCommand;
            }
            List<String> parts = Arrays.asList(fullCommand.split("(?i)" + ON_KEYWORD, 2));
            String item = parts.get(0);
            return item.trim();
        }

        private static Optional<String> getItemTarget(String fullCommand) {
            if (!StringUtils.containsIgnoreCase(fullCommand, ON_KEYWORD)) {
                return Optional.empty();
            }
            List<String> parts = Arrays.asList(fullCommand.split("(?i)" + ON_KEYWORD, 2));
            if (parts.size() > 1) {
                String item = parts.get(1);
                return Optional.of(item.trim());
            }
            return Optional.empty();
        }

        public Optional<String> getTarget() {
            return target;
        }

        public String getItem() {
            return item;
        }
    }
}
