package dev.visionhikooo.listener;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberJoinAndLeaveListener extends ListenerAdapter {

    /*
    * Wenn ein Member auf den Server joined
    * */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);
    }


    /*
    * Wenn ein Member vom Server leaved oder gekickt wird
    * */
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        super.onGuildMemberRemove(event);
    }
}
