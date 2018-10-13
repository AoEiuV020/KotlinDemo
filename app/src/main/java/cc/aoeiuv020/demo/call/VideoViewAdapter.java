package cc.aoeiuv020.demo.call;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import cc.aoeiuv020.demo.R;

public abstract class VideoViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static Logger log = LoggerFactory.getLogger(VideoViewAdapter.class);

    protected final LayoutInflater mInflater;
    protected final Context mContext;

    protected final ArrayList<UserStatusData> mUsers;

    protected final VideoViewEventListener mListener;

    protected int mItemWidth;
    protected int mItemHeight;
    private int mDefaultChildItem = 0;

    public VideoViewAdapter(Context context, HashMap<Integer, UserStatusData> userList, VideoViewEventListener listener) {
        mInflater = LayoutInflater.from(context);
        mContext = context.getApplicationContext();

        mListener = listener;

        mUsers = new ArrayList<>();

        init(userList);
    }

    private void init(HashMap<Integer, UserStatusData> userList) {
        mUsers.clear();

        customizedInit(userList, true);
    }

    protected abstract void customizedInit(HashMap<Integer, UserStatusData> userList, boolean force);

    public abstract void notifyUiChanged(HashMap<Integer, UserStatusData> userList, int uidExtra);

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup v = (ViewGroup) mInflater.inflate(R.layout.video_view_container, parent, false);
        v.getLayoutParams().width = mItemWidth;
        v.getLayoutParams().height = mItemHeight;
        mDefaultChildItem = v.getChildCount();
        return new VideoUserStatusHolder(v);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoUserStatusHolder myHolder = ((VideoUserStatusHolder) holder);

        final UserStatusData user = mUsers.get(position);

        log.debug("onBindViewHolder " + position + " " + user + " " + myHolder + " " + myHolder.itemView + " " + mDefaultChildItem);

        FrameLayout holderView = (FrameLayout) myHolder.itemView;

        holderView.setOnTouchListener(new OnDoubleTapListener(mContext) {
            @Override
            public void onDoubleTap(View view, MotionEvent e) {
                if (mListener != null) {
                    mListener.onItemDoubleClick(view, user);
                }
            }

            @Override
            public void onSingleTapUp() {
            }
        });

        if (holderView.getChildCount() > mDefaultChildItem) {
            View oldView = holderView.findViewById(R.id.video_surface_view);
            if (oldView != null) {
                holderView.removeView(oldView);
            }
        }
        SurfaceView target = user.mView;
        target.setId(R.id.video_surface_view);
        ViewParent parent = target.getParent();
        if (parent != null) {
            // 正常来说不会走到这，但如果分两个列表，一个view从一个列表移到另一个列表，过程态可能出现这种情况，
            ((ViewGroup) parent).removeView(target);
        }
        holderView.addView(target, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public int getItemCount() {
        log.debug("getItemCount " + mUsers.size());
        return mUsers.size();
    }

    @Override
    public long getItemId(int position) {
        UserStatusData user = mUsers.get(position);

        SurfaceView view = user.mView;
        if (view == null) {
            throw new NullPointerException("SurfaceView destroyed for user " + user.mUid);
        }

        return (String.valueOf(user.mUid) + System.identityHashCode(view)).hashCode();
    }

    public void notifyChanged(@NotNull UserStatusData user) {
        // TODO: 可以改成只刷新一个，
        notifyDataSetChanged();
    }
}
