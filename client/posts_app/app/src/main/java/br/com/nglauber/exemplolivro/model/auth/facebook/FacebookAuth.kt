package br.com.nglauber.exemplolivro.model.auth.facebook

import android.content.Intent
import android.support.v4.app.FragmentActivity
import br.com.nglauber.exemplolivro.model.auth.Authentication
import br.com.nglauber.exemplolivro.model.auth.OnAuthRequestedListener
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import java.util.*

class FacebookAuth(private val mActivity: FragmentActivity) : Authentication {
    private lateinit var authListener: OnAuthRequestedListener
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun startAuthProcess(l: OnAuthRequestedListener) {
        authListener = l
        val lm = LoginManager.getInstance()
        lm.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Timber.d("facebook:onSuccess:" + loginResult)
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Timber.d("facebook:onAuthCancel")
                authListener.onAuthCancel()
            }

            override fun onError(error: FacebookException) {
                Timber.d("facebook:onError", error)
                authListener.onAuthError()
            }
        })
        lm.logInWithReadPermissions(mActivity, Arrays.asList("email", "public_profile"))
    }

    override fun handleAuthResponse(requestCode: Int, resultCode: Int, data: Any) {
        callbackManager.onActivityResult(requestCode, resultCode, data as Intent)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Timber.d("handleFacebookAccessToken:" + token)

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity) { task ->
                    Timber.d("signInWithCredential:onComplete:" + task.isSuccessful)

                    if (task.isSuccessful) {
                        authListener.onAuthSuccess()

                    } else {
                        Timber.d( "signInWithCredential", task.exception)
                        authListener.onAuthError()
                    }
                }
    }
}
