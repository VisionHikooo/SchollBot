package dev.visionhikooo.commands;

import dev.visionhikooo.commands.commandSystem.Command;
import dev.visionhikooo.listener.TempChannelManager;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public class TempChannelCMD implements Command {

    private TempChannelManager manager;

    public TempChannelCMD(SchollBot bot) {
        manager = bot.getTempChannelManager();
    }

    @Override
    public boolean onCommand(String[] args, Member member, GuildMessageChannelUnion channel) {
        if (args.length == 0) {
            channel.sendMessage("Bitte gib einen Namen für den Channel an!").queue();
            return false;
        }

        manager.createTempChannel(member, args[0], channel);
        return true;
    }

    @Override
    public String getDescription() {
        return "Erstellt einen temporären Channel für den User";
    }

    @Override
    public String getUsage() {
        return "!Bot temp [Name]";
    }
}
