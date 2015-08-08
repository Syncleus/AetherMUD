package com.comandante.creeper.player;

import java.util.List;

public interface PlayerManagementMBean {

    public void toggleMarkForDelete();

    public boolean isMarkedForDelete();

    public int getGold();

    public int getGoldInBankAmount();

    public void setGoldInBankAmount(int amt);

    public void setGold(int amt);

    public int getHealth();

    public int getMana();

    public void setHealth(int amt);

    public void setMana(int amt);

    public void sendMessageFromGod(String message);

}
