package com.maxwell.kmeth.gui.component.components;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.maxwell.kmeth.gui.component.Component;
import com.maxwell.kmeth.gui.component.Frame;
import com.maxwell.kmeth.gui.component.components.sub.Checkbox;
import com.maxwell.kmeth.gui.component.components.sub.Keybind;
import com.maxwell.kmeth.gui.component.components.sub.Slider;
import com.maxwell.kmeth.modules.Module;
import com.maxwell.kmeth.utilites.Render;
import com.maxwell.kmeth.utilites.Wrapper;
import com.maxwell.kmeth.values.BooleanValue;
import com.maxwell.kmeth.values.NumberValue;

import net.minecraft.client.gui.Gui;

public class Button extends Component {
	private Module mod;
	private Frame parent;
	private int offset;
	private boolean hovered;
	private ArrayList<Component> subcomponents;
	private boolean open;

	public Button(Module mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<Component>();
		this.open = false;
		int optionY = offset + 12;
		if (!mod.getValues().isEmpty()) {
			for (NumberValue numberValue : mod.getValues()) {
				Slider slider = new Slider(numberValue, this, optionY);
				this.subcomponents.add(slider);
				optionY += 12;
			}
		}
		if (!mod.getOptions().isEmpty()) {
			for (BooleanValue booleanValue : mod.getOptions()) {
				Checkbox checkbox = new Checkbox(booleanValue, this, optionY);
				this.subcomponents.add(checkbox);
				optionY += 12;
			}
		}
		this.subcomponents.add(new Keybind(this, optionY));
	}

	@Override
	public void setOffset(int newOffset) {
		this.offset = newOffset;
		int optionY = this.offset + 12;
		for (Component component : this.subcomponents) {
			component.setOffset(optionY);
			optionY += 12;
		}
	}

	@Override
	public void renderComponent() {
		Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, this.hovered ? (this.mod.getState() ? new Color(-14540254).darker().getRGB() : -14540254) : (this.mod.getState() ? new Color(14, 14, 14).getRGB() : -15658735));
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(this.mod.getName(), ((this.parent.getX() + 2) * 2), ((this.parent.getY() + this.offset + 2) * 2 + 4), this.mod.getState() ? 10066329 : -1);
		if (this.subcomponents.size() > 1) {
			Render.drawArrow(((this.parent.getX() + this.parent.getWidth() - 10) * 2), ((this.parent.getY() + this.offset + 2) * 2 + 5), this.open, this.mod.getState() ? 0xFFab4949 : this.hovered ? -11184811 : -12303292);
		}
		GL11.glPopMatrix();
		if (this.open && !this.subcomponents.isEmpty()) {
			for (Component component : this.subcomponents) {
				component.renderComponent();
			}
			Gui.drawRect(this.parent.getX() + 2, this.parent.getY() + this.offset + 12, this.parent.getX() + 3, this.parent.getY() + this.offset + (this.subcomponents.size() + 1) * 12, 0xFFab4949);
		}
	}

	@Override
	public int getHeight() {
		if (this.open) {
			return 12 * (this.subcomponents.size() + 1);
		}
		return 12;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = this.isMouseOnButton(mouseX, mouseY);
		if (!this.subcomponents.isEmpty()) {
			for (Component component : this.subcomponents) {
				component.updateComponent(mouseX, mouseY);
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.setState(!this.mod.getState());
		}
		if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for (Component component : this.subcomponents) {
			component.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void keyTyped(char typedChar, int key) {
		for (Component component : this.subcomponents) {
			component.keyTyped(typedChar, key);
		}
	}
	
	public Module getMod() {
		return this.mod;
	}
	
	public Frame getParent() {
		return this.parent;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public boolean isOpen() {
		return this.open;
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
	}
}
