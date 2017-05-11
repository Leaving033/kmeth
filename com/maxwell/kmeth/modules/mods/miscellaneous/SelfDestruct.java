package com.maxwell.kmeth.modules.mods.miscellaneous;

import org.lwjgl.input.Keyboard;

import com.maxwell.kmeth.modules.Module;
import com.maxwell.kmeth.utilites.Wrapper;

public class SelfDestruct extends Module {
	public SelfDestruct() {
		super("Self Destruct", Keyboard.KEY_DELETE, Category.MISCELLANEOUS);
	}
	
	// Removed. Not going to spoon feed you code that makes a ghost client a ghost client.
	@Override
	public void onEnable() {
		if (Wrapper.getPlayer() != null) {

		}
	}
}
