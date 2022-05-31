package com.iridium.iridiumteams.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.database.IridiumUser;
import com.iridium.iridiumteams.database.Team;
import com.iridium.iridiumteams.gui.ConfirmationGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransferCommand<T extends Team, U extends IridiumUser<T>> extends Command<T, U> {

    public TransferCommand() {
        super(Collections.singletonList("transfer"), "Transfer Team ownership to another player", "%prefix% &7/team transfer <player>", "");
    }

    @Override
    public void execute(U user, T team, String[] args, IridiumTeams<T, U> iridiumTeams) {
        Player player = user.getPlayer();
        if (args.length != 1) {
            player.sendMessage(StringUtils.color(syntax.replace("%prefix%", iridiumTeams.getConfiguration().prefix)));
            return;
        }
        if (user.getUserRank() != -1 && !user.isBypassing()) {
            player.sendMessage(StringUtils.color(iridiumTeams.getMessages().mustBeOwnerToTransfer
                    .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
            ));
            return;
        }
        Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(StringUtils.color(iridiumTeams.getMessages().notAPlayer
                    .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
            ));
            return;
        }
        U targetUser = iridiumTeams.getUserManager().getUser(targetPlayer);
        if (targetUser.getTeamID() != team.getId()) {
            player.sendMessage(StringUtils.color(iridiumTeams.getMessages().userNotInYourTeam
                    .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
            ));
            return;
        }
        if (targetPlayer.getUniqueId().equals(player.getUniqueId()) && !user.isBypassing()) {
            player.sendMessage(StringUtils.color(iridiumTeams.getMessages().cannotTransferToYourself
                    .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
            ));
            return;
        }

        player.openInventory(new ConfirmationGUI<>(() -> {
            targetUser.setUserRank(-1);
            iridiumTeams.getTeamManager().getTeamMembers(team).forEach(user1 -> {
                if (user1.getUserRank() == -1 && user1 != targetUser) {
                    user1.setUserRank(iridiumTeams.getUserRanks().keySet().stream().max(Integer::compareTo).orElse(1));
                }
                Player p = user1.getPlayer();
                if (p != null) {
                    p.sendMessage(StringUtils.color(iridiumTeams.getMessages().ownershipTransferred
                            .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
                            .replace("%old_owner%", user.getName())
                            .replace("%new_owner%", targetUser.getName())
                    ));
                }
            });
        }, iridiumTeams).getInventory());
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args, IridiumTeams<T, U> iridiumTeams) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}