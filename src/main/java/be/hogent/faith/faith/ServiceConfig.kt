package be.hogent.faith.faith

import ActionEncryptionService
import authentication.AuthManager
import authentication.FirebaseAuthManager
import be.hogent.faith.faith.iservice.*
import be.hogent.faith.faith.service.*
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.schedulers.Schedulers
import org.springframework.context.annotation.Bean
import storage.TemporaryStorageRepository
import thumbnail.ThumbnailProvider
import usecases.detail.audiodetail.CreateAudioDetailUseCase
import usecases.detail.drawingdetail.CreateDrawingDetailUseCase
import usecases.detail.drawingdetail.OverwriteDrawingDetailUseCase
import usecases.detailscontainer.SaveDetailsContainerDetailUseCase
import usecases.user.LogoutUserUseCase
import Backpack
import DetailContainerEncryptionService
import DetailEncryptionService
import EventEncryptionService
import FileEncryptionService
import GoalEncryptionService
import SubGoalEncryptionService
import TreasureChest
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.context.annotation.Configuration
import user.UserMapper
import common.DetailMapper
import detailscontainer.*
import event.EventDatabase
import event.EventRepository
import goal.GoalDatabase
import goal.GoalRepository
import internal.KeyEncrypter
import internal.KeyEncryptionService
import internal.KeyGenerator
import org.springframework.context.annotation.Primary
import usecases.detail.photodetail.CreatePhotoDetailUseCase
import usecases.detail.textdetail.CreateTextDetailUseCase
import usecases.detail.textdetail.LoadTextDetailUseCase
import usecases.detail.textdetail.OverwriteTextDetailUseCase
import usecases.detailscontainer.DeleteDetailsContainerDetailUseCase
import usecases.detailscontainer.GetDetailsContainerDataUseCase
import usecases.detailscontainer.LoadDetailFileUseCase
import usecases.event.*
import usecases.goal.AddNewGoalUseCase
import usecases.goal.GetGoalsUseCase
import usecases.goal.SaveGoalUseCase
import usecases.goal.UpdateGoalUseCase
import usecases.user.GetUserUseCase
import usecases.user.InitialiseUserUseCase
import usecases.user.LoginUserUseCase
import user.FirebaseUserDatabase
import user.UserRepository
import java.io.FileInputStream
import java.lang.Exception

@Configuration
class ServiceConfig {

    @Bean
    fun audioDetailService(): IAudioDetailService? {
        return AudioDetailService(CreateAudioDetailUseCase(Schedulers.io()))
    }

    @Bean
    fun cityScreenService(): ICityScreenService? {
        return CityScreenService(
            LogoutUserUseCase(
                AuthManager(FirebaseAuthManager(FirebaseAuth.getInstance())),
                Schedulers.io()
            )
        )
    }

    @Bean
    fun backpackService(): IDetailsContainerService<Backpack> {
        return BackpackService(
            SaveDetailsContainerDetailUseCase(
                DetailContainerRepository(
                    UserMapper,
                    DetailMapper,
                    DetailContainerMapper,
                    BackpackDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ), DetailContainerEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    KeyEncrypter(KeyEncryptionService()),
                    KeyGenerator()
                ), Schedulers.io()
            ),
            DeleteDetailsContainerDetailUseCase(
                DetailContainerRepository(
                    UserMapper,
                    DetailMapper,
                    DetailContainerMapper,
                    BackpackDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ), Schedulers.io()
            ),
            Backpack(),
            LoadDetailFileUseCase(
                DetailContainerRepository(
                    UserMapper,
                    DetailMapper,
                    DetailContainerMapper,
                    BackpackDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ), DetailContainerEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    KeyEncrypter(KeyEncryptionService()),
                    KeyGenerator()
                ), Schedulers.io()
            ),
            GetDetailsContainerDataUseCase(
                DetailContainerRepository(
                    UserMapper,
                    DetailMapper,
                    DetailContainerMapper,
                    BackpackDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ), DetailContainerEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    KeyEncrypter(KeyEncryptionService()),
                    KeyGenerator()
                ), Schedulers.io()
            )
        )
    }

    @Bean
    fun drawingDetailService(): IDrawingDetailService? {
        return DrawingDetailService(
            CreateDrawingDetailUseCase(
                TemporaryStorageRepository(),
                ThumbnailProvider(),
                Schedulers.io()
            ), OverwriteDrawingDetailUseCase(TemporaryStorageRepository(), ThumbnailProvider(), Schedulers.io())
        )
    }

    @Bean
    fun eventService(): IEventService? {
        return EventService(
            SaveEmotionAvatarUseCase(TemporaryStorageRepository(), Schedulers.io()),
            SaveEventDetailUseCase(TemporaryStorageRepository(), Schedulers.io()),
            DeleteEventDetailUseCase(Schedulers.io())
        )
    }

    @Bean
    fun goalService(): IGoalService? {
        return GoalService(
            UpdateGoalUseCase(
                GoalEncryptionService(
                    KeyGenerator(),
                    KeyEncrypter(KeyEncryptionService()), SubGoalEncryptionService(ActionEncryptionService())
                ),
                GoalRepository(GoalDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())),
                Schedulers.io()
            )
        )
    }

    @Bean
    fun skyscraperOverviewService(): ISkyscraperOverviewService? {
        return SkyscraperOverviewService(
            GetGoalsUseCase(
                GoalRepository(GoalDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())),
                GoalEncryptionService(
                    KeyGenerator(),
                    KeyEncrypter(KeyEncryptionService()),
                    SubGoalEncryptionService(ActionEncryptionService())
                ), Schedulers.io()
            ), AddNewGoalUseCase(
                GoalEncryptionService(
                    KeyGenerator(),
                    KeyEncrypter(KeyEncryptionService()),
                    SubGoalEncryptionService(ActionEncryptionService())
                ), GoalRepository(GoalDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())),
                Schedulers.io()
            ), UpdateGoalUseCase(
                GoalEncryptionService(
                    KeyGenerator(),
                    KeyEncrypter(KeyEncryptionService()),
                    SubGoalEncryptionService(ActionEncryptionService())
                ), GoalRepository(GoalDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())),
                Schedulers.io()
            )
        )
    }

    @Bean
    fun takePhotoService(): ITakePhotoService {
        return TakePhotoService(CreatePhotoDetailUseCase(ThumbnailProvider(), Schedulers.io()))
    }

    @Bean
    fun textDetailService(): ITextDetailService {
        return TextDetailService(
            LoadTextDetailUseCase(TemporaryStorageRepository(), Schedulers.io()),
            CreateTextDetailUseCase(TemporaryStorageRepository(), Schedulers.io()),
            OverwriteTextDetailUseCase(TemporaryStorageRepository(), Schedulers.io())
        )
    }

    @Bean
    fun treasureChestService(): IDetailsContainerService<TreasureChest> {
        return TreasureChestService(
            SaveDetailsContainerDetailUseCase(
                DetailContainerRepository(
                    UserMapper,
                    DetailMapper,
                    DetailContainerMapper,
                    TreasureChestDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ), DetailContainerEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    KeyEncrypter(KeyEncryptionService()),
                    KeyGenerator()
                ), Schedulers.io()
            ),
            DeleteDetailsContainerDetailUseCase(
                DetailContainerRepository(
                    UserMapper,
                    DetailMapper,
                    DetailContainerMapper,
                    TreasureChestDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ), Schedulers.io()
            ),
            TreasureChest(),
            LoadDetailFileUseCase(
                DetailContainerRepository(
                    UserMapper,
                    DetailMapper,
                    DetailContainerMapper,
                    TreasureChestDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ), DetailContainerEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    KeyEncrypter(KeyEncryptionService()),
                    KeyGenerator()
                ), Schedulers.io()
            ),
            GetDetailsContainerDataUseCase(
                DetailContainerRepository(
                    UserMapper,
                    DetailMapper,
                    DetailContainerMapper,
                    TreasureChestDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ), DetailContainerEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    KeyEncrypter(KeyEncryptionService()),
                    KeyGenerator()
                ), Schedulers.io()
            )
        )
    }

    @Bean
    fun welcomeService(): IWelcomeService {
        return WelcomeService(
            LoginUserUseCase(
                AuthManager(FirebaseAuthManager(FirebaseAuth.getInstance())),
                Schedulers.io()
            )
        )
    }

    @Bean
    fun initialiseUser(): IRegisterAvatarService {
        return RegisterAvatarService(
            InitialiseUserUseCase(
                UserRepository(
                    UserMapper,
                    FirebaseUserDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ), DetailContainerRepository(
                    UserMapper, DetailMapper, DetailContainerMapper, BackpackDatabase(
                        FirebaseAuth.getInstance(), FirestoreClient.getFirestore()
                    )
                ), DetailContainerRepository(
                    UserMapper, DetailMapper, DetailContainerMapper, CinemaDatabase(
                        FirebaseAuth.getInstance(), FirestoreClient.getFirestore()
                    )
                ), DetailContainerRepository(
                    UserMapper, DetailMapper, DetailContainerMapper, TreasureChestDatabase(
                        FirebaseAuth.getInstance(), FirestoreClient.getFirestore()
                    )
                ), DetailContainerEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    KeyEncrypter(KeyEncryptionService()), KeyGenerator()
                ), DetailContainerEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    KeyEncrypter(KeyEncryptionService()), KeyGenerator()
                ), DetailContainerEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    KeyEncrypter(KeyEncryptionService()), KeyGenerator()
                ), Schedulers.io()
            )
        )
    }

    @Bean
    fun eventListService(): IEventListService {
        return EventListService(
            GetEventsUseCase(
                EventRepository(EventDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())),
                EventEncryptionService(
                    DetailEncryptionService(FileEncryptionService()), FileEncryptionService(),
                    KeyGenerator(), KeyEncrypter
                        (KeyEncryptionService())
                ),
                Schedulers.io()
            ),
            DeleteEventUseCase(
                EventRepository(
                    EventDatabase(
                        FirebaseAuth.getInstance(),
                        FirestoreClient.getFirestore()
                    )
                ), Schedulers.io()
            )
        )
    }

    @Bean
    fun userService(): IUserService {
        return UserService(
            SaveEventUseCase(
                EventEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    FileEncryptionService(),
                    KeyGenerator(),
                    KeyEncrypter(
                        KeyEncryptionService()
                    )
                ), EventRepository(EventDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())),
                ThumbnailProvider(), Schedulers.io
                    ()
            ),
            GetUserUseCase(
                UserRepository(
                    UserMapper,
                    FirebaseUserDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())
                ),
                EventRepository(EventDatabase(FirebaseAuth.getInstance(), FirestoreClient.getFirestore())),
                EventEncryptionService(
                    DetailEncryptionService(FileEncryptionService()),
                    FileEncryptionService(), KeyGenerator(), KeyEncrypter
                        (KeyEncryptionService())
                ),
                AuthManager(FirebaseAuthManager(FirebaseAuth.getInstance())),
                Schedulers.io()
            )
        )
    }
}