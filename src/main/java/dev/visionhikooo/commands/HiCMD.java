package dev.visionhikooo.commands;

import dev.visionhikooo.commands.commandSystem.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public class HiCMD implements Command {
    @Override
    public boolean onCommand(String[] args, Member member, GuildMessageChannelUnion channel) {
        channel.sendMessage("Hallo, " + member.getEffectiveName()).queue();
        return true;
    }

    @Override
    public String getDescription() {
        return "Veranlasst den Bot, dich zurück zu grüßen!";
    }

    @Override
    public String getUsage() {
        return "!Bot Hi";
    }
}
