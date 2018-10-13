package cc.aoeiuv020.demo.call;

import android.view.SurfaceView;

public class UserStatusData {
    public int mUid;
    public SurfaceView mView;
    public boolean videoMuted;

    public UserStatusData(int uid, SurfaceView view) {
        this.mUid = uid;
        this.mView = view;
    }

    @Override
    public String toString() {
        return "UserStatusData{" +
                "mUid=" + (mUid & 0XFFFFFFFFL) +
                ", mView=" + mView +
                '}';
    }
}
