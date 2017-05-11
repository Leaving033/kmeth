package com.maxwell.kmeth.gui.component.components.sub;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.maxwell.kmeth.gui.component.Component;
import com.maxwell.kmeth.gui.component.components.Button;
import com.maxwell.kmeth.utilites.Wrapper;

import net.minecraft.client.gui.Gui;

public class Keybind extends Component {
	private boolean hovered;
	private boolean binding;
	private Button parent;
	private int offset;
	private int x;
	private int y;

	public Keybind(Button button, int offset) {
		this.parent = button;
		this.x = button.getParent().getX() + button.getParent().getWidth();
		this.y = button.getParent().getY() + button.getOffset();
		this.offset = offset;
	}

	@Override
	public void renderComponent() {
		Gui.drawRect(this.parent.getParent().getX() + 2, this.parent.getParent().getY() + this.offset, this.parent.getParent().getX() + this.parent.getParent().getWidth() * 1, this.parent.getParent().getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
		Gui.drawRect(this.parent.getParent().getX(), this.parent.getParent().getY() + this.offset, this.parent.getParent().getX() + 2, this.parent.getParent().getY() + this.offset + 12, -15658735);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(this.binding ? "Press a key.." : ("Keybind: " + Keyboard.getKeyName(this.parent.getMod().getKey())), ((this.parent.getParent().getX() + 7) * 2), ((this.parent.getParent().getY() + this.offset + 2) * 2 + 4), -1);
		GL11.glPopMatrix();
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = this.isMouseOnButton(mouseX, mouseY);
		this.y = this.parent.getParent().getY() + this.offset;
		this.x = this.parent.getParent().getX();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
			this.binding = !this.binding;
		}
	}

	@Override
	public void keyTyped(char typedChar, int key) {
		if (this.binding) {
			if (key == 14) {
				this.parent.getMod().setKey(0);
				this.binding = false;
				return;
			} else {
				this.parent.getMod().setKey(key);
				this.binding = false;
			}
		}
	}

	@Override
	public void setOffset(int newOffset) {
		this.offset = newOffset;
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}
