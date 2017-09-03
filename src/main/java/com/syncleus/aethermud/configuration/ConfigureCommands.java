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
package com.syncleus.aethermud.configuration;

import com.syncleus.aethermud.command.CommandRegistry;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.merchant.lockers.GetCommand;
import com.syncleus.aethermud.merchant.lockers.LockerCommandRegistry;
import com.syncleus.aethermud.merchant.lockers.PutCommand;
import com.syncleus.aethermud.merchant.lockers.QueryCommand;
import com.syncleus.aethermud.merchant.playerclass_selector.PlayerClassCommandRegistry;
import com.syncleus.aethermud.command.commands.*;
import com.syncleus.aethermud.command.commands.admin.*;
import com.syncleus.aethermud.merchant.bank.commands.*;
import com.syncleus.aethermud.merchant.bank.commands.DoneCommand;

public class ConfigureCommands {

    public static CommandRegistry commandRegistry;

    public static BankCommandRegistry bankCommandRegistry;

    public static void configureBankCommands(GameManager gameManager) {
        bankCommandRegistry = new BankCommandRegistry(new com.syncleus.aethermud.merchant.bank.commands.UnknownCommand(gameManager));
        bankCommandRegistry.addCommand(new AccountQueryCommand(gameManager));
        bankCommandRegistry.addCommand(new DepositCommand(gameManager));
        bankCommandRegistry.addCommand(new WithdrawalCommand(gameManager));
        bankCommandRegistry.addCommand(new DoneCommand(gameManager));
    }

    public static LockerCommandRegistry lockerCommandRegistry;

    public static void configureLockerCommands(GameManager gameManager) {
        lockerCommandRegistry = new LockerCommandRegistry(new com.syncleus.aethermud.merchant.lockers.UnknownCommand(gameManager));
        lockerCommandRegistry.addCommand(new PutCommand(gameManager));
        lockerCommandRegistry.addCommand(new GetCommand(gameManager));
        lockerCommandRegistry.addCommand(new QueryCommand(gameManager));
        lockerCommandRegistry.addCommand(new com.syncleus.aethermud.merchant.lockers.DoneCommand(gameManager));
    }
    public static PlayerClassCommandRegistry playerClassCommandRegistry;

    public static void configurePlayerClassSelector(GameManager gameManager) {
       playerClassCommandRegistry = new PlayerClassCommandRegistry(gameManager);
    }

    public static void configure(GameManager gameManager) {
        commandRegistry = new CommandRegistry(new com.syncleus.aethermud.command.commands.UnknownCommand(gameManager));
        commandRegistry.addCommand(new DropCommand(gameManager));
        commandRegistry.addCommand(new GossipCommand(gameManager));
        commandRegistry.addCommand(new InventoryCommand(gameManager));
        commandRegistry.addCommand(new FightKillCommand(gameManager));
        commandRegistry.addCommand(new LookCommand(gameManager));
        commandRegistry.addCommand(new MovementCommand(gameManager));
        commandRegistry.addCommand(new PickUpCommand(gameManager));
        commandRegistry.addCommand(new SayCommand(gameManager));
        commandRegistry.addCommand(new TellCommand(gameManager));
        commandRegistry.addCommand(new UseCommand(gameManager));
        commandRegistry.addCommand(new WhoamiCommand(gameManager));
        commandRegistry.addCommand(new WhoCommand(gameManager));
        commandRegistry.addCommand(new DescriptionCommand(gameManager));
        commandRegistry.addCommand(new TitleCommand(gameManager));
        commandRegistry.addCommand(new TagRoomCommand(gameManager));
        commandRegistry.addCommand(new SaveWorldCommand(gameManager));
        commandRegistry.addCommand(new BuildCommand(gameManager));
        commandRegistry.addCommand(new MapCommand(gameManager));
        commandRegistry.addCommand(new AreaCommand(gameManager));
        commandRegistry.addCommand(new HelpCommand(gameManager));
        commandRegistry.addCommand(new LootCommand(gameManager));
        commandRegistry.addCommand(new GoldCommand(gameManager));
        commandRegistry.addCommand(new InfoCommand(gameManager));
        commandRegistry.addCommand(new TeleportCommand(gameManager));
        commandRegistry.addCommand(new TalkCommand(gameManager));
        commandRegistry.addCommand(new EquipCommand(gameManager));
        commandRegistry.addCommand(new UnequipCommand(gameManager));
        commandRegistry.addCommand(new QuitCommand(gameManager));
        commandRegistry.addCommand(new GiveGoldCommand(gameManager));
        commandRegistry.addCommand(new NexusCommand(gameManager));
        commandRegistry.addCommand(new ColorsCommand(gameManager));
        commandRegistry.addCommand(new XpCommand(gameManager));
        commandRegistry.addCommand(new CastCommand(gameManager));
        commandRegistry.addCommand(new CountdownCommand(gameManager));
        commandRegistry.addCommand(new ReloadNpcsCommand(gameManager));
        commandRegistry.addCommand(new UsersCommand(gameManager));
        commandRegistry.addCommand(new SpawnCommand(gameManager));
        commandRegistry.addCommand(new ForageCommand(gameManager));
        commandRegistry.addCommand(new RecentChangesCommand(gameManager));
        commandRegistry.addCommand(new BounceIrcBotCommand(gameManager));
        commandRegistry.addCommand(new OpenCommand(gameManager));
        commandRegistry.addCommand(new RecentGossipCommand(gameManager));
        commandRegistry.addCommand(new NotablesCommand(gameManager));
        commandRegistry.addCommand(new NpcLocationCommand(gameManager));
        commandRegistry.addCommand(new TimeCommand(gameManager));
        commandRegistry.addCommand(new ShowCommand(gameManager));
        commandRegistry.addCommand(new CoolDownCommand(gameManager));
        commandRegistry.addCommand(new SystemInfo(gameManager));
        commandRegistry.addCommand(new SetCommand(gameManager));
        commandRegistry.addCommand(new DelCommand(gameManager));
        commandRegistry.addCommand(new OpCommand(gameManager));
        commandRegistry.addCommand(new KillTallyCommand(gameManager));
        commandRegistry.addCommand(new CompareCommand(gameManager));
        commandRegistry.addCommand(new CardsCommand(gameManager));
        commandRegistry.addCommand(new SpellsCommand(gameManager));
        commandRegistry.addCommand(new LeaveCommand(gameManager));
        commandRegistry.addCommand(new BackCommand(gameManager));
        commandRegistry.addCommand(new RecallCommand(gameManager));
        commandRegistry.addCommand(new ToggleChatCommand(gameManager));
        commandRegistry.addCommand(new LoadNpcCommand(gameManager));
        commandRegistry.addCommand(new LoadItemCommand(gameManager));
        commandRegistry.addCommand(new LoadMerchantCommand(gameManager));
        commandRegistry.addCommand(new RestartCommand(gameManager));
        commandRegistry.addCommand(new GiveHealthCommand(gameManager));
    }
}
