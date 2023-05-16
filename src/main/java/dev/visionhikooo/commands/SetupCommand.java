package dev.visionhikooo.commands;

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
            String s = "**__Regelwerk__**\n" +
                    "\n" +
                    "**§1** Jeder hat das Recht darauf, respektvoll und höflich behandelt zu werden.\n" +
                    "\n" +
                    "**$2** Auf diesem Discord sind folgende Inhalte in jeglicher Form, sowie Up- und Downloads von Dateien oder das Veröffentlichen bzw. Verbreiten von Links mit diesen Inhalten verboten. \n" +
                    "\n" +
                    "    - Beleidigungen gegenüber Mitspielern, Außenstehenden,    \n" +
                    "       dem Server oder anderen Beteiligten\n" +
                    "    - Jede Form von Rassismus, Antisemitismus und   Gewaltverherrlichung\n" +
                    "    - pornografische Inhalte jedweder Art\n" +
                    "    - das verbreiten privater Informationen (z.B. E-mail oder Adresse) dritter ohne deren Einverständnis\n" +
                    "    - Werbung\n" +
                    "\n" +
                    "**§3** Mehrfaches Vergehen kann zu einem Kick oder Bann vom Server führen.\n" +
                    "\n" +
                    "**§4** Spam ist auf dem Server verboten. Als Spam zählen unter anderem\n" +
                    "\n" +
                    "    - Einspielen von unpassenden oder unerwünschten Tönen / Musik\n" +
                    "    - Das Wiederholen von einem oder mehreren gleichen oder ähnlichen Wortlauten innerhalb einer kurzen Zeit\n" +
                    "    - Dauerhaftes Schreiben im Caps-Lock \n" +
                    "\n" +
                    "**§5** Nicknames dürfen keine beleidigenden, verbotenen oder geschützten Namen enthalten. Zudem sollte aus dem Nickname der echte Name der Person zu erkennen sein.\n" +
                    "\n" +
                    "**§6** Das Mitschneiden von Gesprächen ist auf dem gesamten Server nur nach Absprache mit den anwesenden Benutzern des entsprechenden Channels erlaubt.\n" +
                    "\n" +
                    "**§7** Jeder ist für seinen eigenen Account verantwortlich. Daher wird eine Strafe auch dann ausgesprochen, wenn eine zweite Person außer dem Eigentümer einen Regelverstoß begeht.\n" +
                    " \n" +
                    "**§8** Sollte ein Regelverstoß von einem Benutzer erkannt werden, ist dieser umgehend einem Admin zu melden.\n" +
                    "\n" +
                    "**§9** Server Admins, Moderatoren oder anderweitig befugte Admins haben volles Weisungsrecht. Das Verweigern einer bestimmten Anweisung kann zu einem Kick oder Bann führen.Die Gewichtung der Bestrafung unterliegt den Teammitgliedern.";
            Message message = channel.sendMessage(s).complete();
            bot.getFileManager().setID(FileManager.Options.RULES, message.getIdLong());
            message.addReaction(Emoji.fromUnicode("U+2705")).queue();

        } else if (args[0].equalsIgnoreCase("classes")) {
            String s = "Willkommen auf dem Schollaner-Discord\n" +
                    "\n" +
                    "Ziel dieses Discords ist es, eine Plattform zum Austausch für Schülerinnen und Schüler zu bieten. \n";
            Message message = channel.sendMessage(s).complete();
            bot.getFileManager().setID(FileManager.Options.CLASSES, message.getIdLong());
            message.addReaction(Emoji.fromUnicode("U+0031 U+20E3")).queue();
            message.addReaction(Emoji.fromUnicode("U+0032 U+20E3")).queue();
            message.addReaction(Emoji.fromUnicode("U+0033 U+20E3")).queue();
            message.addReaction(Emoji.fromUnicode("U+0034 U+20E3")).queue();
            message.addReaction(Emoji.fromUnicode("U+0035 U+20E3")).queue();
            message.addReaction(Emoji.fromUnicode("U+0036 U+20E3")).queue();
            message.addReaction(Emoji.fromUnicode("U+0037 U+20E3")).queue();
            message.addReaction(Emoji.fromUnicode("U+0038 U+20E3")).queue();
        } else if (args[0].equalsIgnoreCase("ticket")) {
            ArrayList<Button> buttons = new ArrayList<>();
            buttons.add(Button.of(ButtonStyle.SUCCESS, "TICKET", Emoji.fromUnicode("U+1F39F")));
            buttons.add(Button.of(ButtonStyle.DANGER, "REPORT", Emoji.fromUnicode("U+1F46E")));
            channel.sendMessageEmbeds(new EmbedBuilder().setTitle("Ticket Anfordern")
                    .setDescription("Klickt einfach auf den unteren Button, um eine Anfrage zu schicken. Keine Scheu!")
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
