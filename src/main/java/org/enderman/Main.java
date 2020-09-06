package org.enderman;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
    public static void main(String[] args) {
		JDA jda;
		String token = null;
		try {
			Scanner scanner = new Scanner(new FileReader("token"));
			token = scanner.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			jda = JDABuilder.createDefault(token).build();
			jda.awaitReady();
		} catch (LoginException e) {
			System.out.println("Impossivel logar com o token: " + token);
			System.out.println("Motivo: " + e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("O bot foi interrompido enquanto se conectava.");
		}
		
	}
}
