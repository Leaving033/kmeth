package com.maxwell.kmeth.modules.mods.combat;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.maxwell.kmeth.modules.Module;
import com.maxwell.kmeth.utilites.Wrapper;
import com.maxwell.kmeth.values.BooleanValue;
import com.maxwell.kmeth.values.NumberValue;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;

public class AimAssist extends Module {
	private NumberValue speed;
	private NumberValue distance;
	private NumberValue fov;
	private BooleanValue requireClick;
	private BooleanValue requireWeapon;
	private BooleanValue targetNakeds;
	private BooleanValue targetInvisibles;
    private EntityPlayer currentEntity;
    
	public AimAssist() {
		super("Aim Assist", Keyboard.KEY_NONE, Category.COMBAT);
		this.addValue(this.speed = new NumberValue("Speed", 3.0, 1.0, 10.0));
		this.addValue(this.distance = new NumberValue("Distance", 4.2, 3.0, 6.0));
		this.addValue(this.fov = new NumberValue("FOV", 32.0, 1.0, 360.0));
		this.addOption(this.requireClick = new BooleanValue("Require Click", true));
		this.addOption(this.requireWeapon = new BooleanValue("Require Weapon", false));
		this.addOption(this.targetNakeds = new BooleanValue("Target Nakeds", false));
		this.addOption(this.targetInvisibles = new BooleanValue("Target Invisibles", false));
	}

    @SubscribeEvent
    public void clientTick(ClientTickEvent event) {
		if (Wrapper.getPlayer() != null) {
			if (this.currentEntity == null || Wrapper.getPlayer().getDistanceToEntity(this.currentEntity) > this.distance.getValue()) {
				this.currentEntity = this.findEntity();
			} else if (this.shouldRotate(this.currentEntity)) {
				if (this.getDistanceFromMouse(this.currentEntity) > 8.0) {
					this.faceEntity(this.currentEntity, Math.min(2.0 * this.speed.getValue(), this.speed.getValue() * 2.0 * Math.max(0.2, this.getDistanceFromMouse(this.currentEntity) / Wrapper.getGameSettings().fovSetting)));
				}
			} else {
				this.currentEntity = null;
			}
		}
    }
    
    private EntityPlayer findEntity() {
        if (Wrapper.getWorld() != null) {
            for (Object object : Wrapper.getWorld().playerEntities) {
                EntityPlayer player = (EntityPlayer) object;
                if (player.getCommandSenderName().equals(Wrapper.getPlayer().getCommandSenderName())) {
                    continue;
                }
                if (this.shouldRotate(player)) {
                    return player;
                }
            }
        }
        return null;
    }
    
	private boolean shouldRotate(EntityPlayer entity) {
		if (!Wrapper.getPlayer().isEntityAlive() || !entity.isEntityAlive()) {
			return false;
		}
		if (Wrapper.getPlayer().getDistanceToEntity(entity) > this.distance.getValue()) {
			return false;
		}
		if (!Wrapper.getPlayer().canEntityBeSeen(entity)) {
			return false;
		}
		if (this.getDistanceFromMouse(entity) > this.fov.getValue() / 2) {
			return false;
		}
		if (!(Wrapper.getMinecraft().currentScreen == null)) {
			return false;
		}
		if (!Mouse.isButtonDown(0) && this.requireClick.isEnabled()) {
			return false;
		}
		if (Wrapper.getPlayer().getHeldItem() != null && !(Wrapper.getPlayer().getHeldItem().getItem() instanceof ItemSword) && !(Wrapper.getPlayer().getHeldItem().getItem() instanceof ItemAxe) && this.requireWeapon.isEnabled()) {
			return false;
		}
		if (Wrapper.getPlayer().getHeldItem() == null && this.requireWeapon.isEnabled()) {
			return false;
		}
		if (entity.isInvisible() && !this.targetInvisibles.isEnabled()) {
			return false;
		}
		if (!this.hasArmor(entity) && !this.targetNakeds.isEnabled()) {
			return false;
		}
		return true;
	}
    
    private boolean hasArmor(EntityPlayer player) {
        ItemStack[] armor = player.inventory.armorInventory;
        return armor != null && (armor[0] != null || armor[1] != null || armor[2] != null || armor[3] != null);
    }
    
    private int getDistanceFromMouse(Entity entity) {
        float[] neededRotations = this.getRotationsNeeded(entity);
        if (neededRotations != null) {
            float distanceFromMouse = MathHelper.sqrt_float(Wrapper.getPlayer().rotationYaw - neededRotations[0] * Wrapper.getPlayer().rotationYaw - neededRotations[0] + Wrapper.getPlayer().rotationPitch - neededRotations[1] * Wrapper.getPlayer().rotationPitch - neededRotations[1]);
            return (int) distanceFromMouse;
        }
        return -1;
    }
    
    private float[] getRotationsNeeded(Entity entity) {
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Wrapper.getPlayer().posX;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
        } else {
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
        }
        double diffZ = entity.posZ - Wrapper.getPlayer().posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { Wrapper.getPlayer().rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Wrapper.getPlayer().rotationYaw), Wrapper.getPlayer().rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Wrapper.getPlayer().rotationPitch) };
    }
    
    private void faceEntity(Entity entity, double speed) {
        float[] rotations = this.getRotationsNeeded(entity);
        if (rotations != null) {
            Wrapper.getPlayer().rotationYaw = (float) this.limitAngleChange(Wrapper.getPlayer().prevRotationYaw, rotations[0], speed);
        }
    }
    
    private double limitAngleChange(double current, double intended, double speed) {
        double change = intended - current;
        if (change > speed) {
            change = speed;
        } else if (change < -speed) {
            change = -speed;
        }
        return current + change;
    }
}
