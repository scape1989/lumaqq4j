/*
* LumaQQ - Java QQ Client
*
* Copyright (C) 2004 luma <stubma@163.com>
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
package edu.tsinghua.lumaqq.qq.packets.out._05;

import java.nio.ByteBuffer;

import edu.tsinghua.lumaqq.qq.QQ;
import edu.tsinghua.lumaqq.qq.beans.QQUser;
import edu.tsinghua.lumaqq.qq.packets.PacketParseException;
import edu.tsinghua.lumaqq.qq.packets._05OutPacket;

/**
 * @author luma
 */
public class Unknown05OutPacket extends _05OutPacket {
    /**
     * 构造函数
     * 
     * @param command
     * 			命令
     * @param ack
     * 			是否需要回复
     */
    public Unknown05OutPacket(char command, boolean ack, QQUser user) {
        super(QQ.QQ_CMD_UNKNOWN, false, user);
    }

    /**
     * @param buf
     * @param length
     * @throws PacketParseException
     */
    public Unknown05OutPacket(ByteBuffer buf, int length, QQUser user)
            throws PacketParseException {
        super(buf, length, user);
    }
    
    /* (non-Javadoc)
     * @see edu.tsinghua.lumaqq.qq.packets._05OutPacket#getCryptographStart()
     */
	@Override
    protected int getCryptographStart() {
        return -1;
    }

    /* (non-Javadoc)
     * @see edu.tsinghua.lumaqq.qq.packets.Packet#putBody(java.nio.ByteBuffer)
     */
	@Override
    protected void putBody(ByteBuffer buf) {
    }
}
