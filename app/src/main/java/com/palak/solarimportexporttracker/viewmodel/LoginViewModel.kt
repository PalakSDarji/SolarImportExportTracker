package com.palak.solarimportexporttracker.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

abstract class LoginViewModel(application: Application) : AndroidViewModel(application) {

    enum class LoginStatus {
        GOOGLE_LOGIN, FB_LOGIN, NO_LOGIN
    }

    open var status = LoginStatus.NO_LOGIN
    open var user : FirebaseUser? = null

    open fun initiate() {}
    open fun signIn(clickListener : (Intent) -> Unit) {}
    open fun getSignInDetail(data: Intent?, onCompleteLister : (Task<AuthResult>?) -> Unit) {}
    open fun getSignInDetail(token: AccessToken, onCompleteLister : (Task<AuthResult>?) -> Unit) {}
    open fun signOut(onCompleteLister : (Task<Void>) -> Unit) {}
    open fun revokeAccess(onCompleteLister : (Task<Void>) -> Unit) {}
    abstract fun getCurrentUser() : FirebaseUser?
}