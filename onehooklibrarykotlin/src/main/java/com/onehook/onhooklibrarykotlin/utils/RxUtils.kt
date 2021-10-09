package com.onehook.onhooklibrarykotlin.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.exceptions.OnErrorNotImplementedException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.Subject


val MainThread: Scheduler = AndroidSchedulers.mainThread()

/* 成功时候会发生 */
private val onNextStub: (Any) -> Unit = {}

/* 失败发生 */
private val onErrorStub: (Throwable) -> Unit =
    { RxJavaPlugins.onError(OnErrorNotImplementedException(it)) }

/* 无论成功失败都会发生 */
private var onTerminateStub: () -> Unit = {}

fun <T : Any> Single<T>.observeOnMainThread(
    onSuccess: (T) -> Unit = onNextStub,
    onError: (Throwable) -> Unit = onErrorStub,
    onTerminate: () -> Unit = onTerminateStub,
    subscribeOnBackground: Boolean = true
): Disposable {
    return observeOn(MainThread).let {
        if (subscribeOnBackground) it.subscribeOn(Schedulers.io()) else it
    }.doAfterTerminate(onTerminate).subscribe(onSuccess, onError)
}

fun <T : Any> Subject<T>.observeOnMainThread(
    onNext: (T) -> Unit = onNextStub,
    onError: (Throwable) -> Unit = onErrorStub,
    onTerminate: () -> Unit = onTerminateStub,
    subscribeOnBackground: Boolean = true
): Disposable {
    return observeOn(MainThread).let {
        if (subscribeOnBackground) it.subscribeOn(Schedulers.io()) else it
    }.subscribe(onNext, onError, onTerminate)
}

fun <T> Single<T>.with(disposables: CompositeDisposable): Single<T> =
    doOnSubscribe { disposable -> disposables.add(disposable) }