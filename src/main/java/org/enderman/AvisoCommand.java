package org.enderman;

import org.enderman.CommandHandler.CommandExecutor;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AvisoCommand extends CommandExecutor {

    @Override
    protected boolean onCommand(String prefix, String[] args, String label, MessageReceivedEvent event) {
        if (args.length < 2 || event.getMessage().getMentionedChannels().size() == 0) {
            event.getChannel().sendMessage("**Erro de sintaxe! \nUse: **" + prefix + label + " <#canal> <aviso>").queue();
            return false;
        }
        if (!event.getMessage().getMentionedChannels().get(0).getAsMention().equals(args[0])) {
            event.getChannel().sendMessage("**Erro de sintaxe! \nUse: **" + prefix + label + " <#canal> <aviso>").queue();
            return false;
        }
        StringBuilder avisoBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            avisoBuilder.append(args[i] + " ");
        }
        String aviso = avisoBuilder.toString();

        TextChannel channel = event.getMessage().getMentionedChannels().get(0);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**AVISO!** - " + event.getGuild().getName());
        builder.setDescription(aviso);
        builder.setThumbnail(event.getGuild().getIconUrl());
        builder.setColor(0xB600FF);
        MessageEmbed embed = builder.build();
        channel.sendMessage(embed).queue((message) ->{
            channel.sendMessage("@everyone").queue();
        } );
        return true;
    }

    @Override
    protected String getDescription() {

        return "Mande um aviso!";
    }
}
