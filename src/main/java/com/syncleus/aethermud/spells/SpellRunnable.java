/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
package com.syncleus.aethermud.spells;


import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.player.Player;

import java.util.Optional;

public interface SpellRunnable {
    void run(Player sourcePlayer, Optional<NpcSpawn> destinationNpc, Optional<Player> destinationPlayer, GameManager gameManager);
    String getName();
}
