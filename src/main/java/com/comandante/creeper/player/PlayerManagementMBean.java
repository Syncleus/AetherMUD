package com.comandante.creeper.player;

import java.util.List;
import java.util.Map;

public interface PlayerManagementMBean {

    void setMarkForDelete(boolean isMark);

    boolean getMarkForDelete();

    public int getGold();

    public int getGoldInBankAmount();

    public void setGoldInBankAmount(int amt);

    public void setGold(int amt);

    public int getHealth();

    public int getMana();

    public void setHealth(int amt);

    public void setMana(int amt);

    public void sendMessageFromGod(String message);

    public void setExperience(int amt);

    public int getExperience();

    public void setRoles(String roles);

    public String getRoles();

    public Map<String, String> getInventory();
}
