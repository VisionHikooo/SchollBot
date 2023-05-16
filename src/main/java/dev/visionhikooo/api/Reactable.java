package dev.visionhikooo.api;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public interface Reactable {
    public void onReact(String codePoint, Member member, GuildMessageChannelUnion channel, boolean onOff);
}
