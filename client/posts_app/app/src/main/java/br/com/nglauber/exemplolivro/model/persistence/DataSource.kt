package br.com.nglauber.exemplolivro.model.persistence

import br.com.nglauber.exemplolivro.model.data.Post
import io.reactivex.Observable

interface PostDataSource {

    fun loadPosts() : Observable<Post>

    fun loadPost(postId : Long) : Observable<Post>

    fun savePost(post: Post) : Observable<Long>

    fun deletePost(post: Post) : Observable<Boolean>
}
