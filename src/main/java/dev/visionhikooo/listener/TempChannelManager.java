package dev.visionhikooo.listener;

import dev.visionhikooo.main.SchollBot;
import dev.visionhikooo.features.surveysAndStatistics.StatistikManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

import java.util.LinkedList;

public class TempChannelManager extends Listener {

    private LinkedList<Long> tempChannels;
    private static LinkedList<Long> userMemory;
    private Guild guild;

    static {
        userMemory = new LinkedList<>();
    }

    public TempChannelManager(SchollBot bot) {
        super(bot);
        tempChannels = new LinkedList<>();

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
        AudioChannelUnion channelLeft = event.getChannelLeft();
        AudioChannelUnion channelJoined = event.getChannelJoined();

        if (channelLeft != null && tempChannels.contains(channelLeft.getIdLong())) {
            if (channelLeft.getMembers().size() == 0) {
                tempChannels.remove(channelLeft.getIdLong());
                channelLeft.delete().queue();
            }
        }

        // Wenn jemand gejoined ist, der bisher nicht auf dem Discord war!
        if (channelJoined != null && channelLeft == null) {
            if (!userMemory.contains(event.getMember().getIdLong())){
                getSchollBot().getStatistikManager().addStatisticValue(StatistikManager.StatisticCategory.VOICE_CHANNEL_USERS);
                userMemory.add(event.getMember().getIdLong());
            }
        }
    }

    public void onShutdown() {
        for (Long l : tempChannels)
            guild.getGuildChannelById(l).delete().queue();
    }

    public static void resetUserMemory() {
        userMemory = new LinkedList<>();
    }
}
