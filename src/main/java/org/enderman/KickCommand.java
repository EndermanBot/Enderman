package org.enderman;

import org.enderman.CommandHandler.CommandExecutor;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class KickCommand extends CommandExecutor {

    @Override
    protected boolean onCommand(String prefix, String[] args, String label, MessageReceivedEvent event) {
        if (args.length < 1 || event.getMessage().getMentionedMembers().size() == 0) {
			event.getChannel().sendMessage("**Erro de sintaxe! \nUse: **" + prefix + label + " <@membro> (motivo)").queue();
			return false;
		}
		if(!event.getMessage().getMentionedMembers().get(0).getAsMention().equals(args[0].replaceAll("!", ""))) {
			event.getChannel().sendMessage("**Erro de sintaxe! \nUse: **" + prefix + label + " <@membro> (motivo)").queue();
			System.out.println(event.getMessage().getMentionedMembers().get(0).getAsMention());
			return false;
        }

        Member member = event.getMessage().getMentionedMembers().get(0);
        if(args.length >= 2){
            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reasonBuilder.append(args[i] + " ");
            }
            String reason = reasonBuilder.toString();
            member.kick(reason).queue((v) -> {
                event.getChannel()
                        .sendMessage(new EmbedBuilder().setTitle("Usuário Kickado com sucesso!")
                                .addField("Usuario", member.getAsMention(), true)
                                .addField("Autor:", event.getAuthor().getAsMention(), true)
                                .addField("Motivo", reason, true)
                                .setColor(0xB600FF)
                                .build()).queue();
            });
            return true;
        }
        member.kick().queue((v) -> {
            event.getChannel()
                    .sendMessage(new EmbedBuilder().setTitle("Usuário Kickado com sucesso!")
                            .addField("Usuario", member.getAsMention(), true)
                            .addField("Autor:", event.getAuthor().getAsMention(), true)
                            .addField("Motivo", "Não especificado.", true)
                            .setColor(0xB600FF)
                            .build()).queue();
        });
		

		return true;
    }

    @Override
    protected String getDescription() {
        return "Expulse uma pessoa!";
    }
    
}
