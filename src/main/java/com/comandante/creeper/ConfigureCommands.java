package com.comandante.creeper;

import com.comandante.creeper.command.*;
import com.comandante.creeper.command.UnknownCommand;
import com.comandante.creeper.command.admin.*;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.bank.commands.*;
import com.comandante.creeper.server.CreeperCommandRegistry;

public class ConfigureCommands {

    public static CreeperCommandRegistry creeperCommandRegistry;

    public static BankCommandRegistry bankCommandRegistry;

    public static void configureBankCommands(GameManager gameManager) {
        bankCommandRegistry = new BankCommandRegistry(new com.comandante.creeper.merchant.bank.commands.UnknownCommand(gameManager));
        bankCommandRegistry.addCommand(new AccountQueryCommand(gameManager));
        bankCommandRegistry.addCommand(new DepositCommand(gameManager));
        bankCommandRegistry.addCommand(new WithdrawalCommand(gameManager));
        bankCommandRegistry.addCommand(new DoneCommand(gameManager));
    }

    public static void configure(GameManager gameManager) {
        creeperCommandRegistry = new CreeperCommandRegistry(new UnknownCommand(gameManager));
        creeperCommandRegistry.addCommand(new DropCommand(gameManager));
        creeperCommandRegistry.addCommand(new GossipCommand(gameManager));
        creeperCommandRegistry.addCommand(new InventoryCommand(gameManager));
        creeperCommandRegistry.addCommand(new FightKillCommand(gameManager));
        creeperCommandRegistry.addCommand(new LookCommand(gameManager));
        creeperCommandRegistry.addCommand(new MovementCommand(gameManager));
        creeperCommandRegistry.addCommand(new PickUpCommand(gameManager));
        creeperCommandRegistry.addCommand(new SayCommand(gameManager));
        creeperCommandRegistry.addCommand(new TellCommand(gameManager));
        creeperCommandRegistry.addCommand(new UseCommand(gameManager));
        creeperCommandRegistry.addCommand(new WhoamiCommand(gameManager));
        creeperCommandRegistry.addCommand(new WhoCommand(gameManager));
        creeperCommandRegistry.addCommand(new DescriptionCommand(gameManager));
        creeperCommandRegistry.addCommand(new TitleCommand(gameManager));
        creeperCommandRegistry.addCommand(new TagRoomCommand(gameManager));
        creeperCommandRegistry.addCommand(new SaveWorldCommand(gameManager));
        creeperCommandRegistry.addCommand(new BuildCommand(gameManager));
        creeperCommandRegistry.addCommand(new MapCommand(gameManager));
        creeperCommandRegistry.addCommand(new AreaCommand(gameManager));
        creeperCommandRegistry.addCommand(new HelpCommand(gameManager));
        creeperCommandRegistry.addCommand(new LootCommand(gameManager));
        creeperCommandRegistry.addCommand(new GoldCommand(gameManager));
        creeperCommandRegistry.addCommand(new InfoCommand(gameManager));
        creeperCommandRegistry.addCommand(new TeleportCommand(gameManager));
        creeperCommandRegistry.addCommand(new TalkCommand(gameManager));
        creeperCommandRegistry.addCommand(new EquipCommand(gameManager));
        creeperCommandRegistry.addCommand(new UnequipCommand(gameManager));
        creeperCommandRegistry.addCommand(new QuitCommand(gameManager));
        creeperCommandRegistry.addCommand(new GiveGoldCommand(gameManager));
        creeperCommandRegistry.addCommand(new NexusCommand(gameManager));
        creeperCommandRegistry.addCommand(new ColorsCommand(gameManager));
        creeperCommandRegistry.addCommand(new XpCommand(gameManager));
        creeperCommandRegistry.addCommand(new CastCommand(gameManager));
        creeperCommandRegistry.addCommand(new CountdownCommand(gameManager));
        creeperCommandRegistry.addCommand(new ReloadNpcsCommand(gameManager));
        creeperCommandRegistry.addCommand(new UsersCommand(gameManager));

    }
}
