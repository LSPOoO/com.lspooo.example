package com.lspooo.plugin.common.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author 容联•云通讯
 * @since 2017/4/9
 */
public class PasterEditText extends EditText {

    private Context context;
    private ClipboardManager mClipMgr;
    public int length;

    public PasterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mClipMgr = null;
        length = 0;
        init();
    }

    public PasterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mClipMgr = null;
        length = 0;
        init();
    }


    public int getLength() {
        return length;
    }

    private void init(){
        mClipMgr = (ClipboardManager)this.context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {

        if(id == android.R.id.selectAll) {
            if(this.mClipMgr != null &&
                    this.mClipMgr.getText() != null &&
                    this.mClipMgr.getText() instanceof String &&
                    this.mClipMgr.getText() != null &&
                    this.mClipMgr.getText().length() > 0) {

                length += this.mClipMgr.getText().length();
            }
        }

        return super.onTextContextMenuItem(id);
    }
}

