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
import dev.visionhikooo.filesystem.FileManager;
import dev.visionhikooo.filesystem.OptionManager;
import dev.visionhikooo.listener.ButtonReactionListener;
import dev.visionhikooo.listener.CommandListener;
import dev.visionhikooo.listener.GuildReactionManager;
import dev.visionhikooo.listener.TempChannelManager;
import dev.visionhikooo.surveysAndStatistics.StatistikManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class SchollBot {

    private static ShardManager shardMan;
    private CommandManager commandManager;
    private TempChannelManager tempChannelManager;
    private GuildReactionManager guildReactionManager;
    private static FileManager fileManager;
    private static OptionManager optionManager;

    private StatistikManager statistikManager;


    private static boolean sendDebugToChannel = false;


    public GuildReactionManager getGuildReactionManager() {
        return guildReactionManager;
    }

    public static void main(String[] args) {
        new SchollBot();
    }

    private long adminID = 1105213719555878993L;
    private long modID = 1105255834625249420L;


    public SchollBot() {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(Tokens.TEST_TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Noah zu."));
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        fileManager = new FileManager(this);
        optionManager = new OptionManager(this);
        commandManager = new CommandManager();
        tempChannelManager = new TempChannelManager(this);
        guildReactionManager = new GuildReactionManager(this);

        builder.addEventListeners(new CommandListener(this), tempChannelManager, guildReactionManager, new ButtonReactionListener(this));
        registerCommands();
        registerMessages();
        shardMan = builder.build();
        statistikManager = new StatistikManager(this);
        shutdown();
    }

    public OptionManager getOptionManager() {
        return optionManager;
    }

    public long getAdminID() {
        return adminID;
    }

    public static ShardManager getShardMan() {
        return shardMan;
    }

    public long getModID() {
        return modID;
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

    public StatistikManager getStatistikManager() {
        return statistikManager;
    }

    public void registerCommands() {
        commandManager.registerCommand("hi", new HiCMD());
        commandManager.registerCommand("tmp", new TempChannelCMD(this));
        commandManager.registerCommand("setup", new SetupCommand(this));
        commandManager.registerCommand("debug", new DebugCMD());
    }

    public void registerMessages() {
        //Regelnachricht
        guildReactionManager.registerReactionMessage(new ReactionMessage(optionManager.getID(OptionManager.Options.RULES_ID), new Reactable() {
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


        guildReactionManager.registerReactionMessage(new ReactionMessage(optionManager.getID(OptionManager.Options.CLASSES_ID), new Reactable() {
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

    public List<Role> getClassOfMember(Member member) {
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
                        optionManager.safeIDs();
                        reader.close();
                    } else if(line.equalsIgnoreCase("reload")) {
                        reload();
                    } else if(line.startsWith("debug")) {
                        if (line.equalsIgnoreCase("debug"))
                            System.out.println("Debug-Mode: " + OptionManager.getDebug());
                        else {
                            String s = line.substring(6);
                            try {
                                Debug d = OptionManager.getDebug();
                                OptionManager.setDebug( Debug.valueOf(s.toUpperCase()));
                                sendConsoleMessage("Der Debug-Modus wurde von " + d + " auf " + OptionManager.getDebug() + " umgestellt!", Debug.LOW);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Unbekannter Debug-Wert");
                            }
                        }
                    } else if (line.equalsIgnoreCase("stat")) {
                        statistikManager.safe();
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
            if (sendDebugToChannel && optionManager.hasID(OptionManager.Options.DEBUG_ID)) {
                shardMan.getGuilds().get(0).getTextChannelById(optionManager.getID(OptionManager.Options.DEBUG_ID)).sendMessage(s).queue();
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
        optionManager.safeIDs();


        // load everything
        optionManager.loadIDs();
    }
}