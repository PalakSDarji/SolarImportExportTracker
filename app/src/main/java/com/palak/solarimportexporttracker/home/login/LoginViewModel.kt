package com.palak.solarimportexporttracker.home.login

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.palak.solarimportexporttracker.MyApplication
import com.palak.solarimportexporttracker.di.SolarDataRef
import com.palak.solarimportexporttracker.di.UsersRef
import com.palak.solarimportexporttracker.model.User
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

abstract class LoginViewModel(application: Application,
                              @UsersRef var usersDataRef : DatabaseReference,
                              var userManager : UserManager) : AndroidViewModel(application) {

    open var user : FirebaseUser? = null

    open fun initiate() {}
    open fun signIn(clickListener : (Intent) -> Unit) {}
    open fun getSignInDetail(data: Intent?, onCompleteLister : (Task<AuthResult>?) -> Unit) {}
    open fun getSignInDetail(token: AccessToken, onCompleteLister : (Task<AuthResult>?) -> Unit) {}
    open fun signOut(onCompleteLister : (Task<Void>) -> Unit) {}
    open fun revokeAccess(onCompleteLister : (Task<Void>) -> Unit) {}
    abstract fun getCurrentUser() : FirebaseUser?

    /**
     * Sync the user with firebase realtime database, uid as node. If already sync, it doesnt mind to sync twice.
     */
    fun syncUserToFirebaseDatabase(firebaseUser : FirebaseUser?){
        if(firebaseUser != null) {
            firebaseUser.apply {
                val userObject = User(uid,displayName,email,providerId)
                usersDataRef.child(uid).setValue(userObject)
                userManager.updateCurrentUser(userObject)

            }
        }
        else{
            user = null
            userManager.updateCurrentUser(null)
        }

    }
}