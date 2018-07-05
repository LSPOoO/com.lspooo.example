package com.lspooo.example.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.share.R;
import com.lspooo.plugin.share.ShareInitHelper;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lspooo on 2018/7/2.
 */

public class WXEntryActivity extends CommonActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IWXAPI api = WXAPIFactory.createWXAPI(this, ShareInitHelper.WX_APP_ID);
        api.handleIntent(getIntent(), new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {
            }

            @Override
            public void onResp(BaseResp baseResp) {
                //形参resp 有下面两个个属性比较重要
                //1.resp.errCode
                //2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
                switch (baseResp.errCode) { //根据需要的情况进行处理
                    case BaseResp.ErrCode.ERR_OK:
                        //正确返回
//                        Intent intent = new Intent(this, xxx.class);
//                        intent.putExtra("errCode", resp.errCode);
//                        intent.putExtra("errStr", resp.transaction);
//                        startActivity(intent);
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        //用户取消
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        //认证被否决
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_SENT_FAILED:
                        //发送失败
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_UNSUPPORT:
                        //不支持错误
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_COMM:
                        //一般错误
                        finish();
                        break;
                    default:
                        //其他不可名状的情况
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wx_share_result;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
