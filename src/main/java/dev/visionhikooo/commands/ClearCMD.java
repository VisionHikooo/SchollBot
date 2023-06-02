package dev.visionhikooo.commands;

import dev.visionhikooo.commands.commandSystem.Command;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

import java.util.List;

public class ClearCMD implements Command {
    @Override
    public boolean onCommand(String[] args, Member member, GuildMessageChannelUnion channel) {
        if (args.length == 0)
            return false;

        /*
        if (!member.getRoles().contains(channel.getGuild().getRoleById(SchollBot.getModID())) && !member.getRoles().contains(channel.getGuild().getRoleById(SchollBot.getAdminID())))
            return false;
         */

        TextChannel textChannel = channel.asTextChannel();

        try {
            int num = Integer.valueOf(args[0]);
            textChannel.getHistory().retrievePast(num + 1).queue(messages -> {
                for (int i = 0; i < messages.size(); i++) {
                    messages.get(i).delete().queue();
                }
            });
        } catch (NumberFormatException e) {
            channel.sendMessage("Bitte gib eine gültige Zahl oder all an!").queue();
            System.out.println("Numberformat");
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getUsage() {
        return "";
    }
}
