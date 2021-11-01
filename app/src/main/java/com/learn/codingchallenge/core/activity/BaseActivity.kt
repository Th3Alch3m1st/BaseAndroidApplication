package com.learn.codingchallenge.core.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.learn.codingchallenge.core.fragment.FragmentCommunicator
import com.learn.codingchallenge.core.viewmodel.BaseViewModel
import kotlin.properties.Delegates

/**
 * Created by Rafiqul Hasan
 */

abstract class BaseActivity<ViewModel : BaseViewModel, DataBinding : ViewDataBinding> :
    AppCompatActivity(),
    FragmentCommunicator {
    protected var dataBinding: DataBinding by Delegates.notNull()
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
     * Override to bind viewModel to dataBinding
     */
    protected abstract fun bindViewModel(binding: DataBinding, viewModel: ViewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
        viewModel = getVM()
        bindViewModel(dataBinding, viewModel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return try {
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } catch (ex: Exception) {
            false
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun setActionBar(toolbar: Toolbar, enableBackButton: Boolean) {
        setSupportActionBar(toolbar)
        if (enableBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
    }
}