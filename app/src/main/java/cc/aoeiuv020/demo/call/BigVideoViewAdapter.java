package cc.aoeiuv020.demo.call;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class BigVideoViewAdapter extends VideoViewAdapter {
    private final static Logger log = LoggerFactory.getLogger(SmallVideoViewAdapter.class);

    private int mBigUid;

    public BigVideoViewAdapter(Context context, int exceptedUid, HashMap<Integer, UserStatusData> userList, VideoViewEventListener listener) {
        super(context, userList, listener);
        mBigUid = exceptedUid;
        log.debug("SmallVideoViewAdapter " + " " + (mBigUid & 0xFFFFFFFFL));
    }

    @Override
    protected void customizedInit(HashMap<Integer, UserStatusData> userList, boolean force) {
        mUsers.clear();
        mUsers.add(userList.get(mBigUid));

        if (force || mItemWidth == 0 || mItemHeight == 0) {
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            mItemWidth = outMetrics.widthPixels;
            mItemHeight = outMetrics.heightPixels;
        }
    }

    @Override
    public void notifyUiChanged(HashMap<Integer, UserStatusData> userList, int uidExcepted) {
        mUsers.clear();

        mBigUid = uidExcepted;

        log.debug("notifyUiChanged " + " " + (uidExcepted & 0xFFFFFFFFL) + " " + userList);
        mUsers.add(userList.get(mBigUid));

        notifyDataSetChanged();
    }

    public int getExceptedUid() {
        return mBigUid;
    }
}
