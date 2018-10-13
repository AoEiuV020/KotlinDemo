package cc.aoeiuv020.demo.call;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cc.aoeiuv020.demo.R;

@SuppressWarnings("WeakerAccess")
public class VideoUserStatusHolder extends RecyclerView.ViewHolder {
    public final FrameLayout holderView;
    public final RelativeLayout maskView;
    public final ImageView avatar;

    public VideoUserStatusHolder(View v) {
        super(v);
        holderView = (FrameLayout) v;
        maskView = v.findViewById(R.id.user_control_mask);
        avatar = v.findViewById(R.id.default_avatar);
    }
}
