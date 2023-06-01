package dev.visionhikooo.listener;

import dev.visionhikooo.features.filesystem.OptionManager;
import dev.visionhikooo.main.SchollBot;
import dev.visionhikooo.features.surveysAndStatistics.StatistikManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.EnumSet;
import java.util.List;

public class ButtonReactionListener extends Listener {

    public ButtonReactionListener(SchollBot schollBot) {
        super(schollBot);
    }

    /*
    * Getriggert, wenn bei einer Embedded-Message ein Button geklickt wird.
    * */

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        event.deferEdit().queue();
        long channelID =getSchollBot().getOptionManager().getID(OptionManager.Options.TICKET_CAT_ID);
        Category cat = event.getGuild().getCategoryById(channelID);
        if (channelID == 1)
            channelID = event.getChannel().asTextChannel().getParentCategoryIdLong();

        switch (event.getButton().getId().toUpperCase()) {
            case "TICKET":
                getSchollBot().getStatistikManager().addStatisticValue(StatistikManager.StatisticCategory.TICKETS_PER_DAY);
                event.getGuild().createTextChannel("Support-" + (cat.getChannels().size()), cat).complete()
                .sendMessageEmbeds(new EmbedBuilder().setTitle("Support").setDescription("Wir werden uns schnellstmöglich um dein Problem kümmern. Sobald deine Frage geklärt wurde, klicke bitte auf den Button, um das Ticket zu schließen.")
                .build()).addActionRow(Button.of(ButtonStyle.SUCCESS, "CLOSE-" + event.getMember().getId(), Emoji.fromUnicode("U+2705"))).queue();
                break;
            case "REPORT":
                getSchollBot().getStatistikManager().addStatisticValue(StatistikManager.StatisticCategory.REPORTS_PER_DAY);
                event.getGuild().createTextChannel("Report-" + (cat.getChannels().size()), cat)
                        .addPermissionOverride(event.getGuild().getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                        .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .addPermissionOverride(event.getGuild().getRoleById(getSchollBot().getAdminID()), EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .addPermissionOverride(event.getGuild().getRoleById(getSchollBot().getModID()), EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .complete().sendMessageEmbeds(new EmbedBuilder().setTitle("Report")
                        .setDescription("Bitte schreibe uns, welchen Verstoß bzw. welchen Bug du beobachtet hast. Sobald du fertig bist, klicke auf diesen Haken, um das Ticket zu schließen.").build())
                        .addActionRow(Button.of(ButtonStyle.SUCCESS, "CLOSE", Emoji.fromUnicode("U+2705"))).queue();
                break;
            case "CLOSE":
                event.getChannel().delete().queue();
                break;
            default:
                if (event.getButton().getId().startsWith("CLOSE-")) {
                    String id = event.getButton().getId().substring(6);
                    List<Role> roles = event.getMember().getRoles();
                    if (event.getMember().getId().equalsIgnoreCase(id) || roles.contains(event.getGuild().getRoleById(getSchollBot().getAdminID())) || roles.contains(event.getGuild().getRoleById(getSchollBot().getModID())))
                        event.getChannel().delete().queue();
                }
                break;
        }


    }
}
