package com.maxwell.kmeth.modules.mods.visual;

import org.lwjgl.input.Keyboard;

import com.maxwell.kmeth.kmethMain;
import com.maxwell.kmeth.modules.Module;
import com.maxwell.kmeth.utilites.Wrapper;

public class ClickGUI extends Module {
	public ClickGUI() {
		super("Click GUI", Keyboard.KEY_RSHIFT, Category.VISUAL);
	}

	@Override
	public void onEnable() {
		if (Wrapper.getPlayer() != null) {
			if (Wrapper.getMinecraft().currentScreen == null) {
				Wrapper.getMinecraft().displayGuiScreen(kmethMain.getClickGUI());
				this.setState(false);
			}
		}
	}
}