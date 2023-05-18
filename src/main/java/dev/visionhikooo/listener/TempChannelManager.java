package dev.visionhikooo.listener;

import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

import java.util.ArrayList;

public class TempChannelManager extends Listener {

    private ArrayList<Long> tempChannels;
    private Guild guild;

    public TempChannelManager(SchollBot bot) {
        super(bot);
        tempChannels = new ArrayList<>();
    }

    public void createTempChannel(Member member, String name, GuildMessageChannelUnion channel) {
        if (guild == null)
            guild = member.getGuild();
        Category cat = guild.getCategoryById(1106169535553355797L);
        assert cat != null;
        VoiceChannel voice = cat.createVoiceChannel(name).complete();
        voice.getManager().setUserLimit(5);
        tempChannels.add(voice.getIdLong());
        try {
            member.getGuild().moveVoiceMember(member, voice).queue();
        } catch (IllegalStateException e) {
            voice.delete().queue();
            channel.sendMessage("Du musst in einem VoiceChannel sein!").queue();
        }
    }

    /*
    * Wenn ein Member einen Channel verlässt oder joined
    * */
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        AudioChannelUnion channel = event.getChannelLeft();
        if ( channel != null && tempChannels.contains(channel.getIdLong())) {
            if (channel.getMembers().size() == 0) {
                tempChannels.remove(channel.getIdLong());
                channel.delete().queue();
            }
        }
    }

    public void onShutdown() {
        for (Long l : tempChannels)
            guild.getGuildChannelById(l).delete().queue();
    }
}
