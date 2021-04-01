package be.hogent.faith.faith.iservice

import User

interface ICityScreenService {
    fun logout(uuid:String)
    fun onCleared()
}