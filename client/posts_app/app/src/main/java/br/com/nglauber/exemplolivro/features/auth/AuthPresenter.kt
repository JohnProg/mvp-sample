package br.com.nglauber.exemplolivro.features.auth

import br.com.nglauber.exemplolivro.App
import br.com.nglauber.exemplolivro.model.auth.AccessManager
import timber.log.Timber
import javax.inject.Inject

class AuthPresenter : AuthContract.Presenter {

    private lateinit var view : AuthContract.View
    @Inject lateinit var accessManager: AccessManager

    init {
        App.component.inject(this)
    }

    override fun attachView(view: AuthContract.View) {
        this.view = view
    }

    override fun subscribe() {
        if (!isAuthenticated()) {
            view.logoutView()
            return
        }
        accessManager.addAccessChangedListener(mAuthListener)
    }

    override fun unsubscribe() {
        accessManager.removeAccessChangedListener(mAuthListener)
    }

    override fun performLogout() {
        AccessManager.instance.signOut()
    }

    override fun isAuthenticated(): Boolean {
        return accessManager.currentUser != null
    }

    private val mAuthListener = object : AccessManager.AccessChangedListener {
        override fun accessChanged(hasAccess: Boolean) {
            Timber.d("AuthPresenter::accessChanged(hasAccess= $hasAccess)")
            if (!hasAccess) {
                view.logoutView()
            }
        }
    }
}