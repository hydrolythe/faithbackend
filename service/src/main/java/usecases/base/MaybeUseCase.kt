package usecases.base

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableMaybeObserver
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class MaybeUseCase<Result, in Params>(
    private val observer: Scheduler,
    protected val subscriber: Scheduler = Schedulers.io()
) {
    private val disposables = CompositeDisposable()

    abstract fun buildUseCaseMaybe(params: Params): Maybe<Result>

    open fun execute(params: Params, singleObserver: DisposableMaybeObserver<Result>) {
        val single = this.buildUseCaseMaybe(params)
            .subscribeOn(subscriber)
            .observeOn(observer)
        addDisposable(single.subscribeWith(singleObserver))
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }
}
