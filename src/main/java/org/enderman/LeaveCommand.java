package org.enderman;

import org.enderman.CommandHandler.CommandExecutor;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LeaveCommand extends CommandExecutor {

    @Override
    protected boolean onCommand(String prefix, String[] args, String label, MessageReceivedEvent event) {
        if(!event.getGuild().getMemberByTag(event.getJDA().getSelfUser().getAsTag()).getVoiceState().inVoiceChannel()){
            event.getChannel().sendMessage("⛔ **Eu não estou em nenhum canal de voz!**").queue();
            return true;
        }
        event.getGuild().getAudioManager().closeAudioConnection();
        event.getChannel().sendMessage("✅ **Fui desconectado do canal de voz!**").queue();
        return true;
    }

    @Override
    protected String getDescription() {
        return "Faça eu sair do canal de voz!";
    }
    
}
