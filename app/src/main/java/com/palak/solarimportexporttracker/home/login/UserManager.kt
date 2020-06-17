package com.palak.solarimportexporttracker.home.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.palak.solarimportexporttracker.di.UsersRef
import com.palak.solarimportexporttracker.home.login.di.UserComponent
import com.palak.solarimportexporttracker.model.User
import com.palak.solarimportexporttracker.storage.Storage
import javax.inject.Inject

class UserManager @Inject constructor(private val storage : Storage,
    private val userComponentFactory: UserComponent.Factory) {

    enum class LoginStatus {
        GOOGLE_LOGIN, FB_LOGIN, NO_LOGIN
    }

    open var status = LoginStatus.NO_LOGIN

    val currentUser : MutableLiveData<User>
        get() = internalCurrentUser

    private val internalCurrentUser : MutableLiveData<User> = MutableLiveData()


    var userComponent : UserComponent? = null
        private set

    fun isUserLoggedIn() : Boolean {
        return if(userComponent != null){
            true
        } else{
            //Check for the user object in storage
            val user = storage.get("user",User::class.java)
            if(user != null){
                status = LoginStatus.valueOf(storage.getString("loginStatus"))
                updateCurrentUser(user)
                true
            } else{
                false
            }
        }
    }

    fun updateCurrentUser(userObject: User?) {

        if(userObject != null){
            userComponent = userComponentFactory.create()
        }
        else{
            userComponent = null
        }

        //update livedata value to update listener UI's
        internalCurrentUser.value = userObject
        //Store it to pref
        storage.put("user",userObject)
        storage.setString("loginStatus",status.name)
    }


}