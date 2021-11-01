package com.learn.codingchallenge.core.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Created by Rafiqul Hasan
 */
abstract class BaseViewModel : ViewModel() {

    protected var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}