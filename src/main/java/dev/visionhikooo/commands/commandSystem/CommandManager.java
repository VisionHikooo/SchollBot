package dev.visionhikooo.commands.commandSystem;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

import java.util.HashMap;

public class CommandManager {
    public HashMap<String, Command> commands;

    public CommandManager() {
        commands = new HashMap<>();
    }

    public boolean registerCommand(String label, Command command) {
        if (knowsCommand(label)) {
            SchollBot.sendConsoleMessage("Dieser Command existiert bereits!");
            return false;
        }

        commands.put(label.toLowerCase(), command);
        return true;
    }

    public boolean knowsCommand(String label) {
        return commands.containsKey(label.toLowerCase());
    }

    public Command getCommand(String label) {
        return commands.get(label.toLowerCase());
    }

    public void executeCommand(String label, String[] args, Member member, GuildMessageChannelUnion channel) {
        if (!knowsCommand(label)) {
            channel.sendMessage("Dieser Command ist mir nicht bekannt. Bitte benutze !help").queue();
            SchollBot.sendConsoleMessage("Unbekannter Befehl wurde von " + member.getNickname() + " eingeben", Debug.NORMAL);
        } else
            getCommand(label).onCommand(args, member, channel);
    }

    public void sendHelp(String label, GuildMessageChannelUnion channel, Member member) {
        if (!knowsCommand(label)) {
            channel.sendMessage("Dieser Befehl ist mir nicht bekannt. Bitte benutze !help").queue();
            return;
        }

        channel.sendMessage(getCommand(label).getDescription()).queue();
    }

    public void sendHelp(GuildMessageChannelUnion channel) {
        String message = "";
        for (Command cmd : commands.values())
            message += (cmd.getUsage() + "\n");

        channel.sendMessage(message).queue();
    }
}
