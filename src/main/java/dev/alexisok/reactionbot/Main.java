package dev.alexisok.reactionbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

/**
 * Main class.
 */
public final class Main {

    /**
     * Main method.
     * @param args args, first arg is token.
     * @throws LoginException if the bot cannot login.
     * @throws InterruptedException if there was a problem awaiting the bots readiness.
     */
    public static void main(String[] args) throws LoginException, InterruptedException {
        
        //build the jda listener
        JDA j = JDABuilder.create(GUILD_MESSAGES, GUILD_MESSAGE_REACTIONS, GUILD_EMOJIS, GUILD_MEMBERS)
                .setToken(args[0])
                .addEventListeners(new ReactionListener())
                .enableCache(CacheFlag.EMOTE)
                .build();
        
        //await ready and that's all for main.
        j.awaitReady();
        
    }
    
}
