package com.markhouston.reactiveredux

/**
 * Created by markhouston on 8/8/18.
 */

/**
 * Responsible for taking in the old state and an action to return the action with a new state object.
 */
abstract class Reducer {
    abstract fun reduce(state: HashMap<String, Any>, action: Action): Action
}