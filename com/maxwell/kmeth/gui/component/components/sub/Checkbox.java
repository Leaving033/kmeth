package com.maxwell.kmeth.gui.component.components.sub;

import org.lwjgl.opengl.GL11;

import com.maxwell.kmeth.gui.component.Component;
import com.maxwell.kmeth.gui.component.components.Button;
import com.maxwell.kmeth.utilites.Render;
import com.maxwell.kmeth.utilites.Wrapper;
import com.maxwell.kmeth.values.BooleanValue;

import net.minecraft.client.gui.Gui;

public class Checkbox extends Component {
	private boolean hovered;
	private BooleanValue option;
	private Button parent;
	private int offset;
	private int x;
	private int y;

	public Checkbox(BooleanValue option, Button button, int offset) {
		this.option = option;
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
		Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(this.option.getName(), ((this.parent.getParent().getX() + 10 + 4) * 2 + 5), ((this.parent.getParent().getY() + this.offset + 2) * 2 + 4), -1);
		GL11.glPopMatrix();
		Gui.drawRect(this.parent.getParent().getX() + 3 + 3, this.parent.getParent().getY() + this.offset + 3, this.parent.getParent().getX() + 9 + 3, this.parent.getParent().getY() + this.offset + 9, this.hovered ? -11184811 : -12303292);
		if (this.option.isEnabled()) {
			Render.drawCheckmark(this.parent.getParent().getX() + 6, this.parent.getParent().getY() + this.offset + 4, 0xFFab4949);
		}
	}

	@Override
	public void setOffset(int newOffset) {
		this.offset = newOffset;
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
			this.option.toggle();
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}
