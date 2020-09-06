package org.enderman;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandHandler {
	static enum Result {
		NO_ERROR, ERROR, NOT_FOUND;
	}
	static class CommandListener extends ListenerAdapter{
		@Override
		public void onMessageReceived(MessageReceivedEvent event) {
			String message = event.getMessage().getContentRaw();
			if(message.startsWith(Main.PREFIX)) {
				if(Main.ch.handleCommand(Main.PREFIX, event) == Result.ERROR) {
					event.getChannel().sendMessage("**Parece que houve algum erro enquanto executava o comando!**").queue();
				}
			}
		}
	}
	public static abstract class CommandExecutor implements Cloneable{
		protected abstract boolean onCommand(String prefix, String[] args, String label, MessageReceivedEvent event);
		protected boolean isAlias;
		@Override
		protected CommandExecutor clone() {
			try {
				return (CommandExecutor) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected abstract String getDescription();
	}
	HashMap<String, CommandExecutor> commands = new HashMap<>();
	public void registerCommand(String name, CommandExecutor executor) {
		commands.put(name, executor);
	}
	public void registerAlias(String name, String alias) {
		CommandExecutor clonedCmd = commands.get(name).clone();
		clonedCmd.isAlias = true;
		commands.put(alias, clonedCmd);
	}
	public Result handleCommand(String prefix, MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		String[] split = message.split(" ");
		String label = split[0].replaceFirst(prefix, "");
		List<String> args = ArrayUtils.asArrayList(split);
		args.remove(0);
		CommandExecutor executor = commands.get(label);
		if(executor == null) {
			return Result.NOT_FOUND;
		}
		String[] argsPass = new String[args.size()];
		argsPass = args.toArray(argsPass);
		return executor.onCommand(prefix, argsPass, label, event)? Result.NO_ERROR : Result.ERROR;
	}
}
