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
package com.comandante.creeper.merchant.lockers;

import java.util.*;

public class LockerCommandRegistry {

    private final LockerCommand unknownCommand;

    public LockerCommandRegistry(LockerCommand unknownCommand) {
        this.unknownCommand = unknownCommand;
    }

    private final HashMap<String, LockerCommand> lockerCommands = new HashMap<>();

    public void addCommand(LockerCommand command) {
        List<String> validTriggers = command.validTriggers;
        for (String trigger: validTriggers) {
            lockerCommands.put(trigger, command);
        }
    }

    public LockerCommand getCommandByTrigger(String trigger) {
        for (Map.Entry<String, LockerCommand> next : lockerCommands.entrySet()) {
            if (trigger.equals(next.getKey())) {
                return next.getValue();
            }
        }
        return unknownCommand;
    }

    public Set<LockerCommand> getLockerCommands() {
        Set<LockerCommand> creeperCommandUniq = new HashSet<LockerCommand>(lockerCommands.values());
        return creeperCommandUniq;
    }
}