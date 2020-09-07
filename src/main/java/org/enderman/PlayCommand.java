package org.enderman;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.annotation.Nullable;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;

import org.enderman.CommandHandler.CommandExecutor;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayCommand extends CommandExecutor {

    @Override
    protected boolean onCommand(String prefix, String[] args, String label, MessageReceivedEvent event) {
        
        if(!event.getMember().getVoiceState().inVoiceChannel()){
            event.getChannel().sendMessage("Você precisa entrar eu um canal de voz!").queue();
            return false;
        }
        event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        
        MessageChannel channel = event.getChannel();

        if (args.length == 0) {
            channel.sendMessage("Please provide some arguments").queue();

            return false;
        }

        String input = String.join(" ", args);

        if (!isUrl(input)) {
            String ytSearched = searchYoutube(input);

            if (ytSearched == null) {
                channel.sendMessage("Youtube returned no results").queue();

                return false;
            }

            input = ytSearched;
        }

        PlayerManager manager = PlayerManager.getInstance();

        manager.loadAndPlay(event.getChannel(), input);
        return true;
    }

    private boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Nullable
    private String searchYoutube(String input) {
        YouTube yt;
        try {
            yt = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("EndermanBot")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try {
            List<SearchResult> results = yt.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Main.config.getString("googleApiKey"))
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected String getDescription() {
        return "Toque uma música!";
    }
    
}
