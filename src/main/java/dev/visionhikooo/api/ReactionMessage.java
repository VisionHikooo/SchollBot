package dev.visionhikooo.api;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

public class ReactionMessage {
    private long messageID;
    private Reactable reactable;

    public ReactionMessage(long messageID,Reactable reactable) {
        this.reactable = reactable;
        this.messageID = messageID;
    }

    public long getMessageID() {
        return messageID;
    }

    public void onReact(String codepoint, Member member, GuildMessageChannelUnion channel, boolean onOff) {
        reactable.onReact(codepoint, member, channel, onOff);
    }
}
