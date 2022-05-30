package com.iridium.iridiumteams.gui;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.iridium.iridiumteams.TeamBuilder;
import com.iridium.testplugin.TestPlugin;
import com.iridium.testplugin.TestTeam;
import com.iridium.testplugin.User;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MembersGUITest {

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
    public void membersGUIIsEmpty() {
        TestTeam testTeam = new TeamBuilder().build();
        MembersGUI<TestTeam, User> membersGUI = new MembersGUI<>(testTeam, TestPlugin.getInstance());
        Inventory inventory = membersGUI.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            assertEquals(Material.BLACK_STAINED_GLASS_PANE, inventory.getContents()[i].getType());
        }
    }

    @Test
    public void membersGUIHasFivePlayers() {
        TestTeam testTeam = new TeamBuilder().withMembers(5, serverMock).build();
        MembersGUI<TestTeam, User> membersGUI = new MembersGUI<>(testTeam, TestPlugin.getInstance());
        Inventory inventory = membersGUI.getInventory();
        for (int i = 0; i < 5; i++) {
            assertEquals(Material.PLAYER_HEAD, inventory.getContents()[i].getType());
        }
        for (int i = 5; i < inventory.getSize(); i++) {
            assertEquals(Material.BLACK_STAINED_GLASS_PANE, inventory.getContents()[i].getType());
        }
    }
}