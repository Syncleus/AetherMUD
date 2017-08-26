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
package com.comandante.creeper.bot.command.commands;

import com.comandante.creeper.bot.command.BotCommandManager;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.pircbotx.hooks.events.MessageEvent;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class BotCommand  {

    private final Set<String> triggers;
    private String originalFullCommand;
    private final String helpUsage;
    private final String helpDescription;
    public final BotCommandManager botCommandManager;
    public List<String> args;

    private MessageEvent messageEvent;

    public BotCommand(BotCommandManager botCommandManager, Set<String> triggers, String helpUsage, String helpDescription) {
        this.botCommandManager = botCommandManager;
        this.triggers = triggers;
        this.helpUsage = helpUsage;
        this.helpDescription = helpDescription;
    }

    public List<String> process() {
        return Lists.newArrayList();
    }

    public String joinArgs(List<String> args) {
        return Joiner.on(" ").join(args);
    }

    public List<String> buildArgs(String originalFullCommand) {
        ArrayList<String> args = Lists.newArrayList(Arrays.asList(originalFullCommand.split(" ")));
        // get rid of the original command;
        args.remove(0);
        return args;
    }

    public boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public Set<String> getTriggers() {
        return triggers;
    }

    public String getOriginalFullCommand() {
        return originalFullCommand;
    }

    public void setOriginalFullCommand(String originalFullCommand) {
        this.args = buildArgs(originalFullCommand);
        this.originalFullCommand = originalFullCommand;
    }

    public MessageEvent getMessageEvent() {
        return messageEvent;
    }

    public void setMessageEvent(MessageEvent messageEvent) {
        this.messageEvent = messageEvent;
    }

    public String getHelpUsage() {
        return helpUsage;
    }

    public String getHelpDescription() {
        return helpDescription;
    }

    public BotCommandManager getBotCommandManager() {
        return botCommandManager;
    }
}