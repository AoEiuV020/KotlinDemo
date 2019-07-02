package cc.aoeiuv020.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentActivity
import com.scwang.smartrefresh.layout.R
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.internal.ArrowDrawable
import com.scwang.smartrefresh.layout.internal.InternalClassics
import com.scwang.smartrefresh.layout.internal.ProgressDrawable
import com.scwang.smartrefresh.layout.util.SmartUtil
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 经典下拉头部
 * Created by SCWANG on 2017/5/28.
 */
@Suppress("ProtectedInFinal", "PropertyName", "SENSELESS_COMPARISON", "NON_EXHAUSTIVE_WHEN", "unused", "MemberVisibilityCanBePrivate", "LiftReturnOrAssignment")
class MyHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : InternalClassics<MyHeader>(context, attrs, defStyleAttr), RefreshHeader {
    //    public static String REFRESH_HEADER_UPDATE = "'Last update' M-d HH:mm";

    protected var KEY_LAST_UPDATE_TIME = "LAST_UPDATE_TIME"

    protected var mLastTime: Date? = null
    protected var mLastUpdateText: TextView
    protected var mShared: SharedPreferences? = null
    protected var mLastUpdateFormat: DateFormat
    protected var mEnableLastTime = true

    protected var mTextPulling: String? = null//"下拉可以刷新";
    protected var mTextRefreshing: String? = null//"正在刷新...";
    protected var mTextLoading: String? = null//"正在加载...";
    protected var mTextRelease: String? = null//"释放立即刷新";
    protected var mTextFinish: String? = null//"刷新完成";
    protected var mTextFailed: String? = null//"刷新失败";
    protected var mTextUpdate: String? = null//"上次更新 M-d HH:mm";
    protected var mTextSecondary: String? = null//"释放进入二楼";

    init {

        View.inflate(context, cc.aoeiuv020.R.layout.header_refresh, this)

        //        mLastUpdateText = new TextView(context);
        //        mLastUpdateText.setTextColor(0xff7c7c7c);

        val thisView = this
        mArrowView = thisView.findViewById<ImageView>(R.id.srl_classics_arrow)
        val arrowView = mArrowView
        mLastUpdateText = thisView.findViewById(R.id.srl_classics_update)
        val updateView = mLastUpdateText
        mProgressView = thisView.findViewById<ImageView>(R.id.srl_classics_progress)
        val progressView = mProgressView
        //        final ViewGroup centerLayout = mCenterLayout;

        mTitleText = thisView.findViewById(R.id.srl_classics_title)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ClassicsHeader)

        val lpArrow = arrowView.layoutParams as RelativeLayout.LayoutParams
        val lpProgress = progressView.layoutParams as RelativeLayout.LayoutParams
        val lpUpdateText = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        lpUpdateText.topMargin = ta.getDimensionPixelSize(R.styleable.ClassicsHeader_srlTextTimeMarginTop, SmartUtil.dp2px(0f))
        lpProgress.rightMargin = ta.getDimensionPixelSize(R.styleable.ClassicsFooter_srlDrawableMarginRight, SmartUtil.dp2px(20f))
        lpArrow.rightMargin = lpProgress.rightMargin

        lpArrow.width = ta.getLayoutDimension(R.styleable.ClassicsHeader_srlDrawableArrowSize, lpArrow.width)
        lpArrow.height = ta.getLayoutDimension(R.styleable.ClassicsHeader_srlDrawableArrowSize, lpArrow.height)
        lpProgress.width = ta.getLayoutDimension(R.styleable.ClassicsHeader_srlDrawableProgressSize, lpProgress.width)
        lpProgress.height = ta.getLayoutDimension(R.styleable.ClassicsHeader_srlDrawableProgressSize, lpProgress.height)

        lpArrow.width = ta.getLayoutDimension(R.styleable.ClassicsHeader_srlDrawableSize, lpArrow.width)
        lpArrow.height = ta.getLayoutDimension(R.styleable.ClassicsHeader_srlDrawableSize, lpArrow.height)
        lpProgress.width = ta.getLayoutDimension(R.styleable.ClassicsHeader_srlDrawableSize, lpProgress.width)
        lpProgress.height = ta.getLayoutDimension(R.styleable.ClassicsHeader_srlDrawableSize, lpProgress.height)

        mFinishDuration = ta.getInt(R.styleable.ClassicsHeader_srlFinishDuration, mFinishDuration)
        mEnableLastTime = ta.getBoolean(R.styleable.ClassicsHeader_srlEnableLastTime, mEnableLastTime)
        mSpinnerStyle = SpinnerStyle.values[ta.getInt(R.styleable.ClassicsHeader_srlClassicsSpinnerStyle, mSpinnerStyle.ordinal)]

        if (ta.hasValue(R.styleable.ClassicsHeader_srlDrawableArrow)) {
            mArrowView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsHeader_srlDrawableArrow))
        } else if (mArrowView.drawable == null) {
            mArrowDrawable = ArrowDrawable()
            mArrowDrawable.setColor(-0x99999a)
            mArrowView.setImageDrawable(mArrowDrawable)
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_srlDrawableProgress)) {
            mProgressView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsHeader_srlDrawableProgress))
        } else if (mProgressView.drawable == null) {
            mProgressDrawable = ProgressDrawable()
            mProgressDrawable.setColor(-0x99999a)
            mProgressView.setImageDrawable(mProgressDrawable)
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextSizeTitle)) {
            mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.ClassicsHeader_srlTextSizeTitle, SmartUtil.dp2px(16f)).toFloat())
            //        } else {
            //            mTitleText.setTextSize(16);
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextSizeTime)) {
            mLastUpdateText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.ClassicsHeader_srlTextSizeTime, SmartUtil.dp2px(12f)).toFloat())
            //        } else {
            //            mLastUpdateText.setTextSize(12);
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_srlPrimaryColor)) {
            super.setPrimaryColor(ta.getColor(R.styleable.ClassicsHeader_srlPrimaryColor, 0))
        }
        if (ta.hasValue(R.styleable.ClassicsHeader_srlAccentColor)) {
            setAccentColor(ta.getColor(R.styleable.ClassicsHeader_srlAccentColor, 0))
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextPulling)) {
            mTextPulling = ta.getString(R.styleable.ClassicsHeader_srlTextPulling)
        } else if (REFRESH_HEADER_PULLING != null) {
            mTextPulling = REFRESH_HEADER_PULLING
        } else {
            mTextPulling = context.getString(R.string.srl_header_pulling)
        }
        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextLoading)) {
            mTextLoading = ta.getString(R.styleable.ClassicsHeader_srlTextLoading)
        } else if (REFRESH_HEADER_LOADING != null) {
            mTextLoading = REFRESH_HEADER_LOADING
        } else {
            mTextLoading = context.getString(R.string.srl_header_loading)
        }
        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextRelease)) {
            mTextRelease = ta.getString(R.styleable.ClassicsHeader_srlTextRelease)
        } else if (REFRESH_HEADER_RELEASE != null) {
            mTextRelease = REFRESH_HEADER_RELEASE
        } else {
            mTextRelease = context.getString(R.string.srl_header_release)
        }
        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextFinish)) {
            mTextFinish = ta.getString(R.styleable.ClassicsHeader_srlTextFinish)
        } else if (REFRESH_HEADER_FINISH != null) {
            mTextFinish = REFRESH_HEADER_FINISH
        } else {
            mTextFinish = context.getString(R.string.srl_header_finish)
        }
        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextFailed)) {
            mTextFailed = ta.getString(R.styleable.ClassicsHeader_srlTextFailed)
        } else if (REFRESH_HEADER_FAILED != null) {
            mTextFailed = REFRESH_HEADER_FAILED
        } else {
            mTextFailed = context.getString(R.string.srl_header_failed)
        }
        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextSecondary)) {
            mTextSecondary = ta.getString(R.styleable.ClassicsHeader_srlTextSecondary)
        } else if (REFRESH_HEADER_SECONDARY != null) {
            mTextSecondary = REFRESH_HEADER_SECONDARY
        } else {
            mTextSecondary = context.getString(R.string.srl_header_secondary)
        }
        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextRefreshing)) {
            mTextRefreshing = ta.getString(R.styleable.ClassicsHeader_srlTextRefreshing)
        } else if (REFRESH_HEADER_REFRESHING != null) {
            mTextRefreshing = REFRESH_HEADER_REFRESHING
        } else {
            mTextRefreshing = context.getString(R.string.srl_header_refreshing)
        }
        if (ta.hasValue(R.styleable.ClassicsHeader_srlTextUpdate)) {
            mTextUpdate = ta.getString(R.styleable.ClassicsHeader_srlTextUpdate)
        } else if (REFRESH_HEADER_UPDATE != null) {
            mTextUpdate = REFRESH_HEADER_UPDATE
        } else {
            mTextUpdate = context.getString(R.string.srl_header_update)
        }
        mLastUpdateFormat = SimpleDateFormat(mTextUpdate, Locale.getDefault())

        ta.recycle()

        //        updateView.setId(ID_TEXT_UPDATE);
        progressView.animate().interpolator = null
        updateView.visibility = if (mEnableLastTime) View.VISIBLE else View.GONE
        //        centerLayout.addView(updateView, lpUpdateText);
        mTitleText.text = if (thisView.isInEditMode) mTextRefreshing else mTextPulling

        if (thisView.isInEditMode) {
            arrowView.visibility = View.GONE
        } else {
            progressView.visibility = View.GONE
        }

        var flag = false
        try {//try 不能删除-否则会出现兼容性问题
            if (context is FragmentActivity) {
                val manager = context.supportFragmentManager
                if (manager != null) {
                    @SuppressLint("RestrictedApi")
                    val fragments = manager.fragments
                    if (fragments != null && fragments.size > 0) {
                        setLastUpdateTime(Date())
                        flag = true
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        if (!flag) {
            KEY_LAST_UPDATE_TIME += context.javaClass.name
            mShared = context.getSharedPreferences("ClassicsHeader", Context.MODE_PRIVATE)
            setLastUpdateTime(Date(mShared!!.getLong(KEY_LAST_UPDATE_TIME, System.currentTimeMillis())))
        }

    }

    //    @Override
    //    protected ClassicsHeader self() {
    //        return this;
    //    }

    //</editor-fold>

    //<editor-fold desc="RefreshHeader">

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        if (success) {
            mTitleText.text = mTextFinish
            if (mLastTime != null) {
                setLastUpdateTime(Date())
            }
        } else {
            mTitleText.text = mTextFailed
        }
        return super.onFinish(layout, success)//延迟500毫秒之后再弹回
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        val arrowView = mArrowView
        val updateView = mLastUpdateText
        when (newState) {
            RefreshState.None -> {
                updateView.visibility = if (mEnableLastTime) View.VISIBLE else View.GONE
                mTitleText.text = mTextPulling
                arrowView.visibility = View.VISIBLE
                arrowView.animate().rotation(0f)
            }
            RefreshState.PullDownToRefresh -> {
                mTitleText.text = mTextPulling
                arrowView.visibility = View.VISIBLE
                arrowView.animate().rotation(0f)
            }
            RefreshState.Refreshing, RefreshState.RefreshReleased -> {
                mTitleText.text = mTextRefreshing
                arrowView.visibility = View.GONE
            }
            RefreshState.ReleaseToRefresh -> {
                mTitleText.text = mTextRelease
                arrowView.animate().rotation(180f)
            }
            RefreshState.ReleaseToTwoLevel -> {
                mTitleText.text = mTextSecondary
                arrowView.animate().rotation(0f)
            }
            RefreshState.Loading -> {
                arrowView.visibility = View.GONE
                updateView.visibility = if (mEnableLastTime) View.INVISIBLE else View.GONE
                mTitleText.text = mTextLoading
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="API">

    fun setLastUpdateTime(time: Date): MyHeader {
        val thisView = this
        mLastTime = time
        mLastUpdateText.text = mLastUpdateFormat.format(time)
        if (mShared != null && !thisView.isInEditMode) {
            mShared!!.edit().putLong(KEY_LAST_UPDATE_TIME, time.time).apply()
        }
        return this
    }

    fun setTimeFormat(format: DateFormat): MyHeader {
        mLastUpdateFormat = format
        if (mLastTime != null) {
            mLastUpdateText.text = mLastUpdateFormat.format(mLastTime)
        }
        return this
    }

    fun setLastUpdateText(text: CharSequence): MyHeader {
        mLastTime = null
        mLastUpdateText.text = text
        return this
    }

    override fun setAccentColor(@ColorInt accentColor: Int): MyHeader {
        mLastUpdateText.setTextColor(accentColor and 0x00ffffff or -0x34000000)
        return super.setAccentColor(accentColor)
    }

    fun setEnableLastTime(enable: Boolean): MyHeader {
        val updateView = mLastUpdateText
        mEnableLastTime = enable
        updateView.visibility = if (enable) View.VISIBLE else View.GONE
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this)
        }
        return this
    }

    fun setTextSizeTime(size: Float): MyHeader {
        mLastUpdateText.textSize = size
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this)
        }
        return this
    }

    //    public ClassicsHeader setTextSizeTime(int unit, float size) {
    //        mLastUpdateText.setTextSize(unit, size);
    //        if (mRefreshKernel != null) {
    //            mRefreshKernel.requestRemeasureHeightForHeader();
    //        }
    //        return this;
    //    }

    fun setTextTimeMarginTop(dp: Float): MyHeader {
        val updateView = mLastUpdateText
        val lp = updateView.layoutParams as ViewGroup.MarginLayoutParams
        lp.topMargin = SmartUtil.dp2px(dp)
        updateView.layoutParams = lp
        return this
    }

    companion object {

        val ID_TEXT_UPDATE = R.id.srl_classics_update

        var REFRESH_HEADER_PULLING: String? = null//"下拉可以刷新";
        var REFRESH_HEADER_REFRESHING: String? = null//"正在刷新...";
        var REFRESH_HEADER_LOADING: String? = null//"正在加载...";
        var REFRESH_HEADER_RELEASE: String? = null//"释放立即刷新";
        var REFRESH_HEADER_FINISH: String? = null//"刷新完成";
        var REFRESH_HEADER_FAILED: String? = null//"刷新失败";
        var REFRESH_HEADER_UPDATE: String? = null//"上次更新 M-d HH:mm";
        var REFRESH_HEADER_SECONDARY: String? = null//"释放进入二楼";
    }

}
