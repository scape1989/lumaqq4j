/*
/*
* LumaQQ - Java QQ Client
*
* Copyright (C) 2004 lxleaves_zhang
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package edu.tsinghua.lumaqq.qq.beans;

import java.nio.ByteBuffer;

/**
 * 好友等级信息数据结构
 * 
 * @author 张松
 * @see edu.tsinghua.lumaqq.qq.packets.in.FriendLevelOpReplyPacket
 */
public class FriendLevel {
    public int qq;
    public int activeDays;
    public int level;
    public int upgradeDays;

    /**
     * 从缓冲区中读取一个FriendLevel结构
     * 
     * @param buf
     * 		ByteBuffer
     */
    public void readBean(ByteBuffer buf) {
        qq = buf.getInt();
        activeDays = buf.getInt();
        level = buf.getChar();
        upgradeDays = buf.getChar();
    }
}
