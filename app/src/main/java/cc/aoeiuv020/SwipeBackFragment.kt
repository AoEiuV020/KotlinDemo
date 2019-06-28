package cc.aoeiuv020

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.android.synthetic.main.fragment_swipe_back.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class SwipeBackFragment : Fragment(), AnkoLogger {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_swipe_back, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val number = requireActivity().intent.getIntExtra("number", 0)
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onAny() {
                info { "f: $number-${lifecycle.currentState}" }
            }
        })

        btnNewPage.setOnClickListener {
            SwipeBackLayoutActivity.start(requireContext(), number + 1)
        }
    }
}
