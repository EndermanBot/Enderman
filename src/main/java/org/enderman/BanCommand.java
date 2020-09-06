package org.enderman;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BanCommand extends CommandHandler.CommandExecutor {

	@Override
	protected boolean onCommand(String prefix, String[] args, String label, MessageReceivedEvent event) {
		if (args.length < 2 || event.getMessage().getMentionedMembers().size() == 0) {
			event.getChannel().sendMessage("Erro de sintaxe! \nUse: " + prefix + "ban <@membro> <motivo>").queue();
			return false;
		}
		if(!event.getMessage().getMentionedMembers().get(0).getAsMention().equals(args[0].replaceAll("!", ""))) {
			event.getChannel().sendMessage("Erro de sintaxe! \nUse: " + prefix + "ban <@membro> <motivo>").queue();
			System.out.println(event.getMessage().getMentionedMembers().get(0).getAsMention());
			return false;
		}
		StringBuilder reasonBuilder = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			reasonBuilder.append(args[i] + " ");
		}
		String reason = reasonBuilder.toString();
		Member member = event.getMessage().getMentionedMembers().get(0);
		member.ban(0, reason).queue((v) -> {
			event.getChannel()
					.sendMessage(new EmbedBuilder().setTitle("Usuário banido com sucesso!")
							.addField("Usuario", member.getAsMention(), true)
							.addField("Autor:", event.getAuthor().getAsMention(), true)
							.addField("Motivo", reason, true)
							.build()).queue();
		});

		return true;
	}

	@Override
	protected String getDescription() {
		return "Tem alguem quebrando as regras? Dê um ban!";
	}

}
