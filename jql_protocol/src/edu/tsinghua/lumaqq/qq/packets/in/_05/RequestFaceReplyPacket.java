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
package edu.tsinghua.lumaqq.qq.packets.in._05;

import static edu.tsinghua.lumaqq.qq.events.QQEvent.*;
import java.nio.ByteBuffer;

import edu.tsinghua.lumaqq.qq.annotation.DocumentalPacket;
import edu.tsinghua.lumaqq.qq.annotation.LinkedEvent;
import edu.tsinghua.lumaqq.qq.annotation.PacketName;
import edu.tsinghua.lumaqq.qq.annotation.RelatedPacket;
import edu.tsinghua.lumaqq.qq.beans.QQUser;
import edu.tsinghua.lumaqq.qq.packets.PacketParseException;
import edu.tsinghua.lumaqq.qq.packets._05InPacket;
import edu.tsinghua.lumaqq.qq.packets.out._05.RequestFacePacket;

/**
 * <pre>
 * 请求自定义表情的回复包:
 * 1. 头部
 * 2. 未知的8字节，和请求包一致
 * 3. session id, 4字节
 * ----- 加密开始 --------
 * 4. 未知4字节，全0
 * ------ 加密结束 ------
 * 5. 尾部
 * </pre>
 * 
 * @author luma
 */
@DocumentalPacket
@PacketName("请求下载自定义表情回复包")
@RelatedPacket({RequestFacePacket.class})
@LinkedEvent({QQ_REQUEST_FACE_SUCCESS})
public class RequestFaceReplyPacket extends _05InPacket {
    public int sessionId;
    
    /**
     * @param buf
     * @param length
     * @param user
     * @throws PacketParseException
     */
    public RequestFaceReplyPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

	@Override
	public String getPacketName() {
		return "Request Face Reply Packet";
	}
	
    /* (non-Javadoc)
     * @see edu.tsinghua.lumaqq.qq.packets._05InPacket#getCryptographStart()
     */
	@Override
    protected int getCryptographStart() {
        return 12;
    }

    /* (non-Javadoc)
     * @see edu.tsinghua.lumaqq.qq.packets.Packet#parseBody(java.nio.ByteBuffer)
     */
	@Override
    protected void parseBody(ByteBuffer buf) throws PacketParseException {
        buf.getLong();
        sessionId = buf.getInt();        
    }
    
    /* (non-Javadoc)
     * @see edu.tsinghua.lumaqq.qq.packets._05InPacket#decryptBody(byte[], int, int)
     */
	@Override
    protected byte[] decryptBody(byte[] body, int offset, int length) {
        byte[] ret = new byte[length];
        System.arraycopy(body, offset, ret, 0, length);
        return ret;
    }
}
