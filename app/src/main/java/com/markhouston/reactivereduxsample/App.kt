package com.markhouston.reactivereduxsample

import android.app.Application
import com.markhouston.reactiveredux.Store

/**
 * Created by markhouston on 8/23/18.
 */

interface StoreInterface {
    fun getStore(): Store
}

class App: Application(), StoreInterface {
    private lateinit var mStore: Store

    override fun onCreate() {
        super.onCreate()
        mStore = Store.createStore(TestReducer())
    }

    override fun getStore(): Store {
        return mStore
    }
}