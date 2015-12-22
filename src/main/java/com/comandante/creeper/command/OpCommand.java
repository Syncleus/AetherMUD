package com.comandante.creeper.command;

import com.comandante.creeper.CreeperEntry;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Merchant;
import com.comandante.creeper.merchant.lockers.LockerCommand;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserChannelDao;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OpCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("open", "o");
    final static String description = "Open a locker.";
    final static String correctUsage = "open lockers";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public OpCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    public OpCommand(GameManager gameManager, List<String> validTriggers, String description, String correctUsage) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            //ghetto and will only work for one channel bots.
            originalMessageParts.remove(0);
            String desiredNickNameToOp = Joiner.on(" ").join(originalMessageParts);
            UserChannelDao<User, Channel> userChannelDao = gameManager.getIrcBotService().getBot().getUserChannelDao();
            User user = userChannelDao.getUser(desiredNickNameToOp);
            if (user == null) {
                write("No such nick name exists in " + gameManager.getCreeperConfiguration().ircChannel);
                return;
            }
            ImmutableSortedSet<Channel> channels = user.getChannels();
            for (Channel channel : channels) {
                if (channel.getName().equals(gameManager.get))
                channel.send().op(user);
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
