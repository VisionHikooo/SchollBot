package dev.visionhikooo.main;

import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class SchollBot {

    private ShardManager shardMan;

    public static void main(String[] args) {
        new SchollBot();
    }

    public SchollBot() {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault("");
        shardMan = builder.build();
    }
}