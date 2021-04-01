package be.hogent.faith.faith.service

import be.hogent.faith.faith.DetailsContainerService
import TreasureChest
import User
import org.springframework.stereotype.Service
import usecases.detailscontainer.DeleteDetailsContainerDetailUseCase
import usecases.detailscontainer.GetDetailsContainerDataUseCase
import usecases.detailscontainer.LoadDetailFileUseCase
import usecases.detailscontainer.SaveDetailsContainerDetailUseCase

class TreasureChestService(
    saveDetailUseCase: SaveDetailsContainerDetailUseCase<TreasureChest>,
    deleteDetailUseCase: DeleteDetailsContainerDetailUseCase<TreasureChest>,
    treasureChest: TreasureChest,
    loadDetailFileUseCase: LoadDetailFileUseCase<TreasureChest>,
    getDataUseCase: GetDetailsContainerDataUseCase<TreasureChest>
) : DetailsContainerService<TreasureChest>(
    saveDetailUseCase,
    deleteDetailUseCase,
    loadDetailFileUseCase,
    getDataUseCase,
    treasureChest
)