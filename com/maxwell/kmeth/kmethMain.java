package com.maxwell.kmeth;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.maxwell.kmeth.gui.GUI;
import com.maxwell.kmeth.modules.Module;
import com.maxwell.kmeth.modules.mods.combat.AimAssist;
import com.maxwell.kmeth.modules.mods.combat.Autoclicker;
import com.maxwell.kmeth.modules.mods.combat.Reach;
import com.maxwell.kmeth.modules.mods.miscellaneous.SelfDestruct;
import com.maxwell.kmeth.modules.mods.visual.ClickGUI;
import com.maxwell.kmeth.utilites.Wrapper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "TcpNoDelayMod")
public class kmethMain {
	// This whole class is a mess, I really should have cleaned this up but since the client was obfuscated and 
	// private I didn't feel the need to as the code would not be seen.
	
	private static ArrayList<Module> modules;
	private static GUI clickGUI;

	@EventHandler
	public void fmlInitialization(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		clickGUI = new GUI();
	}
	
	@SubscribeEvent
	public void keyInput(KeyInputEvent event) {
		if (Wrapper.getPlayer() != null) {
			if (!Keyboard.getEventKeyState()) {
				return;
			}
			for (Module mod : this.getModules()) {
				if (mod.getKey() == Keyboard.getEventKey()) {
					mod.setState(!mod.getState());
				}
			}
		}
	}

	public static ArrayList<Module> getModules() {
		return modules;
	}

	public static GUI getClickGUI() {
		return clickGUI;
	}

	// I have no idea what I was thinking when I did this. If you want to clean it up, make a module manager.
	static {
		(modules = new ArrayList<Module>()).add(new Reach());
		modules.add(new AimAssist());
		modules.add(new Autoclicker());
		modules.add(new ClickGUI());
		modules.add(new SelfDestruct());
	}
}
