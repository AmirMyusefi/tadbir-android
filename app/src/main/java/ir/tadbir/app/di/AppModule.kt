package ir.tadbir.app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.tadbir.app.data.local.RoutineDao
import ir.tadbir.app.data.local.TadbirDatabase
import ir.tadbir.app.data.local.TaskDao
import ir.tadbir.app.data.local.TaskRepositoryImpl
import ir.tadbir.app.domain.repository.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TadbirDatabase {
        return Room.databaseBuilder(
            context,
            TadbirDatabase::class.java,
            "tadbir.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTaskDao(db: TadbirDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideRoutineDao(db: TadbirDatabase): RoutineDao = db.routineDao()

    @Provides
    @Singleton
    fun provideTaskRepository(impl: TaskRepositoryImpl): TaskRepository = impl
}
