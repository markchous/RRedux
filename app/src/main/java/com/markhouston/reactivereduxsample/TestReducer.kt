package com.markhouston.reactivereduxsample

import android.util.Log
import com.markhouston.reactiveredux.Action
import com.markhouston.reactiveredux.Reducer
import com.markhouston.reactivereduxsample.Actions.TEST_ACTION
import com.markhouston.reactivereduxsample.Actions.TEST_ACTION_2
import com.markhouston.reactivereduxsample.keys.TEST_KEY
import com.markhouston.reactivereduxsample.keys.TEST_KEY_2

/**
 * Created by Mark.Houston on 8/24/2018
 */

class TestReducer : Reducer() {
    private val mInitialState = hashMapOf<String, Any>(TEST_KEY to "Change me!", TEST_KEY_2 to "Well, hello there!")

    override fun reduce(state: HashMap<String, Any>, action: Action): Action {
        Log.i("Tag", action.actionType)
        when(action.actionType) {
            TEST_ACTION -> {
                val newState = HashMap<String, Any>()
                newState.putAll(state)
                newState.putAll(action.data)
                return action.copy(data = newState)
            }
            TEST_ACTION_2 -> {

            }
        }
        return action.copy(data = mInitialState)
    }
}