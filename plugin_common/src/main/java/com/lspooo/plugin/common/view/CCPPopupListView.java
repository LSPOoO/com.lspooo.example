/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.lspooo.plugin.common.view;

import android.content.Context;
import android.widget.ListView;

import com.lspooo.plugin.common.R;

public class CCPPopupListView extends ListView {

	public boolean mRequestFocusable;
	
	/**
	 * @param context
	 */
	public CCPPopupListView(Context context , boolean focusable) {
		super(context, null , R.attr.dropDownListViewStyle);
		mRequestFocusable = focusable;
		setCacheColorHint(0);
	}

	public void setRequestFocusable(boolean focusable) {
		mRequestFocusable = focusable;
	}
	
}
