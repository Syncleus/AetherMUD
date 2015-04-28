package com.comandante.creeper.player;

public enum PlayerRole {

    ADMIN("administrator", 1),
    MORTAL("mortal", 2);


    private final String roleType;
    private final int roleId;

    PlayerRole(String roleType, int roleId) {
        this.roleType = roleType;
        this.roleId = roleId;
    }
}
