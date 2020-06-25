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
import com.palak.solarimportexporttracker.R
import com.palak.solarimportexporttracker.databinding.FragmentLoginBinding
import com.palak.solarimportexporttracker.home.login.facebook.FacebookLoginViewModel
import com.palak.solarimportexporttracker.home.login.google.GoogleLoginViewModel
import com.palak.solarimportexporttracker.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

/**
 * Do firebase login and registration here.
 */
@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentLoginBinding

    private var loginViewModel: LoginViewModel? = null

    @Inject
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(layoutInflater,container,false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInButton.setOnClickListener(this)
        fbLoginButton.setPermissions("email", "public_profile")
        signOutButton.setOnClickListener(this)
        disconnectButton.setOnClickListener(this)

        when(userManager.status){
            UserManager.LoginStatus.GOOGLE_LOGIN ->
                loginViewModel = ViewModelProvider(requireActivity()).get(GoogleLoginViewModel::class.java)
            UserManager.LoginStatus.FB_LOGIN ->
                loginViewModel = ViewModelProvider(requireActivity()).get(FacebookLoginViewModel::class.java)
        }
        observeUser()

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.signInButton -> {
                loginViewModel = ViewModelProvider(requireActivity()).get(GoogleLoginViewModel::class.java)

                loginViewModel?.apply {
                    initiate()
                    signIn { signInIntent ->
                        startActivityForResult(signInIntent, GoogleLoginViewModel.RC_SIGN_IN)
                    }
                }
            }
            R.id.fbLoginButton -> {
                loginViewModel = ViewModelProvider(requireActivity()).get(FacebookLoginViewModel::class.java)

                loginViewModel?.apply {
                    this as FacebookLoginViewModel
                    initiate()
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
            loginViewModel!!.syncUserToFirebaseDatabase(user)
        } ?: run {
            //user is null.
            loginViewModel!!.syncUserToFirebaseDatabase(null)
        }
    }

    private fun observeUser() {
        userManager.currentUser.observe(viewLifecycleOwner, Observer {
                user ->
            updateUserUI(user)
        })
    }

    private fun updateUserUI(user: User?) {
        binding.viewModel = loginViewModel
        if (user != null) {
            binding.userName = user.name
            Toast.makeText(requireContext(),"User: ${user.name}", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(),"User is signed out!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
