package com.palak.solarimportexporttracker.home.login

import androidx.lifecycle.MutableLiveData
import com.palak.solarimportexporttracker.model.User
import com.palak.solarimportexporttracker.storage.Storage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(private val storage : Storage) {

    enum class LoginStatus {
        GOOGLE_LOGIN, FB_LOGIN, NO_LOGIN
    }

    open var status = LoginStatus.NO_LOGIN

    val currentUser : MutableLiveData<User>
        get() = internalCurrentUser

    private val internalCurrentUser : MutableLiveData<User> = MutableLiveData()

    init {
        updateUserFromStorage()
    }

    fun isUserLoggedIn() : Boolean {
        return internalCurrentUser.value != null
    }

    private fun updateUserFromStorage(){
        //Check for the user object in storage
        val user = storage.get("user",User::class.java)
        if(user != null) {
            status = LoginStatus.valueOf(storage.getString("loginStatus"))
            updateCurrentUser(user)
        }
    }

    fun updateCurrentUser(userObject: User?) {

        //update livedata value to update listener UI's
        internalCurrentUser.value = userObject
        //Store it to pref
        storage.put("user",userObject)
        storage.setString("loginStatus",status.name)
    }


}