package com.comandante.creeper.common;


public class AttackMessage {

    private final Type type;
    private final String attackMessage;

    public AttackMessage(Type type, String attackMessage) {
        this.type = type;
        this.attackMessage = attackMessage;
    }

    public Type getType() {
        return type;
    }

    public String getAttackMessage() {
        return attackMessage;
    }

    public enum Type {
        NORMAL,
        CRITICAL
    }
}
