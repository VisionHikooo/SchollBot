package dev.visionhikooo.listener;

import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class ButtonReactionListener extends Listener {

    public ButtonReactionListener(SchollBot schollBot) {
        super(schollBot);
    }

    /*
    * Getriggert, wenn bei einer Embedded-Message ein Button geklickt wird.
    * */

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Guild guild = event.getGuild();
        switch (event.getButton().getId().toUpperCase()) {
            case "TICKET":
                break;
            case "REPORT":
                break;
            case "CLOSE":
                break;
        }
    }
}
