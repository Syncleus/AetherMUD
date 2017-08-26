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
package com.syncleus.aethermud.command;

import com.syncleus.aethermud.command.commands.Command;

import java.util.*;

public class CommandRegistry {

    private final Command unknownCommand;

    public CommandRegistry(Command unknownCommand) {
        this.unknownCommand = unknownCommand;
    }

    private final HashMap<String, Command> creeperCommands = new HashMap<>();

    public void addCommand(Command command) {
        List<String> validTriggers = command.validTriggers;
        for (String trigger: validTriggers) {
            creeperCommands.put(trigger, command);
        }
    }

    public Command getCommandByTrigger(String trigger) {
        for (Map.Entry<String, Command> next : creeperCommands.entrySet()) {
            if (trigger.equals(next.getKey())) {
                return next.getValue();
            }
        }
        return unknownCommand;
    }

    public Set<Command> getCreeperCommands() {
        Set<Command> creeperCommandUniq = new HashSet<Command>(creeperCommands.values());
        return creeperCommandUniq;
    }
}
