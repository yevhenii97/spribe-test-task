package com.project.restapi.models;

import java.util.Objects;

public class PlayerRequest {

    private Long playerId;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public PlayerRequest() {}

    public PlayerRequest(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "GetPlayerRequest{" +
                "playerId='" + playerId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlayerRequest that = (PlayerRequest) o;
        return Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId);
    }
}
