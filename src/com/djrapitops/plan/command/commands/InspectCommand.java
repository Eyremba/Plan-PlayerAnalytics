package com.djrapitops.plan.command.commands;

import com.djrapitops.plan.Plan;
import com.djrapitops.plan.command.CommandType;
import com.djrapitops.plan.command.SubCommand;
import com.djrapitops.plan.command.utils.DataFormatUtils;
import com.djrapitops.plan.command.utils.DataUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.djrapitops.plan.UUIDFetcher;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InspectCommand extends SubCommand {

    private Plan plugin;

    public InspectCommand(Plan plugin) {
        super("inspect", "plan.inspect", "Inspect data /plan <player> [-a, -r].", CommandType.CONSOLE_WITH_ARGUMENTS);

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String playerName = DataUtils.getPlayerDisplayname(args, sender);
        if (this.plugin.getHooks().isEmpty()) {
            this.plugin.logError("noHookedPluginsError on InspectCommand");

            this.plugin.logToFile("INSPECT\nnoHookedPluginsError on InspectCommand");

            return false;
        }

        boolean allData = false;
        boolean format = true;
        for (String arg : args) {
            if (arg.toLowerCase().equals("-a")) {
                allData = true;
            }
            if (arg.toLowerCase().equals("-r")) {
                format = false;
            }
        }
        Date refreshDate = new Date();
        HashMap<String, String> data = DataUtils.getData(allData, playerName);
        if (format && !data.isEmpty()) {
            data = DataFormatUtils.removeExtraDataPoints(data);
        }
        if (data.isEmpty()) {
            data.put("ERR-NO RESULTS", "No results were found.");

            plugin.logToFile("INSPECT-Results\nNo results were found for: " + playerName);

        }

        List<String[]> dataList = DataFormatUtils.turnDataHashMapToSortedListOfArrays(data);

        ChatColor operatorColor = ChatColor.DARK_GREEN;
        ChatColor textColor = ChatColor.GRAY;

        //header
        sender.sendMessage(textColor + "-- [" + operatorColor + "PLAN - Inspect results: " + playerName +" - took "+DataFormatUtils.formatTimeAmountSinceDate(refreshDate, new Date())+ textColor + "] --");

        for (String[] dataString : dataList) {
            sender.sendMessage("" + operatorColor + dataString[0].charAt(4) + dataString[0].toLowerCase().substring(5) + ": " + textColor + dataString[1]);
        }
        sender.sendMessage(textColor + "-- o --");
        return true;
    }

    // Use DataUtils.getData instead
    @Deprecated
    public HashMap<String, String> getData(boolean allData, String playerName) {
        return DataUtils.getData(allData, playerName);
    }

    // Use DataFormatUtils.removeExtraDataPoints instead
    @Deprecated
    public HashMap<String, String> format(HashMap<String, String> data) throws NumberFormatException {
        return DataFormatUtils.removeExtraDataPoints(data);
    }

    // Use DataUtils.getPlayerDisplayname instead
    @Deprecated
    private String getPlayerDisplayname(String[] args, CommandSender sender) {
        return DataUtils.getPlayerDisplayname(args, sender);
    }
}
