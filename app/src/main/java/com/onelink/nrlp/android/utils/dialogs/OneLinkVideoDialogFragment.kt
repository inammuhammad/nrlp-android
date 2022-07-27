package com.onelink.nrlp.android.utils.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.video_dialog.*


const val ARG_PARAM_VIDEO_ID = "VIDEO_ID"

class OneLinkVideoDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(
            isAlertOnly: Boolean,
            vidId: String
        ): OneLinkVideoDialogFragment {
            val oneLinkVideoDialogFragment = OneLinkVideoDialogFragment()
            val args = Bundle()
            args.putBoolean(ARG_PARAM_IS_ALERT, isAlertOnly)
            args.putString(ARG_PARAM_VIDEO_ID, vidId)
            oneLinkVideoDialogFragment.arguments = args
            return oneLinkVideoDialogFragment
        }
    }

    class Builder {
        private val instance = OneLinkVideoDialogFragment()
        private val args = Bundle()

        fun setTargetFragment(fragment: Fragment, requestCode: Int) = apply {
            instance.setTargetFragment(
                fragment, requestCode
            )
        }

        fun setCancelable(cancelable: Boolean) = apply {
            instance.isCancelable = cancelable
        }

        fun setIsAlertOnly(isAlertOnly: Boolean) = apply {
            args.putBoolean(ARG_PARAM_IS_ALERT, isAlertOnly)
        }

        fun setVideoID(id: String) = apply {
            args.putString(ARG_PARAM_VIDEO_ID, id)
        }

        fun build() = instance.apply { arguments = args }

        fun show(fm: FragmentManager, tag: String) {
            instance.apply { arguments = args }.show(fm, tag)
        }
    }

    private var isAlertOnly = false
    var vidId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_dialog, container)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: Bundle? = arguments
        isAlertOnly = args?.getBoolean(ARG_PARAM_IS_ALERT) ?: false
        vidId = args?.getString(ARG_PARAM_VIDEO_ID) ?: ""
        updateView()
    }

    private fun updateView() {

        iv_close_video.setOnSingleClickListener {
            dismiss()
        }

        val ytPlayer: YouTubePlayerView = yt_player
        ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = vidId //"q3WC-X7xDNo"
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }
}