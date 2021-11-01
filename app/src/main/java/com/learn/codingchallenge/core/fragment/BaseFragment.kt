package com.learn.codingchallenge.core.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.learn.codingchallenge.core.viewmodel.BaseViewModel
import com.learn.codingchallenge.utils.autoCleared
import kotlin.properties.Delegates

/**
 * Created by Rafiqul Hasan
 */
abstract class BaseFragment<ViewModel : BaseViewModel, DataBinding : ViewDataBinding> : Fragment() {
    protected var fragmentCommunicator: FragmentCommunicator? = null
    protected var dataBinding: DataBinding by autoCleared()
    private var viewModel: ViewModel by Delegates.notNull()

    /** Override to set layout resource id
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutResourceId: Int

    /**
     * Override to get viewModel
     * @return view model instance
     */
    abstract fun getVM(): ViewModel

    /**
     * Override to set viewModel to dataBinding
     * Call with onViewCreated
     */
    protected abstract fun bindViewModel(binding: DataBinding, viewModel: ViewModel)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentCommunicator = context as? FragmentCommunicator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel(dataBinding, viewModel)
    }

    @Suppress("SameParameterValue")
    protected fun navigateFragment(@IdRes navigationId: Int, bundle: Bundle? = null) {
        findNavController().navigate(navigationId, bundle)
    }
}