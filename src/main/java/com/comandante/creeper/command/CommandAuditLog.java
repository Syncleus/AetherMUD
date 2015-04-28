package com.comandante.creeper.command;

import org.apache.log4j.Logger;

public class CommandAuditLog {

    private static final Logger log = Logger.getLogger(CommandAuditLog.class);

    public static void logCommand(String originalCommand, String playerName) {
        log.info(playerName + ": " + originalCommand);
    }
}
