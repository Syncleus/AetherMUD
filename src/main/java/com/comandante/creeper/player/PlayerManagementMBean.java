package com.comandante.creeper.player;

import java.util.Map;

public interface PlayerManagementMBean {

    void setMarkForDelete(boolean isMark);

    boolean getMarkForDelete();

    public long getGold();

    public long getGoldInBankAmount();

    public void setGoldInBankAmount(long amt);

    public void setGold(long amt);

    public long getHealth();

    public long getMana();

    public void setHealth(long amt);

    public void setMana(long amt);

    public void sendMessageFromGod(String message);

    public void setExperience(long amt);

    public long getExperience();

    public void setRoles(String roles);

    public String getRoles();

    public Map<String, String> getInventory();

    public String getPassword();

    public void setPassword(String password);

    Map<String, String> getLockerInventory();

    public String createItemInInventory(int itemTypeId);

    public void setPlayerClass(String playerClassName);

    public String getPlayerClass();

    public void clearAllCoolDowns();
}
