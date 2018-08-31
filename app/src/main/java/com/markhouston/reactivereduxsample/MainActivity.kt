package com.markhouston.reactivereduxsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.markhouston.reactiveredux.Store
import com.markhouston.reactivereduxsample.Actions.TEST_ACTION
import com.markhouston.reactivereduxsample.keys.TEST_KEY
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mStore: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mStore = (application as App).getStore()
        mStore.subscribeToStore(TAG) {
            if (it.actionType == Actions.TEST_ACTION && it.data.containsKey(TEST_KEY)) {
                val myText = it.data[TEST_KEY] as String
                myTextView.text = myText
            }
        }
        mStore.dispatch(TAG, TEST_ACTION, TEST_KEY, "Change me!")

        myButton.setOnClickListener {
            val data = arrayOf(TEST_KEY, "MARK")
            mStore.dispatch(TAG, TEST_ACTION, *data)
        }

        myButtonNext.setOnClickListener {
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        Log.i("Tag", "DESTROY")
        super.onDestroy()
        mStore.unsubscribeFromStore(TAG)
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName!!
    }
}
