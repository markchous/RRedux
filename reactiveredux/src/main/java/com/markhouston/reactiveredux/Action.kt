package com.markhouston.reactiveredux

/**
 * Created by markhouston on 8/23/18.
 */

/**
 * Holds the action type and data to be added to the new state object.
 */
data class Action(val actionType: String, var data: HashMap<String, Any>)
