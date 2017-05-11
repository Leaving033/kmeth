package com.maxwell.kmeth.utilites;

import org.lwjgl.opengl.GL11;

public class Render {
	// This whole entire class was made for me by Dustin as I'm horrible at OpenGL.
	
	public static void drawCheckmark(float x, float y, int hexColor) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		hexColor(hexColor);
		GL11.glLineWidth(2);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x + 1, y + 1);
		GL11.glVertex2d(x + 3, y + 4);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x + 3, y + 4);
		GL11.glVertex2d(x + 6, y - 2);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}

	public static void drawArrow(float x, float y, boolean isOpen, int hexColor) {
		GL11.glPushMatrix();
		GL11.glScaled(1.3, 1.3, 1.3);
		if (isOpen) {
			y -= 1.5f;
			x += 2;
		}
		x /= 1.3;
		y /= 1.3;
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		hexColor(hexColor);
		GL11.glLineWidth(2);
		if (isOpen) {
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + 4, y + 3);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x + 4, y + 3);
			GL11.glVertex2d(x, y + 6);
			GL11.glEnd();
		} else {
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + 3, y + 4);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x + 3, y + 4);
			GL11.glVertex2d(x + 6, y);
			GL11.glEnd();
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}

	public static void hexColor(int hexColor) {
		float red = (hexColor >> 16 & 0xFF) / 255.0F;
		float green = (hexColor >> 8 & 0xFF) / 255.0F;
		float blue = (hexColor & 0xFF) / 255.0F;
		float alpha = (hexColor >> 24 & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}
}
