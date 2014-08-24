package com.comandante.creeper.model;

import com.google.common.base.Optional;

public class BasicRoom extends Room {
    @Override
    public void run() {
        super.run();
        System.out.println(getRoomTitle() + " tick.");
    }

    public BasicRoom(Integer roomId, String roomTitle, Optional<Integer> northId, Optional<Integer> westId, Optional<Integer> eastId, Optional<Integer> southId, Optional<Integer> upId, Optional<Integer> downId, String roomDescription) {
        super(roomId, roomTitle, northId, westId, eastId, southId, upId, downId, roomDescription);
    }
}