package dev.visionhikooo.listener;

import dev.visionhikooo.main.FileManager;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class ButtonReactionListener extends Listener {

    public ButtonReactionListener(SchollBot schollBot) {
        super(schollBot);
    }

    /*
    * Getriggert, wenn bei einer Embedded-Message ein Button geklickt wird.
    * */

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        long channelID =getSchollBot().getFileManager().getID(FileManager.Options.TICKET_CAT);
        Category cat = event.getGuild().getCategoryById(channelID);
        if (channelID == 1)
            channelID = event.getChannel().asTextChannel().getParentCategoryIdLong();
        switch (event.getButton().getId().toUpperCase()) {
            case "TICKET":
                event.getGuild().createTextChannel("Support-" + (cat.getChannels().size()), cat).complete().sendMessageEmbeds(new EmbedBuilder().setTitle("Support").setDescription("Wir werden uns schnellstmöglich um dein Problem kümmern.").build()).addActionRow(Button.of(ButtonStyle.SUCCESS, "CLOSE", Emoji.fromUnicode("U+2705"))).queue();
                break;
            case "REPORT":
                event.getGuild().createTextChannel("Report-" + (cat.getChannels().size()), cat).complete().sendMessageEmbeds(new EmbedBuilder().setTitle("Report").setDescription("Bitte schreibe uns, welchen Verstoß bzw. welchen Bug du beobachtet hast.").build()).addActionRow(Button.of(ButtonStyle.SUCCESS, "CLOSE", Emoji.fromUnicode("U+2705"))).queue();
                break;
            case "CLOSE":
                event.getChannel().delete().queue();
                break;
        }
    }
}
