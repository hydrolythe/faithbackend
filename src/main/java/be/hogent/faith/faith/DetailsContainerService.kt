package be.hogent.faith.faith

import DetailsContainer
import User
import be.hogent.faith.faith.iservice.IDetailsContainerService
import detail.*
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import usecases.detailscontainer.DeleteDetailsContainerDetailUseCase
import usecases.detailscontainer.GetDetailsContainerDataUseCase
import usecases.detailscontainer.LoadDetailFileUseCase
import usecases.detailscontainer.SaveDetailsContainerDetailUseCase
import java.io.Console

abstract class DetailsContainerService<T : DetailsContainer>(
    private val saveDetailsContainerDetailUseCase: SaveDetailsContainerDetailUseCase<T>,
    private val deleteDetailsContainerDetailUseCase: DeleteDetailsContainerDetailUseCase<T>,
    private val loadDetailFileUseCase: LoadDetailFileUseCase<T>,
    private val getDetailsContainerDataUseCase: GetDetailsContainerDataUseCase<T>,
    val detailsContainer: T
) : IDetailsContainerService<T> {

    private var vlag = false

    override fun loadDetails(uuid: String): T {
        val params = GetDetailsContainerDataUseCase.Params(uuid)
        getDetailsContainerDataUseCase.execute(params, object : DisposableObserver<List<Detail>>() {
            override fun onNext(loadedDetails: List<Detail>) {
                detailsContainer.setDetails(loadedDetails)
            }

            override fun onComplete() {
                vlag = true
            }

            override fun onError(e: Throwable) {

            }
        })
        while (!vlag) {
            Thread.sleep(1000)
        }
        return detailsContainer
    }

    override fun getCurrentDetailFile(uuid: String, detail: Detail):Detail {
        val params = LoadDetailFileUseCase.Params(uuid, detail)
        loadDetailFileUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {
                vlag = true
            }

            override fun onError(e: Throwable) {

            }
        })
        while (!vlag) {
            Thread.sleep(1000)
        }
        return detail
    }

    override fun saveDetail(uuid: String, expandedDetail: ExpandedDetail) {
        when (expandedDetail.detailType) {
            DetailType.DRAWING -> saveDrawingDetail(
                uuid,
                DrawingDetail(
                    expandedDetail.file,
                    expandedDetail.thumbnail,
                    expandedDetail.title,
                    expandedDetail.uuid,
                    expandedDetail.dateTime
                )
            )
            DetailType.AUDIO -> saveAudioDetail(
                uuid,
                AudioDetail(expandedDetail.file, expandedDetail.title, expandedDetail.uuid, expandedDetail.dateTime)
            )
            DetailType.PHOTO -> savePhotoDetail(
                uuid,
                PhotoDetail(expandedDetail.file, expandedDetail.thumbnail, expandedDetail.title, expandedDetail.uuid)
            )
            DetailType.TEXT -> saveTextDetail(
                uuid,
                TextDetail(expandedDetail.file, expandedDetail.title, expandedDetail.uuid, expandedDetail.dateTime)
            )
            DetailType.VIDEO -> saveVideoDetail(
                uuid,
                VideoDetail(expandedDetail.file, expandedDetail.title, expandedDetail.uuid, expandedDetail.dateTime)
            )
            DetailType.YOUTUBE -> saveYoutubeDetail(
                uuid,
                YoutubeVideoDetail(expandedDetail.file, expandedDetail.title, expandedDetail.uuid, expandedDetail.title)
            )
        }
    }

    fun saveTextDetail(user: String, detail: TextDetail) {
        val params = SaveDetailsContainerDetailUseCase.Params(user, detailsContainer, detail)
        saveDetailsContainerDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {
                vlag = true
            }

            override fun onError(e: Throwable) {
                vlag = true
            }
        })
        while (!vlag) {
            Thread.sleep(1000)
        }
    }

    fun saveAudioDetail(user: String, detail: AudioDetail) {
        val params = SaveDetailsContainerDetailUseCase.Params(user, detailsContainer, detail)
        saveDetailsContainerDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {
                vlag = true
            }

            override fun onError(e: Throwable) {
                vlag = true
            }
        })
        while (!vlag) {
            Thread.sleep(1000)
        }
    }

    fun savePhotoDetail(user: String, detail: PhotoDetail) {
        val params = SaveDetailsContainerDetailUseCase.Params(user, detailsContainer, detail)
        saveDetailsContainerDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {
                vlag = true
            }

            override fun onError(e: Throwable) {
                vlag = true
            }
        })
        while (!vlag) {
            Thread.sleep(1000)
        }
    }

    fun saveDrawingDetail(user: String, detail: DrawingDetail) {
        val params = SaveDetailsContainerDetailUseCase.Params(user, detailsContainer, detail)
        saveDetailsContainerDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {
                vlag = true
            }

            override fun onError(e: Throwable) {
                println(e)
                vlag = true
            }
        })
        while (!vlag) {
            Thread.sleep(1000)
        }
    }

    fun saveVideoDetail(user: String, detail: VideoDetail) {
        val params = SaveDetailsContainerDetailUseCase.Params(user, detailsContainer, detail)
        saveDetailsContainerDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {
                vlag = true
            }

            override fun onError(e: Throwable) {
                vlag = true
            }
        })
        while (!vlag) {
            Thread.sleep(1000)
        }
    }

    fun saveYoutubeDetail(user: String, detail: YoutubeVideoDetail) {
        val params = SaveDetailsContainerDetailUseCase.Params(user, detailsContainer, detail)
        saveDetailsContainerDetailUseCase.execute(params, object : DisposableCompletableObserver() {
            override fun onComplete() {
                vlag = true
            }

            override fun onError(e: Throwable) {
                vlag = true
            }
        })
        while (!vlag) {
            Thread.sleep(1000)
        }
    }

    override fun deleteDetail(user: String, detail: Detail) {
        val params = DeleteDetailsContainerDetailUseCase.Params(user, detail, detailsContainer)
        deleteDetailsContainerDetailUseCase.execute(params, DeleteDetailUseCaseHandler())
        while (!vlag) {
            Thread.sleep(1000)
        }
    }

    private inner class DeleteDetailUseCaseHandler : DisposableCompletableObserver() {
        override fun onComplete() {
            vlag = true
        }

        override fun onError(e: Throwable) {

        }
    }
}