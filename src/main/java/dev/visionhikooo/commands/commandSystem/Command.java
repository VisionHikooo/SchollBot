package dev.visionhikooo.commands.commandSystem;

import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public interface Command {
    public abstract boolean onCommand(String[] args, Member member, GuildMessageChannelUnion channel);
    public abstract String getDescription();
    public abstract String getUsage();
}
