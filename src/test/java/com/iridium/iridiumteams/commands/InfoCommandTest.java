package com.iridium.iridiumteams.commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumteams.TeamBuilder;
import com.iridium.iridiumteams.UserBuilder;
import com.iridium.testplugin.TestPlugin;
import com.iridium.testplugin.TestTeam;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class InfoCommandTest {

    private ServerMock serverMock;

    @BeforeEach
    public void setup() {
        this.serverMock = MockBukkit.mock();
        MockBukkit.load(TestPlugin.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void executeInfoCommandNoTeam() {
        PlayerMock playerMock = new UserBuilder(serverMock).build();

        serverMock.dispatchCommand(playerMock, "test info");

        playerMock.assertSaid(StringUtils.color(TestPlugin.getInstance().getMessages().dontHaveTeam
                .replace("%prefix%", TestPlugin.getInstance().getConfiguration().prefix)
        ));
        playerMock.assertNoMoreSaid();
    }

    @Test
    public void executeInfoCommandWithTeam() {
        TestTeam team = new TeamBuilder().build();
        PlayerMock playerMock = new UserBuilder(serverMock).withTeam(team).build();

        serverMock.dispatchCommand(playerMock, "test info");

        List<Placeholder> placeholderList = TestPlugin.getInstance().getTeamsPlaceholderBuilder().getPlaceholders(team);
        playerMock.assertSaid(StringUtils.color(StringUtils.getCenteredMessage(StringUtils.processMultiplePlaceholders(TestPlugin.getInstance().getConfiguration().teamInfoTitle, placeholderList), TestPlugin.getInstance().getConfiguration().teamInfoTitleFiller)));
        for (String line : TestPlugin.getInstance().getConfiguration().teamInfo) {
            playerMock.assertSaid(StringUtils.color(StringUtils.processMultiplePlaceholders(line, placeholderList)));
        }
        playerMock.assertNoMoreSaid();
    }

    @Test
    public void executeInfoCommandTeamDoesntExist() {
        PlayerMock playerMock = new UserBuilder(serverMock).build();

        serverMock.dispatchCommand(playerMock, "test info InvalidTeamName");

        playerMock.assertSaid(StringUtils.color(TestPlugin.getInstance().getMessages().teamDoesntExistByName
                .replace("%prefix%", TestPlugin.getInstance().getConfiguration().prefix)
        ));
        playerMock.assertNoMoreSaid();
    }

    @Test
    public void executeInfoCommandWithTeamName() {
        TestTeam team = new TeamBuilder().build();
        PlayerMock playerMock = new UserBuilder(serverMock).withTeam(team).build();

        serverMock.dispatchCommand(playerMock, "test info "+team.getName());

        List<Placeholder> placeholderList = TestPlugin.getInstance().getTeamsPlaceholderBuilder().getPlaceholders(team);
        playerMock.assertSaid(StringUtils.color(StringUtils.getCenteredMessage(StringUtils.processMultiplePlaceholders(TestPlugin.getInstance().getConfiguration().teamInfoTitle, placeholderList), TestPlugin.getInstance().getConfiguration().teamInfoTitleFiller)));
        for (String line : TestPlugin.getInstance().getConfiguration().teamInfo) {
            playerMock.assertSaid(StringUtils.color(StringUtils.processMultiplePlaceholders(line, placeholderList)));
        }
        playerMock.assertNoMoreSaid();
    }
}