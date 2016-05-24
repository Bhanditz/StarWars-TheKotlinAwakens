package com.hugomatilla.starwars.articles.detail

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.hugomatilla.starwars.R
import com.hugomatilla.starwars.base.toResourceString
import com.hugomatilla.starwars.domain.model.SectionDomain
import com.hugomatilla.starwars.widgets.ToolbarManager
import kotlinx.android.synthetic.main.article_detail_activity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class ArticleDetailActivity : Activity(), ArticleDetailPresenter.View, ToolbarManager {

    override val toolbar: Toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    val headerImage: String by lazy { intent.getStringExtra(HEADER_IMAGE) }

    companion object {
        val ID = "ArticleDetailActivity.ID"
        val HEADER_IMAGE = "ArticleDetailActivity.HEADER_IMAGE"
        val HEADER_TITLE = "ArticleDetailActivity.HEADER_TITLE"
        val TRANSITION_NAME_HEADER_TITLE = R.string.transition_name_header_image.toResourceString()

        fun getCallingIntent(context: Context, id: Int, title: String, thumbnail: String): Intent {
            val callingIntent = Intent(context, ArticleDetailActivity::class.java)
            callingIntent.putExtra(ID, id);
            callingIntent.putExtra(HEADER_TITLE, title);
            callingIntent.putExtra(HEADER_IMAGE, thumbnail);
            return callingIntent;
        }

        //        private fun getTransition(activity: Activity, view: View): ActivityOptionsCompat {
//            return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, android.support.v4.util.Pair(view, "toolbar"))
//        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun getTransition(activity: Activity, view: View): ActivityOptions {
            return ActivityOptions.makeSceneTransitionAnimation(activity, android.util.Pair(view, TRANSITION_NAME_HEADER_TITLE))
        }

        //Todo Use a builder instead
        fun start(activity: Activity, view: View, id: Int, title: String, thumbnail: String) {
            val intent = getCallingIntent(activity, id, title, thumbnail)
            val transitionBundle = getTransition(activity, view).toBundle()
            activity.startActivity(intent, transitionBundle)
        }
    }

    // Todo: add dependency injection or add it in the parameter
    private val presenter: ArticleDetailPresenter = ArticleDetailPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_detail_activity)
        toolbar.title = intent.getStringExtra(HEADER_TITLE)
        enableHomeAsUp { onBackPressed() }
        sectionsListView.layoutManager = LinearLayoutManager(this)
        val id = intent.getIntExtra(ID, 0)
        presenter.getDetailArticle(id)
        header.withImage(headerImage)
    }

    override fun showArticle(sections: Collection<SectionDomain>) {
        val sectionsWithHeader = addArticleHeaderInfo(sections)
        sectionsListView.adapter = ArticleDetailAdapter(sectionsWithHeader)
    }

    override fun showError(message: String) {
        alert(message) { positiveButton("OK") { } }.show()
    }

    override fun showEmptyCase() {
        toast("Sorry this article is empty. :)")
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.INVISIBLE
    }


    private fun addArticleHeaderInfo(sections: Collection<SectionDomain>): Collection<SectionDomain> {
//        val sectionsMutable = sections.toMutableList()
//        val headerSection = with(sections.elementAt(0)) { SectionDomain(title, level, text, headerImage, caption) }
//        sectionsMutable.removeAt(0)
//        sectionsMutable.add(0, headerSection)
//        return sectionsMutable.toList()
        return sections
    }
}
