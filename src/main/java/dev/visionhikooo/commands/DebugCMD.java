package dev.visionhikooo.commands;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.commands.commandSystem.Command;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public class DebugCMD implements Command  {

    //!bot debug high
    @Override
    public boolean onCommand(String[] args, Member member, GuildMessageChannelUnion channel) {
        if (member==null || !member.isOwner())
            return false;
        if (args.length == 0) {
            SchollBot.setSendDebugToChannel(!SchollBot.isSendDebugToChannel());
            channel.sendMessage("Der Debug wird ab jetzt " + (SchollBot.isSendDebugToChannel()? "":"nicht mehr") + " in den Channel geschickt!").queue();
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                SchollBot.setSendDebugToChannel(true);
                channel.sendMessage("Der Debug wird ab jetzt in den Channel geschickt!").queue();
            } else if (args[0].equalsIgnoreCase("off")) {
                SchollBot.setSendDebugToChannel(false);
                channel.sendMessage("Der Debug wird jetzt nicht mehr in den Channel geschickt!").queue();
            } else {
                try {
                    Debug d = SchollBot.getDebug();
                    SchollBot.setDebug(Debug.valueOf(args[0]));
                    channel.sendMessage("Der Debug wurde erfolgreich von " + d + " auf " + SchollBot.getDebug() + " geändert").queue();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Kann den Debug-Status ändern";
    }

    @Override
    public String getUsage() {
        return null;
    }
}
