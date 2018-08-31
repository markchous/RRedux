package com.markhouston.reactivereduxsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.markhouston.reactiveredux.Store
import com.markhouston.reactivereduxsample.Actions.TEST_ACTION_2
import com.markhouston.reactivereduxsample.keys.TEST_KEY_2
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    private lateinit var mStore: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mStore = (application as App).getStore()
        mStore.subscribeToStore(TAG) {
            Log.i("RRedux", it.actionType)
            if (it.actionType == Actions.TEST_ACTION_2 && it.data.containsKey(keys.TEST_KEY_2)) {
                Log.i("RRedux", "${it.data[keys.TEST_KEY_2]}")
            }
        }

        myButton2.setOnClickListener {
            mStore.dispatch(TAG, TEST_ACTION_2, TEST_KEY_2, "Ben")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mStore.unsubscribeFromStore(TAG)
    }

    companion object {
        val TAG = Main2Activity::class.java.simpleName!!
    }

}
