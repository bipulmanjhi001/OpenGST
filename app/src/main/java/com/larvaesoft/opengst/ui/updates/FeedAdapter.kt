package com.larvaesoft.opengst.ui.updates

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.model.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_login.view.*
import kotlinx.android.synthetic.main.feed_layout.view.*
import timber.log.Timber

class FeedAdapter(private val list: MutableList<Article>, private val presenter: GupdatePresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_feed_layout, parent, false)
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val vh = holder as FeedViewHolder
        vh.bind(list[position])
    }

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(article: Article) {
            Timber.d(article.toString())
            if (article.image.isNotBlank()) {
                itemView.rss_image.visibility = View.VISIBLE
                Picasso.with(presenter.getContext()).load(article.image).into(itemView.rss_image)
            }else{
                itemView.rss_image.visibility = View.GONE
            }
            itemView.rss_title.text = article.title
            itemView.rss_source.text = article.source
            itemView.setOnClickListener {
                presenter.openSource(article.link)
            }
        }
    }
}