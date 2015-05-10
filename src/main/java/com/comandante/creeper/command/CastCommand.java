package com.comandante.creeper.command;


import com.comandante.creeper.fight.FightManager;
import com.comandante.creeper.fight.FightResults;
import com.comandante.creeper.fight.FightRun;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spells.LightningSpell;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

public class CastCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("cast", "c");
    final static String description = "Cast a spell.";
    final static String correctUsage = "cast lightning";

    public CastCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() == 1) {
                LightningSpell lightningSpell = new LightningSpell(gameManager);
                lightningSpell.attackSpell(currentRoom.getNpcIds(), player);
                return;
            }
            originalMessageParts.remove(0);
            String target = Joiner.on(" ").join(originalMessageParts);
            for (String npcId : currentRoom.getNpcIds()) {
                Npc npcEntity = entityManager.getNpcEntity(npcId);
                if (npcEntity.getValidTriggers().contains(target)) {
                    LightningSpell lightningSpell = new LightningSpell(gameManager);
                    lightningSpell.attackSpell(Sets.newHashSet(npcId), player);
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}