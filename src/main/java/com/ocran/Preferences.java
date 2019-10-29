package com.ocran;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;

public class Preferences {
	public static final String CONFIG_FILE = "config.txt";
	
	private int nDaysWithoutFine;
	private float finePerDay;
	private String username;
	private String password;
	
	public Preferences() {
		nDaysWithoutFine = 14;
		finePerDay = 2;
		username = "admin";
		setPassword("admin");
	}
	
	// Setters
	public void setnDaysWithoutFine(int nDaysWithoutFine) {
		this.nDaysWithoutFine = nDaysWithoutFine;
	}

	public void setFinePerDay(float finePerDay) {
		this.finePerDay = finePerDay;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		if(password.length() < 16) {
			this.password = DigestUtils.sha1Hex(password);
		}else {
			this.password = password;
		}
	}

	// Getters
	public int getnDaysWithoutFine() {
		return nDaysWithoutFine;
	}

	public float getFinePerDay() {
		return finePerDay;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public static void initConfig() {
		Writer writer = null;
		try {
			Preferences  preference = new Preferences();
			Gson gson = new Gson();
			writer =  new FileWriter(CONFIG_FILE);
			gson.toJson(preference, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				writer.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Preferences getPreferences() {
		Gson gson = new Gson();
		Preferences preference = new Preferences();
		try {
			preference = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
		} catch (Exception e) {
			initConfig();
			e.printStackTrace();
		}
		return preference; 
	}
	
	public static void writePreferencesToFile(Preferences preference) {
		Writer writer = null;
		try {
			Gson gson = new Gson();
			writer =  new FileWriter(CONFIG_FILE);
			gson.toJson(preference, writer);
			
			Message.infoMessage("Success", "Settings updated");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				writer.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
