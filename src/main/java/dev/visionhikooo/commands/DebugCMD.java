package dev.visionhikooo.commands;

import dev.visionhikooo.commands.commandSystem.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public class DebugCMD implements Command  {

    @Override
    public boolean onCommand(String[] args, Member member, GuildMessageChannelUnion channel) {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }
}
