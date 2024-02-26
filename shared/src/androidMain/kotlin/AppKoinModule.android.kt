import io.architecture.core.di.coreKoinModules
import io.architecture.database.imp.room.di.roomDaoModule
import io.architecture.database.imp.room.di.roomDatabaseModule
import io.architecture.map.featureMapModule
import org.koin.dsl.module

actual val appModule = module {
    includes(
        coreKoinModules,
        roomDaoModule,
        roomDatabaseModule,
        featureMapModule
    )
}