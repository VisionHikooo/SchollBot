package dev.visionhikooo.api;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public class ReactionMessage {
    private Message message;
    private Reactable reactable;

    public ReactionMessage(Message message,Reactable reactable) {
        this.reactable = reactable;
        this.message = message;
    }

    public void onReact(String codepoint, Member member, GuildMessageChannelUnion channel) {
        reactable.onReact(codepoint, member, channel);
    }
}
