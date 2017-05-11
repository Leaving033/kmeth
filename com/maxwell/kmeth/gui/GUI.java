package com.maxwell.kmeth.gui;

import java.util.ArrayList;

import com.maxwell.kmeth.gui.component.Component;
import com.maxwell.kmeth.gui.component.Frame;
import com.maxwell.kmeth.modules.Module;

import net.minecraft.client.gui.GuiScreen;

public class GUI extends GuiScreen {
	public ArrayList<Frame> frames;

	public GUI() {
		this.frames = new ArrayList<Frame>();
		int frameX = 5;
		Module.Category[] values;
		for (int length = (values = Module.Category.values()).length, i = 0; i < length; ++i) {
			Module.Category category = values[i];
			Frame frame = new Frame(category);
			frame.setX(frameX);
			this.frames.add(frame);
			frameX += frame.getWidth() + 1;
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		for (Frame frame : this.frames) {
			frame.renderFrame();
			frame.updatePosition(mouseX, mouseY);
			for (Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (Frame frame : this.frames) {
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if (frame.isOpen() && !frame.getComponents().isEmpty()) {
				for (Component component : frame.getComponents()) {
					component.mouseClicked(mouseX, mouseY, mouseButton);
				}
			}
		}
	}

	protected void keyTyped(char typedChar, int keyCode) {
		for (Frame frame : this.frames) {
			if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
				for (Component component : frame.getComponents()) {
					component.keyTyped(typedChar, keyCode);
				}
			}
		}
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
		}
	}
	
	protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
		for (Frame frame : this.frames) {
			frame.setDrag(false);
		}
	}

	public boolean doesGuiPauseGame() {
		return false;
	}
}
