package br.ronanlima.opiniaodetudo

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * Created by rlima on 08/10/19.
 */
class AppExecutors {

    var diskIO: Executor? = null
    var mainThread: Executor? = null
    var networkIO: Executor? = null

    companion object {
        val LOCK = Any()
        val threads = 3

        private var sInstance: AppExecutors? = null

        fun getInstance(): AppExecutors {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = AppExecutors(Executors.newSingleThreadExecutor(), MainThreadExecutor(), Executors.newFixedThreadPool(threads))
                }
            }
            return sInstance!!
        }
    }

    constructor(diskIo: Executor?, mainThread: Executor?, networkIo: Executor?) {
        this.diskIO = diskIo
        this.mainThread = mainThread
        this.networkIO = networkIo
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}