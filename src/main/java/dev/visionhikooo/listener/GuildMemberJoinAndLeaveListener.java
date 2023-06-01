package dev.visionhikooo.listener;

import dev.visionhikooo.main.SchollBot;
import dev.visionhikooo.features.surveysAndStatistics.StatistikManager;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

public class GuildMemberJoinAndLeaveListener extends Listener {

    public GuildMemberJoinAndLeaveListener(SchollBot schollBot) {
        super(schollBot);
    }

    /*
    * Wenn ein Member auf den Server joined
    * */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        getSchollBot().getStatistikManager().addStatisticValue(StatistikManager.StatisticCategory.NEW_USERS);
    }


    /*
    * Wenn ein Member vom Server leaved oder gekickt wird
    * */
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        super.onGuildMemberRemove(event);
    }
}
