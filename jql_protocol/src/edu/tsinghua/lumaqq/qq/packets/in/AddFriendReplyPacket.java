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
package edu.tsinghua.lumaqq.qq.packets.in;

import java.nio.ByteBuffer;

import edu.tsinghua.lumaqq.qq.QQ;
import edu.tsinghua.lumaqq.qq.Util;
import edu.tsinghua.lumaqq.qq.annotation.DocumentalPacket;
import edu.tsinghua.lumaqq.qq.annotation.PacketName;
import edu.tsinghua.lumaqq.qq.annotation.RelatedPacket;
import edu.tsinghua.lumaqq.qq.beans.QQUser;
import edu.tsinghua.lumaqq.qq.packets.BasicInPacket;
import edu.tsinghua.lumaqq.qq.packets.PacketParseException;
import edu.tsinghua.lumaqq.qq.packets.out.AddFriendPacket;


/**
 * <pre>
 * 这个添加好友的应答包，格式是
 * 1. 头部
 * 2. 我的QQ号的字符串形式
 * 3. 分隔符1字节，0x1F
 * 4. 回复码，成功还是需要认证
 * 5. 尾部
 * </pre>
 *
 * @author luma
 */
@Deprecated
@DocumentalPacket
@PacketName("添加好友回复包")
@RelatedPacket({AddFriendPacket.class})
public class AddFriendReplyPacket extends BasicInPacket {
    public byte replyCode;
    
    /**
     * 构造函数
     * @param buf 缓冲区
     * @param length 包长度
     * @throws PacketParseException 解析错误
     */
    public AddFriendReplyPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }
    
    /* (non-Javadoc)
     * @see edu.tsinghua.lumaqq.qq.packets.OutPacket#getPacketName()
     */
	@Override
    public String getPacketName() {
        return "Add Friend Reply Packet";
    }
    
    /* (non-Javadoc)
     * @see edu.tsinghua.lumaqq.qq.packets.InPacket#parseBody(java.nio.ByteBuffer)
     */
	@Override
    protected void parseBody(ByteBuffer buf) throws PacketParseException {
        // 解析每个字段，0x1F是分隔符，第一个字段是QQ号，第二个是应答码
        int qqNum = Util.getInt(Util.getString(buf, (byte)0x1F), 0);
        if(qqNum != user.getQQ())
            throw new PacketParseException("该消息不是发给我的消息，忽略该包");
        replyCode = Util.getByte(Util.getString(buf), QQ.QQ_AUTH_REJECT);
    }
}
