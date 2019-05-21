package cc.aoeiuv020

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class BlankFragment : Fragment(), AnkoLogger {
    private val id = hashCode().toString(16)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        info { "$id onCreateView" }
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        info { "$id onActivityCreated" }
    }

    override fun onResume() {
        super.onResume()
        info { "$id onResume" }
    }

    override fun onPause() {
        super.onPause()
        info { "$id onPause" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        info { "$id onDestroyView" }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        info { "$id setUserVisibleHint $isVisibleToUser" }
    }
}
