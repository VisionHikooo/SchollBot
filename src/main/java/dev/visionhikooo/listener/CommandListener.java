package dev.visionhikooo.listener;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.commands.commandSystem.CommandManager;
import dev.visionhikooo.main.SchollBot;
import dev.visionhikooo.surveysAndStatistics.StatistikManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.Locale;

public class CommandListener extends Listener {

    private CommandManager manager;

    public CommandListener(SchollBot schollBot) {
        super(schollBot);
        this.manager = schollBot.getCommandManager();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String message = event.getMessage().getContentDisplay();
        SchollBot.sendConsoleMessage("Eine neue Nachricht wurde erkannt!", Debug.HIGH);
        if (message.equalsIgnoreCase("!Bot") || message.equalsIgnoreCase("!help"))
            manager.sendHelp(event.getGuildChannel());

        boolean bot;
        if (!(bot = message.startsWith("!bot ")) && !message.startsWith("!help ")) {
            // Also es ist eine normale Nachricht!
            getSchollBot().getStatistikManager().addStatisticValue(StatistikManager.StatisticCategory.MESSAGES_PER_DAY);
            return;
        }

        getSchollBot().getStatistikManager().addStatisticValue(StatistikManager.StatisticCategory.COMMANDS_PER_DAY);
        SchollBot.sendConsoleMessage("Ein neuer Befehl wurde erkannt!", Debug.NORMAL);
        String[] commandParts = message.substring(bot ? 5 : 6).split(" ");
        String label = commandParts[0].toLowerCase();
        if (bot) {
            String[] args;
            if (commandParts.length > 1)
                args = Arrays.copyOfRange(commandParts, 1, commandParts.length);
            else
                args = new String[0];

            manager.executeCommand(label, args, event.getMember(), event.getGuildChannel());
        } else
            manager.sendHelp(label, event.getGuildChannel(), event.getMember());
    }
}
