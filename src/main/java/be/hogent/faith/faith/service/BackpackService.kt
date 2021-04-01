package be.hogent.faith.faith.service

import Backpack
import be.hogent.faith.faith.DetailsContainerService
import usecases.detailscontainer.DeleteDetailsContainerDetailUseCase
import usecases.detailscontainer.GetDetailsContainerDataUseCase
import usecases.detailscontainer.LoadDetailFileUseCase
import usecases.detailscontainer.SaveDetailsContainerDetailUseCase

class BackpackService(
    saveBackpackDetailUseCase: SaveDetailsContainerDetailUseCase<Backpack>,
    deleteBackpackDetailUseCase: DeleteDetailsContainerDetailUseCase<Backpack>,
    backpack: Backpack,
    loadDetailFileUseCase: LoadDetailFileUseCase<Backpack>,
    getBackPackDataUseCase: GetDetailsContainerDataUseCase<Backpack>
) : DetailsContainerService<Backpack>(
    saveBackpackDetailUseCase,
    deleteBackpackDetailUseCase,
    loadDetailFileUseCase,
    getBackPackDataUseCase,
    backpack
)