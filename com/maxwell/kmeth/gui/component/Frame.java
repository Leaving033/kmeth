package com.maxwell.kmeth.gui.component;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.maxwell.kmeth.gui.component.components.Button;
import com.maxwell.kmeth.modules.Module;
import com.maxwell.kmeth.utilites.Wrapper;

import net.minecraft.client.gui.Gui;

public class Frame {
	private ArrayList<Component> components;
	private Module.Category category;
	private boolean open;
	private int width;
	private int y;
	private int x;
	private int barHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;

	public Frame(Module.Category category) {
		this.components = new ArrayList<Component>();
		this.category = category;
		this.width = 88;
		this.x = 5;
		this.y = 5;
		this.barHeight = 13;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		int tY = this.barHeight;
		for (Module mod : Module.getCategoryModules(this.category)) {
			Button modButton = new Button(mod, this, tY);
			this.components.add(modButton);
			tY += 12;
		}
	}

	public void renderFrame() {
		Gui.drawRect(this.x - 3, this.y, this.x + this.width + 3, this.y + this.barHeight, 0xFFab4949);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(this.category.getName(), ((this.x + 2) * 2 + 5), (this.y + 2) * 2 + 5, -1);
		Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(this.open ? "-" : "+", ((this.x + this.width - 10) * 2 + 5), (this.y + 2) * 2 + 5, -1);
		GL11.glPopMatrix();
		if (this.open && !this.components.isEmpty()) {
			for (Component component : this.components) {
				component.renderComponent();
			}
		}
	}
	
	public ArrayList<Component> getComponents() {
		return this.components;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}

	public boolean isOpen() {
		return this.open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void refresh() {
		int offset = this.barHeight;
		for (Component component : this.components) {
			component.setOffset(offset);
			offset += component.getHeight();
		}
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public void updatePosition(int mouseX, int mouseY) {
		if (this.isDragging) {
			this.setX(mouseX - this.dragX);
			this.setY(mouseY - this.dragY);
		}
	}

	public boolean isWithinHeader(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
	}
}
