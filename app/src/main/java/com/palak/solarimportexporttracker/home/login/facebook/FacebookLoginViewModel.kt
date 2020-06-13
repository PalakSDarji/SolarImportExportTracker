package com.palak.solarimportexporttracker.home.login.facebook

import android.app.Application
import android.content.Intent
import android.util.Log
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.palak.solarimportexporttracker.home.login.LoginViewModel

class FacebookLoginViewModel(private val appContext : Application) : LoginViewModel(appContext){

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun initiate() {

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

    }

    override fun signIn(clickListener: (Intent) -> Unit) {
        TODO("Not yet implemented")
    }

    fun fbSignIn() : CallbackManager {
        return callbackManager
    }

    override fun getSignInDetail(data: Intent?, onCompleteLister: (Task<AuthResult>?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun signOut(onCompleteLister: (Task<Void>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun revokeAccess(onCompleteLister: (Task<Void>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): FirebaseUser? {
        TODO("Not yet implemented")
    }

    override fun getSignInDetail(token: AccessToken, onCompleteLister: (Task<AuthResult>?) -> Unit) {
        Log.d("TAG", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)

        onCompleteLister(auth.signInWithCredential(credential))
    }
}