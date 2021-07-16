package com.absinthe.libchecker.ui.fragment

import android.os.Bundle
import android.view.Menu
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.absinthe.libchecker.BaseActivity
import com.absinthe.libchecker.viewmodel.HomeViewModel
import rikka.widget.borderview.BorderViewDelegate

abstract class BaseListControllerFragment<T : ViewBinding> : BaseFragment<T>(), IListController {

    protected var borderDelegate: BorderViewDelegate? = null
    protected val homeViewModel by activityViewModels<HomeViewModel>()
    protected var isListReady = false
    protected var allowRefreshing = true
    protected var menu: Menu? = null

    override fun onVisibilityChanged(visible: Boolean) {
        super.onVisibilityChanged(visible)
        if (visible) {
            if (this != homeViewModel.controller) {
                homeViewModel.controller = this
                requireActivity().invalidateOptionsMenu()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()
        scheduleAppbarRaisingStatus()
    }

    override fun onDetach() {
        super.onDetach()
        if (this == homeViewModel.controller) {
            homeViewModel.controller = null
        }
    }

    override fun getAppBar() = (activity as BaseActivity?)?.appBar

    override fun getBorderViewDelegate() = borderDelegate

    override fun scheduleAppbarRaisingStatus() {
        getAppBar()?.setRaised(!(getBorderViewDelegate()?.isShowingTopBorder ?: true))
    }

    override fun isAllowRefreshing() = allowRefreshing
}
