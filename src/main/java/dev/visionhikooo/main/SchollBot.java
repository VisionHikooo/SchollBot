package dev.visionhikooo.main;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.api.Reactable;
import dev.visionhikooo.api.ReactionMessage;
import dev.visionhikooo.api.SchoolClass;
import dev.visionhikooo.commands.DebugCMD;
import dev.visionhikooo.commands.HiCMD;
import dev.visionhikooo.commands.SetupCommand;
import dev.visionhikooo.commands.TempChannelCMD;
import dev.visionhikooo.commands.commandSystem.CommandManager;
import dev.visionhikooo.listener.ButtonReactionListener;
import dev.visionhikooo.listener.CommandListener;
import dev.visionhikooo.listener.GuildReactionManager;
import dev.visionhikooo.listener.TempChannelManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SchollBot {

    private static ShardManager shardMan;
    private CommandManager commandManager;
    private TempChannelManager tempChannelManager;
    private GuildReactionManager guildReactionManager;
    private static FileManager fileManager;

    private static Debug debug = Debug.HIGH;
    private static boolean sendDebugToChannel = false;


    public GuildReactionManager getGuildReactionManager() {
        return guildReactionManager;
    }

    public static Debug getDebug() {
        return debug;
    }

    public static void setDebug(Debug debug) {
        SchollBot.debug = debug;
    }

    public static void main(String[] args) {
        new SchollBot();
    }

    public SchollBot() {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(Tokens.HAUPT_TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Noah zu."));
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        fileManager = new FileManager(this);
        commandManager = new CommandManager();
        tempChannelManager = new TempChannelManager(this);
        guildReactionManager = new GuildReactionManager(this);

        builder.addEventListeners(new CommandListener(this), tempChannelManager, guildReactionManager, new ButtonReactionListener(this));
        registerCommands();
        registerMessages();
        shardMan = builder.build();
        shutdown();
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public TempChannelManager getTempChannelManager() {
        return tempChannelManager;
    }

    public void registerCommands() {
        commandManager.registerCommand("hi", new HiCMD());
        commandManager.registerCommand("tmp", new TempChannelCMD(this));
        commandManager.registerCommand("setup", new SetupCommand(this));
        commandManager.registerCommand("debug", new DebugCMD());
    }

    public void registerMessages() {
        //Regelnachricht
        guildReactionManager.registerReactionMessage(new ReactionMessage(fileManager.getID(FileManager.Options.RULES), new Reactable() {
            @Override
            public void onReact(String codePoint, Member member, GuildMessageChannelUnion channel) {

                if (codePoint.equals("U+2705")) {
                    try {
                        sendConsoleMessage("Es wurde mit dem Haken reagiert!", Debug.HIGH);
                        Role role = member.getGuild().getRolesByName("Mitglied", true).get(0);
                        if (!member.getRoles().contains(role))
                            member.getGuild().addRoleToMember(member, role).queue();
                        else
                            member.getGuild().removeRoleFromMember(member, role).queue();
                    } catch (HierarchyException e) {
                        sendConsoleMessage("HierarchieException");
                    }
                }
            }
        }));


        guildReactionManager.registerReactionMessage(new ReactionMessage(fileManager.getID(FileManager.Options.CLASSES), new Reactable() {
            @Override
            public void onReact(String codePoint, Member member, GuildMessageChannelUnion channel) {
                for (Role role : getClassOfMember(member)) {
                    member.getGuild().removeRoleFromMember(member, role).queue();
                }
                Role role = null;
                SchoolClass schoolClass = SchoolClass.CLASS_5.getSchoolClass(codePoint);
                if (schoolClass == null) {
                    sendConsoleMessage("Schoolclass IS NULL!");
                    return;
                }
                List<Role> roles = member.getGuild().getRolesByName(schoolClass.getRoleName(), true);
                if (roles.isEmpty())
                    role = null;
                role = roles.get(0);

                if (role != null)
                    member.getGuild().addRoleToMember(member, role).queue();
            }
        }));
    }

    private List<Role> getClassOfMember(Member member) {
        List<Role> roles = member.getRoles().stream().filter(role -> role.getName().endsWith("Klasse")).collect(Collectors.toList());
        sendConsoleMessage("Anzahl an Passenden Roles: " + roles.size());
        return roles;
    }

    public void shutdown() {
        new Thread(() -> {
            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        if (shardMan != null) {
                            shardMan.setStatus(OnlineStatus.OFFLINE);
                            shardMan.shutdown();
                            System.out.println("Bot offline.");
                        }
                        tempChannelManager.onShutdown();
                        fileManager.safeIDs();
                        reader.close();
                    } else if(line.equalsIgnoreCase("reload")) {
                        reload();
                    } else if(line.startsWith("debug")) {
                        if (line.equalsIgnoreCase("debug"))
                            System.out.println("Debug-Mode: " + debug);
                        else {
                            String s = line.substring(6);
                            try {
                                Debug d = debug;
                                debug = Debug.valueOf(s.toUpperCase());
                                sendConsoleMessage("Der Debug-Modus wurde von " + d + " auf " + debug + " umgestellt!", Debug.LOW);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Unbekannter Debug-Wert");
                            }
                        }
                    } else {
                        System.out.println("Use 'exit' to shutdown or 'reload' to reload the bot.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void sendConsoleMessage(String message, Debug debug) {
        if (debug.isAllowed()) {
            String s = "[" + (debug != Debug.NONE ? debug : "Error") + "] " + message;
            System.out.println(s);
            System.out.println(fileManager.hasID(FileManager.Options.DEBUG));
            System.out.println(sendDebugToChannel);
            if (sendDebugToChannel && fileManager.hasID(FileManager.Options.DEBUG)) {
                shardMan.getGuilds().get(0).getTextChannelById(fileManager.getID(FileManager.Options.DEBUG)).sendMessage(s).queue();
                System.out.println("Kleiner Test ");
            }
        }
    }

    public static boolean isSendDebugToChannel() {
        return sendDebugToChannel;
    }

    public static void setSendDebugToChannel(boolean sendDebugToChannel) {
        SchollBot.sendDebugToChannel = sendDebugToChannel;
    }

    public static void sendConsoleMessage(String message) {
        sendConsoleMessage(message, Debug.NONE);
    }

    private void reload() {
        // Safe everything
        fileManager.safeIDs();


        // load everything
        fileManager.loadIDs();
    }
}