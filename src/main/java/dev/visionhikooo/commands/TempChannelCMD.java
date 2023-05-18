package dev.visionhikooo.commands;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.api.SchoolClass;
import dev.visionhikooo.commands.commandSystem.Command;
import dev.visionhikooo.listener.TempChannelManager;
import dev.visionhikooo.main.FileManager;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public class TempChannelCMD implements Command {

    private TempChannelManager manager;
    private SchollBot bot;

    public TempChannelCMD(SchollBot bot) {
        this.bot = bot;
        manager = bot.getTempChannelManager();
    }

    @Override
    public boolean onCommand(String[] args, Member member, GuildMessageChannelUnion channel) {
        if (args.length == 0) {
            channel.sendMessage("Bitte gib einen Namen für den Channel an!").queue();
            return false;
        }

        if (!bot.getFileManager().hasID(FileManager.Options.TEMP_CAT)) {
            channel.sendMessage("Es ist aktuell keine Kategorie für temporäre Channel vorhanden. Bitte wende dich an einen Admin!").queue();
            SchollBot.sendConsoleMessage("Es wurde keine Kategorie für temporäre Channel erstellt!", Debug.LOW);
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
