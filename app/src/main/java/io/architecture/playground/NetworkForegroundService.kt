package io.architecture.playground

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.architecture.playground.core.pool.PoolManager
import io.architecture.playground.data.repository.interfaces.ConnectionStateRepository
import io.architecture.playground.domain.ObserveAndStoreRoutesUseCase
import io.architecture.playground.domain.ObserveAndStoreTracesUseCase
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

enum class Actions {
    START,
    STOP
}

enum class ServiceState {
    STARTED,
    STOPPED,
}

@AndroidEntryPoint
class NetworkForegroundService :
    LifecycleService() {   // TODO Extract notifications operations to Notifier class

    @Inject
    lateinit var observeAndStoreTraces: ObserveAndStoreTracesUseCase

    @Inject
    lateinit var observeAndStoreRoutes: ObserveAndStoreRoutesUseCase

    @Inject
    lateinit var connectionStateRepository: ConnectionStateRepository

    @Inject
    lateinit var poolManager: PoolManager
    private var supervisorJob = SupervisorJob(parent = null)

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null) {
            when (intent.action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> Log.d("TAG", "No action in the received intent")
            }
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent =
            Intent(applicationContext, NetworkForegroundService::class.java).also {
                it.setPackage(packageName)
            };
        val restartServicePendingIntent: PendingIntent =
            PendingIntent.getService(
                this, 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            );
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            restartServicePendingIntent
        );
    }

    @SuppressLint("WakelockTimeout")
    private fun startService() {
        if (isServiceStarted) return
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    "NetworkForegroundService::lock"
                ).apply {
                    acquire()
                }
            }

        val serviceJob = lifecycleScope.launch {
            poolManager.initialize()
            while (!poolManager.isInitialized) {
                delay(500)
            }
            launch {
                launch { observeAndStoreTraces() }
                observeAndStoreRoutes()
            }
        }
        supervisorJob[serviceJob.key]
        supervisorJob.cancel()
    }

    private fun stopService() {
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }

                else -> {
                    @Suppress("DEPRECATION")
                    stopForeground(true)
                }
            }
            stopSelf()
        } catch (e: Exception) {
            Log.d("SERVICE", "Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "NETWORK SERVICE CHANNEL"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Network Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Network Service channel"
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent =
            Intent(this, MapActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        val builder: NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationCompat.Builder(
                this,
                notificationChannelId
            ) else NotificationCompat.Builder(this, notificationChannelId)

        return builder
            .setContentTitle("Network Service")
            .setContentText("Keep alive websocket connection...")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Websocket connection")
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .build()
    }
}

private const val name = "SERVICE_KEY"
private const val key = "SERVICE_STATE"

fun setServiceState(context: Context, state: ServiceState) {
    val sharedPrefs = getPreferences(context)
    sharedPrefs.edit().let {
        it.putString(key, state.name)
        it.apply()
    }
}

fun getServiceState(context: Context): ServiceState {
    val sharedPrefs = getPreferences(context)
    val value = sharedPrefs.getString(key, ServiceState.STOPPED.name)
    return ServiceState.valueOf(value!!)
}

private fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(name, 0)
}