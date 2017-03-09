package br.com.nglauber.exemplolivro.features.postdetail

import android.databinding.DataBindingUtil.setContentView
import android.os.Bundle
import br.com.nglauber.exemplolivro.R
import br.com.nglauber.exemplolivro.features.postdetail.PostFragment
import br.com.nglauber.exemplolivro.shared.BaseActivity

class PostActivity : BaseActivity() {

    companion object {
        val EXTRA_ID = "postId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val id = intent?.getLongExtra(EXTRA_ID, -1)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.post_fragment_placeholder, PostFragment.newInstance(id ?: -1))
                    .commit()
        }
    }
}
