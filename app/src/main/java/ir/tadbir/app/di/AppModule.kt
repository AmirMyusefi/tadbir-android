package ir.tadbir.app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.tadbir.app.data.local.BadHabitDao
import ir.tadbir.app.data.local.CountdownDao
import ir.tadbir.app.data.local.GoalDao
import ir.tadbir.app.data.local.InboxDao
import ir.tadbir.app.data.local.MoodDao
import ir.tadbir.app.data.local.ProjectDao
import ir.tadbir.app.data.local.RoutineDao
import ir.tadbir.app.data.local.TadbirDatabase
import ir.tadbir.app.data.local.TaskDao
import ir.tadbir.app.data.local.TaskRepositoryImpl
import ir.tadbir.app.data.local.GoalRepositoryImpl
import ir.tadbir.app.data.local.ProjectRepositoryImpl
import ir.tadbir.app.data.local.WellnessRepositoryImpl
import ir.tadbir.app.domain.repository.GoalRepository
import ir.tadbir.app.domain.repository.ProjectRepository
import ir.tadbir.app.domain.repository.TaskRepository
import ir.tadbir.app.domain.repository.WellnessRepository
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

    @Provides fun provideTaskDao(db: TadbirDatabase): TaskDao = db.taskDao()
    @Provides fun provideRoutineDao(db: TadbirDatabase): RoutineDao = db.routineDao()
    @Provides fun provideInboxDao(db: TadbirDatabase): InboxDao = db.inboxDao()
    @Provides fun provideGoalDao(db: TadbirDatabase): GoalDao = db.goalDao()
    @Provides fun provideProjectDao(db: TadbirDatabase): ProjectDao = db.projectDao()
    @Provides fun provideMoodDao(db: TadbirDatabase): MoodDao = db.moodDao()
    @Provides fun provideBadHabitDao(db: TadbirDatabase): BadHabitDao = db.badHabitDao()
    @Provides fun provideCountdownDao(db: TadbirDatabase): CountdownDao = db.countdownDao()

    @Provides
    @Singleton
    fun provideTaskRepository(impl: TaskRepositoryImpl): TaskRepository = impl

    @Provides
    @Singleton
    fun provideGoalRepository(impl: GoalRepositoryImpl): GoalRepository = impl

    @Provides
    @Singleton
    fun provideProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository = impl

    @Provides
    @Singleton
    fun provideWellnessRepository(impl: WellnessRepositoryImpl): WellnessRepository = impl
}
