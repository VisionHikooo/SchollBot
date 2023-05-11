package dev.visionhikooo.listener;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class GuildReactionManager extends ListenerAdapter {

    private ArrayList<Long> messageIDs;

    public GuildReactionManager() {
        messageIDs = new ArrayList<>();
    }

    /*
    * Wenn eine Reaction einer Nachricht hinzugefügt wird
    * */


    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        System.out.println("Entspricht dem gesuchten: ");
    }

    /*
    * Wenn eine Reaction einer Nachricht entfernt wird
    * */
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        super.onMessageReactionRemove(event);
    }
}
