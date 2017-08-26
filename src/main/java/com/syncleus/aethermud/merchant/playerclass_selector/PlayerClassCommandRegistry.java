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
package com.syncleus.aethermud.merchant.playerclass_selector;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerClassCommandRegistry {

    private final PlayerClassCommand unknownCommand;
    private final GameManager gameManager;

    public PlayerClassCommandRegistry(GameManager gameManager) {
        this.gameManager = gameManager;
        this.unknownCommand = new UnknownCommand(gameManager);
    }

    public PlayerClassCommand getCommandByTrigger(String trigger) {
        if (trigger.equalsIgnoreCase("leave")) {
            return new LeaveCommand(gameManager);
        }
        List<PlayerClass> matchedClasses = Arrays.stream(PlayerClass.values()).filter(playerClass -> playerClass.getIdentifier().equalsIgnoreCase(trigger)).collect(Collectors.toList());
        if (matchedClasses.size() > 0) {
            return new ChooseClassCommand(matchedClasses.get(0), gameManager);
        }
        return unknownCommand;
    }

}
