package dev.visionhikooo.commands;

import dev.visionhikooo.api.SchoolClass;
import dev.visionhikooo.commands.commandSystem.Command;
import dev.visionhikooo.main.FileManager;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.io.File;
import java.util.ArrayList;

public class SetupCommand implements Command {

    private SchollBot bot;

    public SetupCommand(SchollBot bot) {
        this.bot = bot;
    }
    @Override
    public boolean onCommand(String[] args, Member member, GuildMessageChannelUnion channel) {
        channel.deleteMessageById(channel.getLatestMessageIdLong()).queue();
        if (!(member!=null && member.isOwner()))
            return false;
        if (args.length == 0)
            return false;

        if (args[0].equalsIgnoreCase("rules")) {
            try {
                String s = "**__Regelwerk__**\n" +
                        "\n" +
                        "**§1** Jeder hat das Recht darauf, respektvoll und höflich behandelt zu werden.\n" +
                        "\n" +
                        "**§2** Auf diesem Discord sind folgende Inhalte in jeglicher Form, sowie Up- und Downloads von Dateien oder das Veröffentlichen bzw. Verbreiten von Links mit diesen Inhalten verboten. \n" +
                        "\n" +
                        "    - Beleidigungen gegenüber Mitspielern, Außenstehenden,    \n" +
                        "       dem Server oder anderen Beteiligten\n" +
                        "    - Jede Form von Rassismus, Antisemitismus und   Gewaltverherrlichung\n" +
                        "    - pornografische Inhalte jeglicher Art\n" +
                        "    - das verbreiten privater Informationen (z.B. E-mail oder Adresse) dritter ohne deren Einverständnis\n" +
                        "    - Werbung, also das kommerzielle Verbreiten von Informationen für Produkte\n" +
                        "\n" +
                        "    Mehrfaches Vergehen kann zu einem Kick oder Bann vom Server führen.\n" +
                        "\n" +
                        "**§3** Spam ist auf dem Server verboten. Als Spam zählen unter anderem\n" +
                        "\n" +
                        "    - Das Einspielen von unpassenden oder unerwünschten Tönen / Musik\n" +
                        "    - Das Wiederholen von einem oder mehreren gleichen oder ähnlichen Wortlauten innerhalb einer kurzen Zeit\n" +
                        "    - Dauerhaftes Schreiben im Caps-Lock \n" +
                        "\n" +
                        "**§4** Nicknames dürfen keine beleidigenden, verbotenen oder geschützten Namen enthalten. Zudem sollte aus dem Nickname der echte Name der Person zu erkennen sein.\n" +
                        "\n" +
                        "**§5** Das Mitschneiden von Gesprächen ist auf dem gesamten Server nur nach Absprache mit den anwesenden Benutzern des entsprechenden Channels erlaubt.\n" +
                        "\n" +
                        "**§6** Jeder ist für seinen eigenen Account verantwortlich. Daher wird eine Strafe auch dann ausgesprochen, wenn andere Personen außer dem Eigentümer einen Regelverstoß begehen.\n" +
                        " \n" +
                        "**§7** Sollte ein Regelverstoß von einem Benutzer erkannt werden, ist dieser umgehend einem Teammitglied zu melden.\n" +
                        "\n" +
                        "**§8** Server Admins, Moderatoren oder anderweitig durch den Admin Befugte haben volles Weisungsrecht. Das Verweigern einer bestimmten Anweisung wird bestraft werden (bspw. durch einen Kick bis hin zum Bann). Die Gewichtung der Bestrafung unterliegt den Teammitgliedern.\n";
                Message message = channel.sendMessage(s).complete();
                bot.getFileManager().setID(FileManager.Options.RULES, message.getIdLong());
                message.addReaction(Emoji.fromUnicode("U+2705")).queue();
            } catch (Exception e) {
                channel.asTextChannel().sendMessage(e.toString()).queue();
            }

        } else if (args[0].equalsIgnoreCase("classes")) {
            String s = "Ihr könnt eure Klassenstufe auswählen, indem ihr unten mit dem Symbol reagiert, das eurer Klassenstufe zugewiesen ist. Bitte wählt nur die Klassenstufe aus, der ihr angehört. Bei Fragen und Problemen könnt ihr euch an das Server-Team wenden.\n\n"
                    + ":five:\t\tKlassenstufe 5\n\n"
                    + ":six:\t\tKlassenstufe 6\n\n"
                    + ":seven:\t\tKlassenstufe 7\n\n"
                    + ":eight:\t\tKlassenstufe 8\n\n"
                    + ":nine:\t\tKlassenstufe 9\n\n"
                    + ":books:\t\tKlassenstufe 10\n\n"
                    + ":confounded:\t\tKlassenstufe 11\n\n"
                    + ":student:\t\tKlassenstufe 12\n\n";
            Message message = channel.sendMessage(s).complete();
            bot.getFileManager().setID(FileManager.Options.CLASSES, message.getIdLong());
            for (SchoolClass school : SchoolClass.values()) {
                message.addReaction(Emoji.fromUnicode(school.getEmoji())).queue();
            }
        } else if (args[0].equalsIgnoreCase("ticket")) {
            ArrayList<Button> buttons = new ArrayList<>();
            buttons.add(Button.of(ButtonStyle.SUCCESS, "TICKET", Emoji.fromUnicode("U+1F39F")));
            buttons.add(Button.of(ButtonStyle.DANGER, "REPORT", Emoji.fromUnicode("U+1F46E")));
            channel.sendMessageEmbeds(new EmbedBuilder().setTitle("Ticket Anfordern")
                    .setDescription("Klickt einfach auf den unteren Button, um eine Anfrage zu schicken.\n\n" +
                            "Der grüne Button erstellt eine Support-Anfrage. Hier kannst du um Hilfe bei der Schule bitten\n\n" +
                            "Der rote Button erstellt ein Report-Ticket. Hier kannst du Bugs und Vergehen melden und ein Teammitglied wird sich schnellstmöglich darum kümmern.\n\n" +
                            "Keine Scheu!")
                    .build()).addActionRow(buttons).queue();
            long id = channel.asTextChannel().getParentCategory().getIdLong();
            bot.getFileManager().setID(FileManager.Options.TICKET_CAT, id);
        } else if (args[0].equalsIgnoreCase("setBotChannel")) {
            bot.getFileManager().setID(FileManager.Options.BOT, channel.getIdLong());
        } else if (args[0].equalsIgnoreCase("setDebugChannel")) {
            bot.getFileManager().setID(FileManager.Options.DEBUG, channel.getIdLong());
        }

        return true;
    }

    @Override
    public String getDescription() {
        return "Erstellt Reaction-Messages mit Funktionalität";
    }

    @Override
    public String getUsage() {
        return "!Bot react [Type]";
    }
}
