package be.hogent.faith.faith.service

import be.hogent.faith.faith.iservice.ITextDetailService
import detail.TextDetail
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import org.springframework.stereotype.Service
import usecases.detail.textdetail.CreateTextDetailUseCase
import usecases.detail.textdetail.LoadTextDetailUseCase
import usecases.detail.textdetail.OverwriteTextDetailUseCase

class TextDetailService(
    private val loadTextDetailUseCase: LoadTextDetailUseCase,
    private val createTextDetailUseCase: CreateTextDetailUseCase,
    private val overwriteTextDetailUseCase: OverwriteTextDetailUseCase
) : ITextDetailService {
    private var _text: String = ""
    private var _existingDetail: TextDetail? = null
    private var _loadedString: String = ""

    override fun insertText(text: String) {
        _text = text
    }

    override fun loadExistingDetail(existingDetail: TextDetail): String {
        _existingDetail = existingDetail
        val params = LoadTextDetailUseCase.LoadTextParams(existingDetail)
        loadTextDetailUseCase.execute(params, LoadTextUseCaseHandler())
        while (_loadedString.isEmpty()) {
            Thread.sleep(1000)
        }
        return _loadedString
    }

    override fun onSaveClicked(): TextDetail {
        if (_text.isNotEmpty()) {
            _existingDetail = null
            val params = CreateTextDetailUseCase.Params(_text)
            createTextDetailUseCase.execute(params, CreateTextDetailUseCaseHandler())
        }
        while (_existingDetail == null) {
            Thread.sleep(1000)
        }
        return _existingDetail!!
    }

    private inner class OverwriteTextDetailUseCaseHandler : DisposableCompletableObserver() {
        override fun onComplete() {

        }

        override fun onError(e: Throwable) {

        }
    }

    private inner class CreateTextDetailUseCaseHandler : DisposableSingleObserver<TextDetail>() {
        override fun onSuccess(createdDetail: TextDetail) {
            _existingDetail = createdDetail
        }

        override fun onError(e: Throwable) {

        }
    }

    private inner class LoadTextUseCaseHandler : DisposableSingleObserver<String>() {
        override fun onSuccess(loadedString: String) {
            _loadedString = loadedString
        }

        override fun onError(e: Throwable) {

        }
    }
}