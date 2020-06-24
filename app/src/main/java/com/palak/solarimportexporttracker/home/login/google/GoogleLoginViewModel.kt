package com.palak.solarimportexporttracker.home.login.google

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.palak.solarimportexporttracker.MyApplication
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.di.UsersRef
import com.palak.solarimportexporttracker.home.login.LoginViewModel
import com.palak.solarimportexporttracker.home.login.UserManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

class GoogleLoginViewModel @ViewModelInject constructor(val appContext: Application,
                                                        @UsersRef var usersDataReff : DatabaseReference, userManager: UserManager)
    : LoginViewModel(appContext, usersDataReff,userManager) {

    private var auth: FirebaseAuth? = null
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun initiate() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(appContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(appContext, gso)

        auth = FirebaseAuth.getInstance()
    }

    override fun signIn(clickListener : (Intent) -> Unit) {
        val signInIntent = googleSignInClient.signInIntent
        clickListener(signInIntent)
    }

    override fun signOut(onCompleteLister : (Task<Void>) -> Unit) {
        // Firebase sign out

        auth?.let{
            it.signOut()
            // Google sign out
            onCompleteLister(googleSignInClient.signOut())
        }
        userManager.status = UserManager.LoginStatus.NO_LOGIN
        userManager.currentUser.value = null
    }

    override fun getSignInDetail(data: Intent?, onCompleteLister: (Task<AuthResult>?) -> Unit) {

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!,onCompleteLister)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
            onCompleteLister(null)
        }
    }

    override fun getSignInDetail(
        token: AccessToken,
        onCompleteLister: (Task<AuthResult>?) -> Unit
    ) {

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount,onCompleteLister: (Task<AuthResult>) -> Unit) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        onCompleteLister(auth!!.signInWithCredential(credential))
    }

    override fun getCurrentUser() : FirebaseUser? {

        user = auth!!.currentUser
        userManager.status = if(user != null){
            UserManager.LoginStatus.GOOGLE_LOGIN
        } else{
            UserManager.LoginStatus.NO_LOGIN
        }

        return user
    }

    override fun revokeAccess(onCompleteLister: (Task<Void>) -> Unit) {
        // Firebase sign out
        auth?.let {
            it.signOut()
            // Google revoke access
            onCompleteLister(googleSignInClient.revokeAccess())
        }
        userManager.status = UserManager.LoginStatus.NO_LOGIN
    }

    companion object {
        private const val TAG = "GoogleActivity"
        const val RC_SIGN_IN = 9001
    }

}