package com.iridium.iridiumteams;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.iridium.testplugin.TestPlugin;
import com.iridium.testplugin.TestTeam;
import com.iridium.testplugin.User;

public class UserBuilder {
    private final PlayerMock playerMock;
    private final User user;

    public UserBuilder(ServerMock serverMock) {
        this.playerMock = serverMock.addPlayer();
        this.user = TestPlugin.getInstance().getUserManager().getUser(playerMock);
    }

    public UserBuilder(ServerMock serverMock, String playerName) {
        this.playerMock = serverMock.addPlayer(playerName);
        this.user = TestPlugin.getInstance().getUserManager().getUser(playerMock);
    }

    public UserBuilder withTeam(TestTeam testTeam) {
        user.setTeam(testTeam);
        return this;
    }

    public UserBuilder withRank(int rank) {
        user.setUserRank(rank);
        return this;
    }

    public UserBuilder setBypassing() {
        user.setBypassing(true);
        return this;
    }

    public PlayerMock build() {
        return playerMock;
    }

}