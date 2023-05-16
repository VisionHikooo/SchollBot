package dev.visionhikooo.listener;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.api.ReactionMessage;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.swing.text.Utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GuildReactionManager extends Listener {

    private HashMap<Long, ReactionMessage> reactionMessages;

    public GuildReactionManager(SchollBot bot) {
        super(bot);
        reactionMessages = new HashMap<>();
    }

    public void registerReactionMessage(ReactionMessage message) {
        reactionMessages.put(message.getMessageID(), message);
    }

    public void changeID(long oldID, long newID) {
        if (!reactionMessages.containsKey(oldID)) return;
        ReactionMessage reaction = reactionMessages.get(oldID);
        reactionMessages.remove(oldID);
        reactionMessages.put(newID, reaction);
    }

    /*
    * Wenn eine Reaction einer Nachricht hinzugefügt wird
    * */
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUser().isBot()) return;
        SchollBot.sendConsoleMessage("Es wurde auf eine Nachricht reagiert", Debug.HIGH);
        System.out.println(Arrays.asList(reactionMessages));
        if (reactionMessages.containsKey(event.getMessageIdLong()))
            reactionMessages.get(event.getMessageIdLong()).onReact(event.getEmoji().asUnicode().getAsCodepoints(), event.getMember(), event.getGuildChannel(), true);
    }

    /*
    * Wenn eine Reaction einer Nachricht entfernt wird
    * */
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (event.getUser().isBot()) return;
        if (reactionMessages.containsKey(event.getMessageIdLong()))
            reactionMessages.get(event.getMessageIdLong()).onReact(event.getEmoji().asUnicode().getAsCodepoints(), event.getMember(), event.getGuildChannel(), false);
    }
}
