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
import com.google.firebase.database.DatabaseReference
import com.palak.solarimportexporttracker.MyApplication
import com.palak.solarimportexporttracker.di.UsersRef
import com.palak.solarimportexporttracker.home.login.LoginViewModel
import com.palak.solarimportexporttracker.home.login.UserManager
import javax.inject.Inject
import javax.inject.Named

class FacebookLoginViewModel @Inject constructor(val appContext : Application, @Named("UsersRef") var usersDataReff : DatabaseReference)
    : LoginViewModel(appContext, usersDataReff){

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    init {
        (appContext as MyApplication).appComponent.inject(this)
    }

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

        user = auth.currentUser
        userManager.status = if(user != null){
            UserManager.LoginStatus.FB_LOGIN
        } else{
            UserManager.LoginStatus.NO_LOGIN
        }

        return user
    }

    override fun getSignInDetail(token: AccessToken, onCompleteLister: (Task<AuthResult>?) -> Unit) {
        Log.d("TAG", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)

        onCompleteLister(auth.signInWithCredential(credential))
    }
}