package com.comandante.creeper.bot.commands;

import com.comandante.creeper.bot.BotCommandManager;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

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

    public String getHelpUsage() {
        return helpUsage;
    }

    public String getHelpDescription() {
        return helpDescription;
    }


}