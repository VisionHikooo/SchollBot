package dev.visionhikooo.features.webconnection;

import dev.visionhikooo.api.Debug;
import dev.visionhikooo.features.filesystem.OptionManager;
import dev.visionhikooo.features.filesystem.Safeable;
import dev.visionhikooo.main.SchollBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ScholltimesManager {

    private SchollBot schollBot;

    public ScholltimesManager(SchollBot schollBot) {
        this.schollBot = schollBot;
    }

    private void makeCurl() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("curl", "https://scholltimes.de/",
                "-H", "authority: scholltimes.de",
                "-H", "Accept-Language: de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7",
                "-H", "User-Agent: Mozilla/5.0 (iPad; CPU OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/87.0.4280.77 Mobile/15E148 Safari/604.1"
        );
        pb.inheritIO();
        File tmp = new File("tmp");
        tmp.createNewFile();
        pb.redirectOutput(tmp);
        Process process = pb.start();
        process.waitFor();
    }

    public void check() {
        check(false);
    }
    public void check(boolean justFirst) {
        LinkedList<String> links = new LinkedList<>();

        try {
            makeCurl();
            // Solange die Posts durchgehen, bis er mit der bekannten ID übereinstimmt
            int edge = justFirst ? 1 : 10;
            for (int i = 1; i <= edge; i++) {
                int postID = getPostID(i);

                if (schollBot.getOptionManager().getID(OptionManager.Options.LAST_SCHOLLTIMES_ID) == postID) {
                    SchollBot.sendConsoleMessage("Bekannter Artikel wurde erreicht" , Debug.LOW);
                    return;
                }

                String url = getURL(postID);

                SchollBot.sendConsoleMessage("Neuste ID: " + postID, Debug.NORMAL);
                System.out.println("Website: " + getURL(postID));

                //überprüfen, ob er neu ist
                links.add(url);

                // Dateien löschen
                new File("tmp2").delete();
                new File("tmp3").delete();

                if (i==1) {
                    schollBot.getOptionManager().setID(OptionManager.Options.LAST_SCHOLLTIMES_ID, (long) postID);
                }
            }

            new File("tmp").delete();

            for (int i = links.size()-1; i >= 0; i--) {
                sendMessage(links.get(i));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    private int getPostID(int i) throws IOException, InterruptedException {
        File tmp2 = new File("tmp2");
        tmp2.createNewFile();

        List<Process> processes = ProcessBuilder.startPipeline(List.of(
                new ProcessBuilder("cat","tmp").inheritIO().redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("grep", "article id").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("head", "-n", String.valueOf(i)).inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("tail", "-1").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("tr", "\"", "\\n").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("grep", "post-").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("head" ,"-n","1").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("tr", "-", "\\n").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("grep", "-v", "post").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(tmp2)
        ));
        processes.get(processes.size()-1).waitFor();

        // Lese String aus Datei
        return Integer.parseInt(new String(new FileInputStream(tmp2).readAllBytes()).trim());
    }

    private String getURL(int nr) throws IOException, InterruptedException {
        File tmp3 = new File("tmp3");
        tmp3.createNewFile();

        List<Process> processes = ProcessBuilder.startPipeline(List.of(
                new ProcessBuilder("cat","tmp").inheritIO().redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("grep", "article id=\\\"post-" + nr + "\\\"", "-A", "5").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("grep", "href").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("tr", "\"", "\\n").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("grep", "http").inheritIO().redirectInput(ProcessBuilder.Redirect.PIPE).redirectOutput(tmp3)
        ));
        processes.get(processes.size()-1).waitFor();

        // Lese String aus Datei
        return new String(new FileInputStream(tmp3).readAllBytes());
    }

    public void sendMessage(String url) {
        if (!schollBot.getOptionManager().hasID(OptionManager.Options.SCHOLLTIMES_ID)) {
            SchollBot.sendConsoleMessage("Der Scholltimes-Channel wurde noch nicht eingerichtet!");
            return;
        }



        TextChannel textChannel = SchollBot.getShardMan().getGuilds().get(0).getTextChannelById(schollBot.getOptionManager().getID(OptionManager.Options.SCHOLLTIMES_ID));
        textChannel.sendMessageEmbeds(new EmbedBuilder().setTitle("Neuer Scholltimes-Beitrag").setDescription("Hallo, Schollaner !\n" +
                "\n" +
                "Habt ihr schon den neusten Beitrag auf [Scholltimes](https://scholltimes.de/) \uD83D\uDCF0 gesehen ? Ihr könnt ihn über den folgenden Link erreichen:\n" +
                "\n" +
                url).build()).queue();
    }

}
