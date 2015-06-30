package com.comandante.creeper.server;

import com.comandante.creeper.CreeperEntry;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.fight.FightResults;
import com.comandante.creeper.merchant.Merchant;
import com.google.common.base.Optional;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreeperSession {

    private Optional<String> username = Optional.absent();
    private Optional<String> password = Optional.absent();
    private boolean isAuthed = false;
    private Optional<Future<FightResults>> activeFight = Optional.absent();
    private AtomicBoolean isAbleToDoAbility = new AtomicBoolean(false);
    private Optional<CreeperEntry<UUID, Command>> grabMultiLineInput = Optional.absent();
    private Optional<CreeperEntry<Merchant, SimpleChannelUpstreamHandler>> grabMerchant = Optional.absent();
    private String lastMessage;
    private final Long initialLoginTime;
    private Long lastActivity;

    public CreeperSession() {
        long currentTime = System.currentTimeMillis();
        this.initialLoginTime = currentTime;
        this.lastActivity = currentTime;
    }

    State state;

    public enum State {
        promptedForPassword,
        promptedForUsername,
        newUserPromptedForUsername,
        newUserPromptedForPassword,
        newUserRegCompleted,
        authed
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    public Optional<String> getPassword() {
        return password;
    }

    public void setPassword(Optional<String> password) {
        this.password = password;
    }

    public boolean isAuthed() {
        return isAuthed;
    }

    public void setAuthed(boolean isAuthed) {
        this.isAuthed = isAuthed;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setIsAbleToDoAbility(boolean b) {
        this.isAbleToDoAbility.set(b);
    }

    public boolean IsAbleToDoAbility() {
        return this.isAbleToDoAbility.get();
    }

    public Optional<CreeperEntry<UUID, Command>> getGrabMultiLineInput() {
        return grabMultiLineInput;
    }

    public void setGrabMultiLineInput(Optional<CreeperEntry<UUID, Command>> grabMultiLineInput) {
        this.grabMultiLineInput = grabMultiLineInput;
    }

    public AtomicBoolean getIsAbleToDoAbility() {
        return isAbleToDoAbility;
    }

    public void setIsAbleToDoAbility(AtomicBoolean isAbleToDoAbility) {
        this.isAbleToDoAbility = isAbleToDoAbility;
    }

    public Optional<CreeperEntry<Merchant, SimpleChannelUpstreamHandler>> getGrabMerchant() {
        return grabMerchant;
    }

    public void setGrabMerchant(Optional<CreeperEntry<Merchant, SimpleChannelUpstreamHandler>> grabMerchant) {
        this.grabMerchant = grabMerchant;
    }

    public Long getInitialLoginTime() {
        return initialLoginTime;
    }

    public Long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Long lastActivity) {
        this.lastActivity = lastActivity;
    }
}

