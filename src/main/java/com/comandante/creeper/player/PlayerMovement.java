package com.comandante.creeper.player;

import com.comandante.creeper.command.MovementCommand;

public class PlayerMovement {

    private final Player player;
    private final Integer sourceRoomId;
    private final Integer destinationRoomId;
    private final String roomExitMessage;
    private final String returnDirection;

    public PlayerMovement(Player player,
                          Integer sourceRoomId,
                          Integer destinationRoomId,
                          String roomExitMessage,
                          String returnDirection) {
        this.player = player;
        this.sourceRoomId = sourceRoomId;
        this.destinationRoomId = destinationRoomId;
        this.roomExitMessage = roomExitMessage;
        this.returnDirection = returnDirection;

    }

    public String getReturnDirection() {
        return returnDirection;
    }

    public Integer getSourceRoomId() {
        return sourceRoomId;
    }

    public Integer getDestinationRoomId() {
        return destinationRoomId;
    }

    public Player getPlayer() {
        return player;
    }

    public String getRoomExitMessage() {
        return roomExitMessage;
    }
}
