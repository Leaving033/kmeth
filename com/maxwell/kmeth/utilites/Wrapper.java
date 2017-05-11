package com.maxwell.kmeth.utilites;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;

public class Wrapper {
	public static Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	public static EntityPlayerSP getPlayer() {
		return getMinecraft().thePlayer;
	}

	public static WorldClient getWorld() {
		return getMinecraft().theWorld;
	}

	public static PlayerControllerMP getPlayerController() {
		return getMinecraft().playerController;
	}

	public static GameSettings getGameSettings() {
		return getMinecraft().gameSettings;
	}
}
