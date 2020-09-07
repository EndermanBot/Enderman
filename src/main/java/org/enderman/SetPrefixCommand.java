package org.enderman;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SetPrefixCommand extends CommandHandler.CommandExecutor {

    @Override
    protected boolean onCommand(String prefix, String[] args, String label, MessageReceivedEvent event) {
        if(args.length < 1){
            event.getChannel().sendMessage("**Erro de Sintaxe!\nUse: **" + prefix + label + " <novo-prefix>").queue();
            return false;
        }
        JsonHelper guildPreferences = Main.database.getJsonObject(event.getGuild().getId());
        guildPreferences.setString("prefix",args[0]);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Prefix definido com sucesso!");
        builder.addField("Prefix antigo", "`" + prefix +"`", true);
        builder.addField("Prefix novo", "`" + args[0] +"`", true);
        event.getChannel().sendMessage(builder.build()).queue();
        return true;
    }

    @Override
    protected String getDescription() {

        return "Defina o prefix do bot!";
    }
    
}
