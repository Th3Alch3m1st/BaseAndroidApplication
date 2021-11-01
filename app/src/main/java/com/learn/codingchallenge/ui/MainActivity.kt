package com.learn.codingchallenge.ui

import androidx.activity.viewModels
import com.learn.codingchallenge.R
import com.learn.codingchallenge.core.activity.BaseActivity
import com.learn.codingchallenge.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Rafiqul Hasan
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModels()

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override fun getVM(): MainViewModel = viewModel

    override fun bindViewModel(binding: ActivityMainBinding, viewModel: MainViewModel) {

    }
}