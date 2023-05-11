package dev.visionhikooo.commands;

import dev.visionhikooo.api.ReactionMessage;
import dev.visionhikooo.commands.commandSystem.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public class ReactionMessageCMD implements Command {

    @Override
    public boolean onCommand(String[] args, Member member, GuildMessageChannelUnion channel) {
        channel.deleteMessageById(channel.getLatestMessageIdLong()).queue();
        if (args.length == 0)
            return false;
        if (args[0].equalsIgnoreCase("rules")) {
            String messageString = "Regel 1: Sei lieb, du Arsch!";
            Message message = channel.sendMessage(messageString).complete();
        } else if (args[0].equalsIgnoreCase("classes")) {

        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Erstellt Reaction-Messages mit Funktionalität";
    }

    @Override
    public String getUsage() {
        return "!Bot react [Type]";
    }
}
