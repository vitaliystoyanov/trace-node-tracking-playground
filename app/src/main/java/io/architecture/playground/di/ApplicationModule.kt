package io.architecture.playground.di

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideActivityManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    @Provides
    fun provideLifeCycle(application: Application) =
        AndroidLifecycle.ofApplicationForeground(application)
}