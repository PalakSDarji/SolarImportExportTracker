package com.palak.solarimportexporttracker.home.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseUser
import com.palak.solarimportexporttracker.MyApplication
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.home.login.facebook.FacebookLoginViewModel
import com.palak.solarimportexporttracker.home.login.google.GoogleLoginViewModel
import com.palak.solarimportexporttracker.model.User
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

/**
 * Do firebase login and registration here.
 */
class LoginFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    var loginViewModel: LoginViewModel? = null

    private lateinit var navController: NavController
    private lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val appComponent = (requireActivity().application as MyApplication).appComponent
        appComponent.inject(this)
        userManager = appComponent.userManager()
        userManager.userComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        signInButton.setOnClickListener(this)
        fbLoginButton.setPermissions("email", "public_profile")
        signOutButton.setOnClickListener(this)
        disconnectButton.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.signInButton -> {
                loginViewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(GoogleLoginViewModel::class.java)

                loginViewModel?.apply {
                    initiate()
                    observeUser()
                    signIn { signInIntent ->
                        startActivityForResult(signInIntent, GoogleLoginViewModel.RC_SIGN_IN)
                    }
                }
            }
            R.id.fbLoginButton -> {
                loginViewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(FacebookLoginViewModel::class.java)

                loginViewModel?.apply {
                    this as FacebookLoginViewModel
                    initiate()
                    observeUser()
                    fbLoginButton.registerCallback(fbSignIn(), object : FacebookCallback<LoginResult>{
                        override fun onSuccess(result: LoginResult) {
                            getSignInDetail(result.accessToken) { task ->
                                if (task != null) {
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithCredential:success")
                                        val user = loginViewModel?.getCurrentUser()
                                        updateLoginStatus(user)
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                                        updateLoginStatus(null)
                                    }
                                }
                            }
                        }

                        override fun onCancel() {

                            updateLoginStatus(null)
                        }

                        override fun onError(error: FacebookException?) {

                            updateLoginStatus(null)
                        }
                    })
                }
            }
            R.id.signOutButton -> {
                loginViewModel?.signOut {
                    it.addOnCompleteListener(requireActivity()) {
                        updateLoginStatus(null)
                    }
                }
            }
            R.id.disconnectButton -> loginViewModel?.revokeAccess {
                it.addOnCompleteListener(requireActivity()) {
                    updateLoginStatus(null)
                }
            }
        }
    }

    private fun updateLoginStatus(user: FirebaseUser?){
        user?.let {
            //User is not null.
            userManager.userComponent?.inject(this)
            loginViewModel!!.syncUserToFirebaseDatabase(user)
        } ?: run {
            //user is null.
            loginViewModel!!.syncUserToFirebaseDatabase(null)
        }
    }

    private fun observeUser() {
        userManager?.let {
            it.currentUser.observe(viewLifecycleOwner, Observer {
                user ->
                updateUserUI(user)
            })
        }
    }

    private fun updateUserUI(user: User) {
        if (user != null) {

            Toast.makeText(requireContext(),"User: ${user.name}", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(),"User is signed out!", Toast.LENGTH_LONG).show()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GoogleLoginViewModel.RC_SIGN_IN) {
            loginViewModel?.apply {
                getSignInDetail(data) {
                        result ->
                    if(result != null){
                        result.addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = loginViewModel?.getCurrentUser()
                                updateLoginStatus(user)
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithCredential:failure", task.exception)
                                updateLoginStatus(null)
                            }
                        }
                    }
                    else{
                        updateLoginStatus(null)
                    }
                }
            }
        }
    }
}
