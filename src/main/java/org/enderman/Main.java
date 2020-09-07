package org.enderman;

import java.io.IOException;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;

public class Main {
	public static String DEFAULT_PREFIX = "en!";
	public static CommandHandler ch = new CommandHandler();
	static JDA jda;
	public static JsonHelper database;
	public static JsonHelper config;

	public static void main(String[] args) {
		try {
			config = new JsonHelper("config.json");
			database = new JsonHelper("database.json");
		} catch (JsonIOException | JsonSyntaxException | IOException e1) {

			e1.printStackTrace();
		}
		String token = config.getString("token");

		try {
			jda = JDABuilder.createDefault(token).build();
			jda.awaitReady();
		} catch (LoginException e) {
			System.out.println("Impossivel logar com o token: " + token);
			System.out.println("Motivo: " + e.getMessage());
			return;
		} catch (InterruptedException e) {
			System.out.println("O bot foi interrompido enquanto se conectava.");
			return;
		}
		jda.addEventListener(new CommandHandler.CommandListener());
		ch.registerCommand("ban", new BanCommand());
		ch.registerCommand("kick", new KickCommand());
		ch.registerCommand("aviso", new AvisoCommand());
		ch.registerCommand("setprefix", new SetPrefixCommand());
		ch.registerCommand("play", new PlayCommand());
		ch.registerCommand("leave", new LeaveCommand());
		ch.registerAlias("kick", "expulsar");
		ch.registerAlias("ban", "banir");

		presences.add(new PresenceBuilder().setName("com o meu criador!"));
		presences.add(new PresenceBuilder().setName("%servers% servidores de discord!").setType(ActivityType.WATCHING));
		presences.add(new PresenceBuilder().setName("%users% pessoas!"  ).setType(ActivityType.WATCHING));

		startLoop();
	}

	private static class PresenceBuilder {
		String name;
		ActivityType type;
		public PresenceBuilder(){
			type = ActivityType.DEFAULT;
			name = "";
		}
		public PresenceBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public PresenceBuilder setType(ActivityType type) {
			this.type = type;
			return this;
		}

		public String getName() {
			return name;
		}

		public ActivityType getType() {
			return type;
		}

		public Activity build() {
			return Activity.of(type, name);
		}
	}

	public static ArrayList<PresenceBuilder> presences = new ArrayList<>();

	private static void startLoop() {
		Thread loop = new Thread(new Runnable() {
			@Override
			public void run() {
				while (jda.getStatus() == JDA.Status.CONNECTED) {
					for (PresenceBuilder presence : presences) {

						presence.name = presence.name.replaceAll("%users%", String.valueOf(jda.getUsers().size())).replaceAll("%servers%", String.valueOf(jda.getGuilds().size()));
						jda.getPresence().setActivity(presence.build());
						try {
							Thread.sleep(15000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		loop.start();
	}
}
