package be.hogent.faith.faith.iservice

import DetailsContainer
import User
import detail.*

interface IDetailsContainerService<T:DetailsContainer>{
    fun loadDetails(uuid:String):T
    fun getCurrentDetailFile(uuid:String,detail:Detail):Detail
    fun saveDetail(uuid:String,detail:ExpandedDetail)
    fun deleteDetail(uuid:String,detail:Detail)
}