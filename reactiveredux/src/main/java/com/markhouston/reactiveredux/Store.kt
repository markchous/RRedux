package com.markhouston.reactiveredux

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by markhouston on 8/8/18.
 */
class Store {
    /**
     * Used to check if a new state is being created.
     */
    private var mIsDispatching = false

    /**
     * Stores immutable application state.
     * If store is dispatching an action, throw an exception.
     * The user shouldn't try to get state while the state is changing.
     */
    private var state: HashMap<String, Any> = HashMap()
        get() {
            if (mIsDispatching) {
                return field
            }
            return hashMapOf("ERROR" to Error("You cannot get state while the store is dispatching."))
        }

    /**
     * If I use a set then subjects cannot repeat.
     * The set would contain <String, Any> objects.
     * The key would be the class name.
     */
    private var mStateSubjects: HashMap<String, Any> = HashMap()

    init {
        sInstance = this
    }

    /**
     * Subscribe the view to the store.
     */
    fun subscribeToStore(className: String, completionHandler: (data: Action) -> Unit) {
        if (mStateSubjects.containsKey(className)) {
            Log.w("RRedux", "$className already is subscribed to the store.")
            return
        }
        val stateSubject: PublishSubject<Action> = PublishSubject.create()
        stateSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mIsDispatching = false
                    completionHandler(it)
                }
        mStateSubjects[className] = stateSubject
    }

    /**
     * Unsubscribe the view from the store.
     */
    fun unsubscribeFromStore(className: String) {
        if (mStateSubjects.containsKey(className)) {
            val subject = mStateSubjects[className] as PublishSubject<*>
            subject.onComplete()
            mStateSubjects.remove(className)
        }
    }

    /**
     * Send the action to the reducer.
     * Assign a new state object.
     */
    fun dispatch(className: String, actionType: String, vararg data: Any = arrayOf()) {
        mIsDispatching = true
        if (className.isEmpty()) {
            throw IllegalArgumentException("className must not be empty.")
        }
        if (actionType.isEmpty()) {
            throw IllegalArgumentException("actionType must not be empty.")
        }
        if (data.count() % 2 != 0) {
            throw IllegalArgumentException("Must provide a valid list of K, V pairs.")
        }
        val dataHashMap = HashMap<String, Any>()
        var i = 0
        while (i < data.size) {
            val key = data[i++] as String
            val value = data[i++]
            dataHashMap[key] = value
        }
        val action = Action(actionType, dataHashMap)
        state = mReducer.reduce(state, action).data
        action.data = state
        getSubject(className)?.onNext(action)
    }

    /**
     * Gets the subject from the HashMap.
     */
    @Suppress("UNCHECKED_CAST")
    private fun getSubject(className: String): PublishSubject<Action>? {
        if (mStateSubjects.containsKey(className)) {
            return mStateSubjects[className] as PublishSubject<Action>
        }
        return null
    }

    companion object {
        var sInstance: Store? = null
            get() = field?.let { it } ?: run { Store() }
            private set
        private lateinit var mReducer: Reducer

        fun createStore(reducer: Reducer): Store {
            mReducer = reducer
            return sInstance!!
        }
    }
}
