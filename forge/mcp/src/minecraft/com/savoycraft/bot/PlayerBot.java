/**
 * Copyright (c) 2013 William Karnavas 
 * All Rights Reserved
 * 
 * This file is part of SavoyCraft.
 * 
 * SavoyCraft is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SavoyCraft is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SavoyCraft. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.savoycraft.bot;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;

import com.savoycraft.cue.Cue;
import com.savoycraft.cue.NoteCue;

/**
 *
 */
public class PlayerBot extends Bot {

	/**
	 * A duplicate of Bot.entity, to avoid excess casting
	 */
	private EntityVillager entityVillager;

	public PlayerBot(String name) {
		super(name);

		System.out.println("Creating bot: " + name);

		setEntity(new EntityVillager(Minecraft.getMinecraft().theWorld));

		entityVillager.setJumping(false);
		entityVillager.getDataWatcher().updateObject(5, name);

		posX = Minecraft.getMinecraft().thePlayer.posX;
		posY = Minecraft.getMinecraft().thePlayer.posY - 1.5;
		posZ = Minecraft.getMinecraft().thePlayer.posZ - 3;

		Minecraft.getMinecraft().theWorld.addEntityToWorld(2034923, entity);
	}

	@Override
	public void update() {
		Minecraft.getMinecraft().theWorld.spawnParticle("reddust", posX, posY,
				posZ, 0, 0, 0);

		if (!entity.isCollidedVertically) {
			posY -= 0.1;
		}

		entity.posX = posX;
		entity.posY = posY;
		entity.posZ = posZ;

		entity.serverPosX = (int) posX;
		entity.serverPosY = (int) posY;
		entity.serverPosZ = (int) posZ;

		entity.setPosition(posX, posY, posZ);
	}

	@Override
	public void kill() {
		entity.setDead();
	}

	@Override
	public void executeCue(Cue cue) {
		if (cue instanceof NoteCue) {
			Minecraft.getMinecraft().theWorld.spawnParticle("note", posX,
					posY + 2.5, posZ, 0, 0, 0);

		}
	}

	@Override
	public void setEntity(Entity entity) {
		super.setEntity(entity);

		entityVillager = (EntityVillager) entity;
	}

}
