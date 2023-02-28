package net.portalsam.magichealth.command.item.tabcomplete;

import net.portalsam.magichealth.database.PluginLanguage;
import net.portalsam.magichealth.item.MagicHealthItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandMagicGiveItemTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> itemList = new ArrayList<>();
        List<String> onlinePlayers = new ArrayList<>();

        for(Player player : Bukkit.getOnlinePlayers()) {
            onlinePlayers.add(player.getName());
        }
        onlinePlayers.add(PluginLanguage.getMagicGiveItemListSubcommandName());

        for(ItemStack item : MagicHealthItems.getItems()) {
            itemList.add(ChatColor.stripColor(Objects.requireNonNull(item.getItemMeta()).getDisplayName().replace(" ", "_")));
        }

        switch (args.length) {
            case 1: return onlinePlayers;
            case 2: return itemList;
            case 3: return new ArrayList<>(Collections.singletonList("1"));
            default: return null;
        }

    }

}
