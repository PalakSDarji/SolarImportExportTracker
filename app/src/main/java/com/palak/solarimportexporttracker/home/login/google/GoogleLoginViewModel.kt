package com.palak.solarimportexporttracker.home.login.google

import android.app.Application
import android.content.Intent
import android.util.Log
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
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.home.login.LoginViewModel

class GoogleLoginViewModel(private val appContext: Application) : LoginViewModel(appContext) {

    private lateinit var auth: FirebaseAuth
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
        auth.signOut()

        // Google sign out
        onCompleteLister(googleSignInClient.signOut())
        status = LoginStatus.NO_LOGIN
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
        onCompleteLister(auth.signInWithCredential(credential))
    }

    override fun getCurrentUser() : FirebaseUser? {

        user = auth.currentUser
        status = if(user != null){
            LoginStatus.GOOGLE_LOGIN
        } else{
            LoginStatus.NO_LOGIN
        }

        return user
    }

    override fun revokeAccess(onCompleteLister: (Task<Void>) -> Unit) {
        // Firebase sign out
        auth.signOut()
        // Google revoke access
        onCompleteLister(googleSignInClient.revokeAccess())
        status = LoginStatus.NO_LOGIN
    }

    companion object {
        private const val TAG = "GoogleActivity"
        const val RC_SIGN_IN = 9001
    }

}