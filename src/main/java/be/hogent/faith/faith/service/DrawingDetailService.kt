package be.hogent.faith.faith.service

import be.hogent.faith.faith.iservice.IDrawingDetailService
import detail.DrawingDetail
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import org.springframework.stereotype.Service
import usecases.detail.drawingdetail.CreateDrawingDetailUseCase
import usecases.detail.drawingdetail.OverwriteDrawingDetailUseCase
import java.awt.image.BufferedImage

class DrawingDetailService(
    private val createDrawingDetailUseCase: CreateDrawingDetailUseCase,
    private val overwriteDrawingDetailUseCase: OverwriteDrawingDetailUseCase
) : IDrawingDetailService {
    private var existingDetail: DrawingDetail? = null

    override fun loadExistingDetail(existingDetail: DrawingDetail) {
        this.existingDetail = existingDetail
        // The approach in the TextViewModel of fetching the existing text  using the UC
        // and setting it in the VM is not applicable here because we have to interact
        // directly with an Android-element (DrawView). Instead setting up the DrawView is done in the
        // [DrawingDetailFragment].
    }

    override fun onBitMapAvailable(bitmap: BufferedImage): DrawingDetail {
        existingDetail = null
        val params = CreateDrawingDetailUseCase.Params(bitmap)
        createDrawingDetailUseCase.execute(params, CreateDrawingDetailUseCaseHandler())
        while (existingDetail == null) {
            Thread.sleep(1000)
        }
        return existingDetail!!
    }

    private inner class OverwriteDrawingDetailUseCaseHandler :
        DisposableCompletableObserver() {
        override fun onComplete() {

        }

        override fun onError(e: Throwable) {

        }
    }

    private inner class CreateDrawingDetailUseCaseHandler :
        DisposableSingleObserver<DrawingDetail>() {
        override fun onSuccess(createdDetail: DrawingDetail) {
            existingDetail = createdDetail
        }

        override fun onError(e: Throwable) {

        }
    }
}