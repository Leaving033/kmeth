package com.maxwell.kmeth.modules.mods.combat;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.maxwell.kmeth.modules.Module;
import com.maxwell.kmeth.utilites.Wrapper;
import com.maxwell.kmeth.values.NumberValue;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;

public class Reach extends Module {
	// Static so the method below could be accessed by Autoclicker.java.
	private static NumberValue distance;

	public Reach() {
		super("Reach", Keyboard.KEY_NONE, Category.COMBAT);
		this.addValue(this.distance = new NumberValue("Distance", 3.2, 3.0, 6.0));
	}

	@SubscribeEvent
	public void mouse(MouseEvent event) {
		if (Wrapper.getPlayer() != null) {
			if (event.buttonstate) {
				if (event.button == 0) {
					if ((Wrapper.getMinecraft().objectMouseOver == null || Wrapper.getMinecraft().objectMouseOver.entityHit == null)) {
						if (this.findTarget(this.distance.getValue()) != null) {
							if (!this.getModule(Autoclicker.class).getState()) {
								Wrapper.getPlayerController().attackEntity(Wrapper.getPlayer(), this.findTarget(this.distance.getValue()));
							}
						}
					}
				}
			}
		}
	}

	// Horribly decompiled method. Basically everyone pastes this method so I decided to as well.
	private Entity findTarget(double distance) {
		Minecraft func_71410_x;
		Minecraft minecraft = func_71410_x = Minecraft.getMinecraft();
		MovingObjectPosition rayTrace = func_71410_x.thePlayer.rayTrace(distance, 1.0f);
		double distanceTo = distance;
		Vec3 getPosition = func_71410_x.thePlayer.getPosition(1.0f);
		if (rayTrace != null) {
			distanceTo = rayTrace.hitVec.distanceTo(getPosition);
		}
		Minecraft minecraft2 = minecraft;
		Vec3 getLook = minecraft2.thePlayer.getLook(1.0f);
		Vec3 addVector = getPosition.addVector(getLook.xCoord * distance, getLook.yCoord * distance, getLook.zCoord * distance);
		Entity entity = null;
		WorldClient theWorld = minecraft2.theWorld;
		Minecraft minecraft3 = minecraft;
		List func_72839_b = theWorld.getEntitiesWithinAABBExcludingEntity(minecraft3.thePlayer, minecraft3.thePlayer.boundingBox.addCoord(getLook.xCoord * distance, getLook.yCoord * distance, getLook.zCoord * distance).expand(1.0, 1.0, 1.0));
		double n2 = distanceTo;
		int n3 = 0;
		int i = 0;
		while (i < func_72839_b.size()) {
			Entity entity2 = (Entity) func_72839_b.get(n3);
			if (entity2.canBeCollidedWith()) {
				double distanceTo2;
				Entity entity3 = entity2;
				float getCollisionBorderSize = entity3.getCollisionBorderSize();
				AxisAlignedBB expand = entity3.boundingBox.expand(getCollisionBorderSize, getCollisionBorderSize, getCollisionBorderSize);
				MovingObjectPosition func_72327_a = expand.calculateIntercept(getPosition, addVector);
				if (expand.isVecInside(getPosition)) {
					if (0.0 < n2 || n2 == 0.0) {
						entity = entity2;
						n2 = 0.0;
					}
				} else if (func_72327_a != null && ((distanceTo2 = getPosition.distanceTo(func_72327_a.hitVec)) < n2 || n2 == 0.0)) {
					if (entity2 == minecraft.thePlayer.ridingEntity) {
						if (n2 == 0.0) {
							entity = entity2;
						}
					} else {
						entity = entity2;
						n2 = distanceTo2;
					}
				}
			}
			i = ++n3;
		}
		return entity;
	}

	// Used by Autoclicker.java so it will attack entites from the specified distance when reach is enabled.
	public static double getDistance() {
		return distance.getValue();
	}
}
