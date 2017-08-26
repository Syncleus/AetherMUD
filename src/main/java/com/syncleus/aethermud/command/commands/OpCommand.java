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
package com.syncleus.aethermud.command.commands;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerRole;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.UserChannelDao;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OpCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("op");
    final static String description = "Give a user in the IRC channel ops.";
    final static String correctUsage = "op fibs";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public OpCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    public OpCommand(GameManager gameManager, List<String> validTriggers, String description, String correctUsage) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            //ghetto and will only work for one channel bots.
            if (originalMessageParts.size() <= 1) {
                return;
            }
            originalMessageParts.remove(0);
            String desiredNickNameToOp = Joiner.on(" ").join(originalMessageParts);
            UserChannelDao<User, Channel> userChannelDao = gameManager.getIrcBotService().getBot().getUserChannelDao();
            User user = userChannelDao.getUser(desiredNickNameToOp);
            if (user == null) {
                write("No such nick name exists in " + gameManager.getAetherMudConfiguration().ircChannel);
                return;
            }
            ImmutableSortedSet<Channel> channels = user.getChannels();
            for (Channel channel : channels) {
                channel.send().op(user);
                write("Ops given in channel " + channel.getName());
            }
        });
    }
}
