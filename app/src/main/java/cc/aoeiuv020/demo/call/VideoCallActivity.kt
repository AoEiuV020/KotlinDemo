package cc.aoeiuv020.demo.call

import android.content.Context
import android.graphics.PorterDuff
import android.media.AudioManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import cc.aoeiuv020.anull.notNull
import cc.aoeiuv020.demo.R
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import kotlinx.android.synthetic.main.activity_video_call.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.warn

class VideoCallActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        const val CHANNEL = "Channel1"

        fun start(ctx: Context) {
            ctx.startActivity<VideoCallActivity>()
        }
    }

    private lateinit var rtcEngine: RtcEngine
    private val mUidsList = HashMap<Int, SurfaceView>()
    private var mVideoMuted = false
    private var mAudioMuted = false
    private var mAudioRouting = -1 // Default
    private lateinit var mSmallVideoViewAdapter: SmallVideoViewAdapter
    private var localUid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)
        // 改音量调节键的效果，
        volumeControlStream = AudioManager.STREAM_VOICE_CALL

        rtcEngine = RtcEngine.create(this, "6ffa586315ed49e6a8cdff064ad8a0b0", object : IRtcEngineEventHandler() {
            override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
                debug { "self $uid joined $channel" }
                runOnUiThread {
                    if (!isFinishing) {
                        doRenderLocalUi(uid)
                    }
                }
            }

            override fun onLeaveChannel(stats: RtcStats?) {
                debug { "self leave" }
                finish()
            }

            override fun onUserJoined(uid: Int, elapsed: Int) {
                debug { "$uid joined" }
                runOnUiThread {
                    if (!isFinishing) {
                        doRenderRemoteUi(uid)
                    }
                }
            }

            override fun onUserOffline(uid: Int, reason: Int) {
                debug { "$uid offline" }
                runOnUiThread {
                    if (!isFinishing) {
                        doRemoveRemoteUi(uid)
                    }
                }
            }

            // 这个不会调用，
            override fun onUserMuteVideo(uid: Int, muted: Boolean) {
                debug { "$uid muted: $muted" }
                runOnUiThread {
                    if (!isFinishing) {
                        doHideTargetView(uid, muted)
                    }
                }
            }

            // 一开始就是禁用的话不会回调，
            override fun onUserEnableVideo(uid: Int, enabled: Boolean) {
                debug { "$uid enabled: $enabled" }
                runOnUiThread {
                    if (!isFinishing) {
                        doHideTargetView(uid, !enabled)
                    }
                }
            }

            override fun onAudioRouteChanged(routing: Int) {
                debug { "onAudioRouteChanged: $routing" }
                runOnUiThread {
                    if (!isFinishing) {
                        notifyHeadsetPlugged(routing)
                    }
                }
            }
        }).apply {
            enableAudio()
            enableVideo()
            // 音量回调，
//            enableAudioVolumeIndication(200, 3) // 200 ms
            setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)//设置为通信模式（默认）
/*
            //视频配置，设置为360P
            @Suppress("DEPRECATION")
            setVideoProfile(Constants.VIDEO_PROFILE_360P, false)
*/
        }
        // 预览，
        doRenderLocalUi(localUid)
        rtcEngine.joinChannel(null, CHANNEL, null, localUid)
    }

    private fun replaceBig(uid: Int) {
        val view = mUidsList[uid]
        (view?.parent as? ViewGroup)?.removeView(view)
        big_container.removeAllViews()
        big_container.addView(view)
        bindToSmallVideoView(uid)
    }

    private fun doRemoveRemoteUi(uid: Int) {
        mUidsList.remove(uid) ?: return

        var bigBgUid = localUid
        if (::mSmallVideoViewAdapter.isInitialized) {
            bigBgUid = mSmallVideoViewAdapter.exceptedUid
        }

        debug("doRemoveRemoteUi $uid $bigBgUid")

        if (bigBgUid == uid) {
            // 如果是大画面被删除，就改成展示本地预览，
            switchToSmallVideoView(localUid)
        } else {
            switchToSmallVideoView(bigBgUid)
        }
    }

    private fun switchToSmallVideoView(bigBgUid: Int) {
        mUidsList.forEach { (_, s) ->
            s.setZOrderOnTop(true)
            s.setZOrderMediaOverlay(true)
        }

        val bigView = mUidsList[bigBgUid].notNull()
        bigView.setZOrderOnTop(false)
        bigView.setZOrderMediaOverlay(false)

        replaceBig(bigBgUid)
    }

    private fun bindToSmallVideoView(exceptUid: Int) {

        val recycler = small_video_view_container

        var create = false

        if (!::mSmallVideoViewAdapter.isInitialized) {
            create = true
            mSmallVideoViewAdapter = SmallVideoViewAdapter(this, localUid, exceptUid, mUidsList, VideoViewEventListener { _, user ->
                switchToSmallVideoView(user.mUid)
            })
            mSmallVideoViewAdapter.setHasStableIds(true)
        }
        recycler.setHasFixedSize(true)

        debug("bindToSmallVideoView $exceptUid")

        recycler.layoutManager = RtlLinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recycler.addItemDecoration(SmallVideoViewDecoration())
        recycler.adapter = mSmallVideoViewAdapter

        recycler.isDrawingCacheEnabled = true
        recycler.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_AUTO

        if (!create) {
            mSmallVideoViewAdapter.setLocalUid(localUid)
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null)
        }
        recycler.visibility = View.VISIBLE
    }

    @Suppress("RedundantVisibilityModifier", "UNUSED_PARAMETER")
    public fun onEndCallClicked(view: View) {
        callEnd()
    }

    @Suppress("RedundantVisibilityModifier")
    public fun onVoiceChatClicked(view: View) {
        if (mUidsList.size == 0) {
            return
        }

        val surfaceV = mUidsList[localUid]
        val parent: ViewParent? = surfaceV?.parent
        if (surfaceV == null || parent == null) {
            warn { "onVoiceChatClicked $view $surfaceV" }
            return
        }

        mVideoMuted = !mVideoMuted

        if (mVideoMuted) {
            rtcEngine.disableVideo()
        } else {
            rtcEngine.enableVideo()
        }

        val iv = view as ImageView

        iv.setImageResource(if (mVideoMuted) R.drawable.btn_video else R.drawable.btn_voice)

        hideLocalView(mVideoMuted)

        if (mVideoMuted) {
            resetToVideoDisabledUI()
        } else {
            resetToVideoEnabledUI()
        }
    }

    private fun hideLocalView(hide: Boolean) {
        doHideTargetView(localUid, hide)
    }

    private fun doHideTargetView(targetUid: Int, hide: Boolean) {
        val status = HashMap<Int, Int>()
        status[targetUid] = if (hide) UserStatusData.VIDEO_MUTED else UserStatusData.DEFAULT_STATUS
        val bigUid = mSmallVideoViewAdapter.exceptedUid
        // TODO: 关闭视频时的默认画面没做，
        if (targetUid == bigUid) {
        } else {
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, mSmallVideoViewAdapter.exceptedUid, status, null)
        }
    }

    @Suppress("RedundantVisibilityModifier", "UNUSED_PARAMETER")
    public fun onCustomizedFunctionClicked(view: View) {
        if (mVideoMuted) {
            onSwitchSpeakerClicked()
        } else {
            onSwitchCameraClicked()
        }
    }

    private fun onSwitchCameraClicked() {
        rtcEngine.switchCamera()
    }

    private fun onSwitchSpeakerClicked() {
        rtcEngine.setEnableSpeakerphone(mAudioRouting != 3)
    }

    @Suppress("RedundantVisibilityModifier")
    public fun onVoiceMuteClicked(view: View) {
        if (mUidsList.size == 0) {
            return
        }

        mAudioMuted = !mAudioMuted
        rtcEngine.muteLocalAudioStream(mAudioMuted)

        val iv = view as ImageView

        if (mAudioMuted) {
            @Suppress("DEPRECATION")
            iv.setColorFilter(resources.getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY)
        } else {
            iv.clearColorFilter()
        }
    }

    private fun resetToVideoEnabledUI() {
        val iv = customized_function_id
        iv.setImageResource(R.drawable.btn_switch_camera)
        iv.clearColorFilter()

        notifyHeadsetPlugged(mAudioRouting)
    }

    private fun resetToVideoDisabledUI() {
        val iv = customized_function_id
        iv.setImageResource(R.drawable.btn_speaker)
        iv.clearColorFilter()

        notifyHeadsetPlugged(mAudioRouting)
    }

    private fun notifyHeadsetPlugged(routing: Int) {

        mAudioRouting = routing

        if (!mVideoMuted) {
            return
        }

        val iv = customized_function_id
        if (mAudioRouting == 3) { // Speakerphone
            @Suppress("DEPRECATION")
            iv.setColorFilter(resources.getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY)
        } else {
            iv.clearColorFilter()
        }
    }

    private fun doRenderLocalUi(uid: Int) {
        // 本地预览只要一个，
        mUidsList.remove(localUid)
        localUid = uid
        if (uid in mUidsList) {
            return
        }
        val view = RtcEngine.CreateRendererView(this)
        mUidsList[uid] = view
        rtcEngine.setupLocalVideo(VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid))
        rtcEngine.startPreview()
        switchToSmallVideoView(uid)
    }

    private fun doRenderRemoteUi(uid: Int) {
        if (uid in mUidsList) {
            return
        }
        val view = RtcEngine.CreateRendererView(this)
        mUidsList[uid] = view
        rtcEngine.setupRemoteVideo(VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid))
        switchToSmallVideoView(mSmallVideoViewAdapter.exceptedUid)
    }

    private fun callEnd() {
        // 无视成功失败，会自动下线，
        rtcEngine.leaveChannel()
        rtcEngine.stopPreview()
        finish()
    }

    override fun onBackPressed() {
        callEnd()
        super.onBackPressed()
    }

    override fun onDestroy() {
        if (::rtcEngine.isInitialized) {
            callEnd()
        }
        RtcEngine.destroy()

        super.onDestroy()
    }
}
