package com.maxwell.kmeth.modules.mods.combat;

import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.maxwell.kmeth.modules.Module;
import com.maxwell.kmeth.utilites.Timer;
import com.maxwell.kmeth.utilites.Wrapper;
import com.maxwell.kmeth.values.BooleanValue;
import com.maxwell.kmeth.values.NumberValue;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Autoclicker extends Module {
	private NumberValue cps;
	private BooleanValue requireWeapon;
	private Timer timer = new Timer();
	private Random random = new Random();

	public Autoclicker() {
		super("Autoclicker", Keyboard.KEY_NONE, Category.COMBAT);
		this.addValue(this.cps = new NumberValue("Average CPS", 8.0, 1.0, 20.0));
		this.addOption(this.requireWeapon = new BooleanValue("Require Weapon", false));
	}

	// Horribly randomized. Anyone with a brain could easily detect this with a simple server sided check.
	@SubscribeEvent
	public void clientTick(ClientTickEvent event) {
		if (Wrapper.getPlayer() != null) {
			if (this.shouldClick()) {
				if (this.timer.hasReached(1000.0 / this.cps.getValue() - random.nextInt(4))) {
					Wrapper.getPlayer().swingItem();
					if (Wrapper.getMinecraft().objectMouseOver != null && Wrapper.getMinecraft().objectMouseOver.entityHit != null) {
						Wrapper.getPlayerController().attackEntity(Wrapper.getPlayer(), Wrapper.getMinecraft().objectMouseOver.entityHit);
					}
					if (this.getModule(Reach.class).getState()) {
						if ((Wrapper.getMinecraft().objectMouseOver == null || Wrapper.getMinecraft().objectMouseOver.entityHit == null)) {
							if (this.findTarget(Reach.getDistance()) != null) {
								Wrapper.getPlayerController().attackEntity(Wrapper.getPlayer(), this.findTarget(Reach.getDistance()));
							}
						}
					}
					timer.reset();
				}
			}
		}
	}

	private boolean shouldClick() {
		if (Wrapper.getPlayer().getHeldItem() != null && !(Wrapper.getPlayer().getHeldItem().getItem() instanceof ItemSword) && !(Wrapper.getPlayer().getHeldItem().getItem() instanceof ItemAxe) && this.requireWeapon.isEnabled()) {
			return false;
		}
		if (Wrapper.getPlayer().getHeldItem() == null && this.requireWeapon.isEnabled()) {
			return false;
		}
		if (!Wrapper.getGameSettings().keyBindAttack.getIsKeyPressed()) {
			return false;
		}
		if (Wrapper.getPlayer().isUsingItem()) {
			return false;
		}
		if (Wrapper.getMinecraft().currentScreen != null) {
			return false;
		}
		return true;
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
}
