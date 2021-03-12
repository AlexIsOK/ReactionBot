package dev.alexisok.reactionbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.*;

/**
 * Listens for reactions (and messages for initialization)
 */
public final class ReactionListener extends ListenerAdapter {
    
    //                   emote id, role id
    private static final Map<Long, Long> REACTION_ROLES = new HashMap<>();
    
    //add all roles to map
    static {
        //py
        REACTION_ROLES.put(819769443240116265L, 819700308527546389L);
        
        //lua
        REACTION_ROLES.put(819769315619897404L, 819692444873916426L);
        
        //c#
        REACTION_ROLES.put(819769315024306186L, 819692531893272598L);
        
        //c++
        REACTION_ROLES.put(819769314945138738L, 819692372542750772L);
        
        //java
        REACTION_ROLES.put(819769314932555796L, 819692249461293076L);
        
        //wdev
        REACTION_ROLES.put(819784087992664064L, 819704226674245693L);
    }
    
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        //only listen for the correct channel
        if(e.getChannel().getIdLong() != 819692855735615509L) return;
        
        //if the command is correct
        if(!e.getMessage().getContentRaw().equals("!start")) return;
        
        //if it's me issuing the command
        if(e.getAuthor().getIdLong() != 541763812676861952L) return;
        
        //build the message
        EmbedBuilder eb = new EmbedBuilder();
        
        //title and description of embed
        eb.setTitle("Click a reaction to be given access to that language's channel");
        eb.setDescription("" +
                "<:py:819769443240116265> - Python\n" +
                "<:lua:819769315619897404> - Lua\n" +
                "<:cs:819769315024306186> - C#\n" +
                "<:cpp:819769314945138738> - C++\n" +
                "<:java:819769314932555796> - Java\n" +
                "<:fstack:819784087992664064> - WebDev");
        
        //green on the left side of embed
        eb.setColor(Color.GREEN);
        
        //send the message and delete command usage.
        e.getChannel().sendMessage(eb.build()).queue(m -> {
            //when the message is sent, get it back here and add
            //the reactions so people don't have to react manually.
            List<Emote> emotes = Arrays.asList(
                    e.getGuild().getEmoteById(819769443240116265L),
                    e.getGuild().getEmoteById(819769315619897404L),
                    e.getGuild().getEmoteById(819769315024306186L),
                    e.getGuild().getEmoteById(819769314945138738L),
                    e.getGuild().getEmoteById(819769314932555796L),
                    e.getGuild().getEmoteById(819784087992664064L)
            );
            
            emotes.forEach(r -> m.addReaction(r).queue());
        });
    }
    
    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent e) {
        if(e.getChannel().getIdLong() == 819692855735615509L) {
            long roleID = checkReaction(e.getReactionEmote());
            
            //don't check if it errored.
            if(roleID == -1) return;
            
            e.getGuild()
                    .addRoleToMember(
                            e.getMember(),
                            Objects.requireNonNull(e.getGuild().getRoleById(roleID))
                    ).queue();
        }
    }
    
    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent e) {
        if(e.getChannel().getIdLong() == 819692855735615509L) {
            long roleID = checkReaction(e.getReactionEmote());
            
            //don't check if it errored.
            if(roleID == -1) return;
            
            e.getGuild()
                    .removeRoleFromMember(
                            Objects.requireNonNull(e.getMember()),
                            Objects.requireNonNull(e.getGuild().getRoleById(roleID))
                    ).queue();
        }
    }

    /**
     * Check the reaction to make sure that it's allowed.
     * @param emote the emote.
     * @return the ID of the role if it is ok to continue, -1 otherwise.
     */
    @Contract(pure = true)
    private static long checkReaction(MessageReaction.ReactionEmote emote) {
        //no idea when this would be emoji, might as well check here
        //as exceptions will be thrown if it is not checked.
        if(emote.isEmoji())
            return -1;
        
        long emoteID = emote.getEmote().getIdLong();
        
        return REACTION_ROLES.getOrDefault(emoteID, -1L);
    }
}
