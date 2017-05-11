package com.maxwell.kmeth.modules;

import java.util.ArrayList;

import com.maxwell.kmeth.kmethMain;
import com.maxwell.kmeth.values.BooleanValue;
import com.maxwell.kmeth.values.NumberValue;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public abstract class Module {
	private boolean state;
	private String name;
	private int key;
	private Category category;
	private ArrayList<BooleanValue> options;
	private ArrayList<NumberValue> values;

	public Module(String name, int key, Category category) {
		this.state = false;
		this.name = name;
		this.key = key;
		this.category = category;
		this.options = new ArrayList<BooleanValue>();
		this.values = new ArrayList<NumberValue>();
	}

	public String getName() {
		return this.name;
	}

	public int getKey() {
		return this.key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Category getCategory() {
		return this.category;
	}

	public ArrayList<BooleanValue> getOptions() {
		return this.options;
	}

	public void addOption(BooleanValue option) {
		this.options.add(option);
	}

	public ArrayList<NumberValue> getValues() {
		return this.values;
	}

	public void addValue(NumberValue value) {
		this.values.add(value);
	}

	public boolean getState() {
		return this.state;
	}

	public void setState(boolean enabled) {
		if (this.state == enabled) {
			return;
		}
		this.state = enabled;
		if (enabled) {
			MinecraftForge.EVENT_BUS.register(this);
			FMLCommonHandler.instance().bus().register(this);
			this.onEnable();
		} else {
			MinecraftForge.EVENT_BUS.unregister(this);
			FMLCommonHandler.instance().bus().unregister(this);
			this.onDisable();
		}
	}
	
	public static ArrayList<Module> getCategoryModules(Module.Category cat) {
		ArrayList<Module> modsInCategory = new ArrayList<Module>();
		for (Module mod : kmethMain.getModules()) {
			if (mod.getCategory() == cat) {
				modsInCategory.add(mod);
			}
		}
		return modsInCategory;
	}
	
	public static Module getModule(Class<? extends Module> clazz) {
		for (Module mod : kmethMain.getModules()) {
			if (mod.getClass() == clazz) {	
				return mod;
			}
		}
		return null;
	}
	
	public void onEnable() {

	}

	public void onDisable() {

	}

	public enum Category {
		COMBAT("Combat", 0), VISUAL("Visual", 1), MISCELLANEOUS("Miscellaneous", 2);

		private String name;
		private int id;

		private Category(String name, int id) {
			this.name = name;
			this.id = id;
		}

		public String getName() {
			return this.name;
		}

		public int getID() {
			return this.id;
		}
	}
}