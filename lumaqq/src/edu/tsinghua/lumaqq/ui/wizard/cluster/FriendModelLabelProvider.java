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
package edu.tsinghua.lumaqq.ui.wizard.cluster;

import edu.tsinghua.lumaqq.models.User;
import edu.tsinghua.lumaqq.qq.beans.ContactInfo;
import edu.tsinghua.lumaqq.resource.Resources;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * FriendModel的label provider
 * 
 * @author luma
 */
class FriendModelLabelProvider extends LabelProvider implements
        ITableLabelProvider {
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
        if(columnIndex == 0) {
            User f = (User)element;
            return Resources.getInstance().getSmallHead(f.headId);
        } else
            return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
    	User f = (User)element;
        switch(columnIndex) {
            case 0:
                return String.valueOf(f.qq);
            case 1:
                return f.displayName;
            case 2:
                ContactInfo info = f.info;
                if(info != null)
                    return info.province;
                else
                    return "";
            default:
                return "";
        }
    }
}
