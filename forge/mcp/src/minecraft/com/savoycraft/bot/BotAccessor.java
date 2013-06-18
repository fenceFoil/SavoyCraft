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

import aurelienribon.tweenengine.TweenAccessor;

/**
 * @author BJ
 * 
 */
public class BotAccessor implements TweenAccessor<Bot> {

	public static final int TWEEN_TYPE_POSXYZ = 100;
	public static final int TWEEN_TYPE_ROT = 200;
	public static final int TWEEN_TYPE_ROTHEAD = 300;
	public static final int TWEEN_TYPE_POSY = 101;

	@Override
	public int getValues(Bot arg0, int arg1, float[] arg2) {
		switch (arg1) {
		case TWEEN_TYPE_POSXYZ:
			arg2[0] = (float) arg0.getPosition()[0];
			arg2[1] = (float) arg0.getPosition()[1];
			arg2[2] = (float) arg0.getPosition()[2];
			return 3;
		case TWEEN_TYPE_POSY:
			arg2[0] = (float) arg0.getPosition()[1];
			return 1;
		case TWEEN_TYPE_ROT:
			// TODO
			return 1;
		case TWEEN_TYPE_ROTHEAD:
			// TODO
			return 2;
		default:
			return 0;
		}
	}

	@Override
	public void setValues(Bot arg0, int arg1, float[] arg2) {
		switch (arg1) {
		case TWEEN_TYPE_POSXYZ:
			arg0.setPosition(arg2[0], arg2[1], arg2[2]);
			break;
		case TWEEN_TYPE_POSY:
			arg0.setPosY(arg2[0]);
			break;
		case TWEEN_TYPE_ROT:
			// TODO
			break;
		case TWEEN_TYPE_ROTHEAD:
			// TODO
			break;
		}
	}

}
