package be.hogent.faith.faith.service

import be.hogent.faith.faith.iservice.ITakePhotoService
import detail.PhotoDetail
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import org.springframework.stereotype.Service
import usecases.detail.photodetail.CreatePhotoDetailUseCase
import java.io.File

class TakePhotoService(
    private val createPhotoDetailUseCase: CreatePhotoDetailUseCase
):ITakePhotoService {

    private var currentDetail: PhotoDetail? = null

    override fun onSaveClicked(_tempPhotoSaveFile: File):PhotoDetail {
        val params = CreatePhotoDetailUseCase.Params(_tempPhotoSaveFile)
        createPhotoDetailUseCase.execute(params, object : DisposableSingleObserver<PhotoDetail>() {
            override fun onSuccess(createdDetail: PhotoDetail) {
                currentDetail = createdDetail
            }

            override fun onError(e: Throwable) {

            }
        })
        while(currentDetail==null){
            Thread.sleep(1000)
        }
        return currentDetail!!
    }
}