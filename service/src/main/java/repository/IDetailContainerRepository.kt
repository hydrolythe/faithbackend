package repository

import DetailsContainer
import User
import detail.Detail
import encryption.EncryptedDetail
import encryption.EncryptedDetailsContainer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface IDetailContainerRepository<T> {
    fun insertDetail(encryptedDetail: EncryptedDetail, uuid:String): Completable

    fun getEncryptedContainer(uuid:String): Single<EncryptedDetailsContainer>

    fun saveEncryptedContainer(encryptedDetailsContainer: EncryptedDetailsContainer, uuid:String): Completable

    fun getAll(uuid:String): Flowable<List<EncryptedDetail>>

    fun deleteDetail(detail: Detail,uuid:String): Completable
}