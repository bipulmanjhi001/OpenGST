package com.larvaesoft.opengst.ui.gstrates

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.formats.NativeAppInstallAd
import com.google.android.gms.ads.formats.NativeAppInstallAdView
import com.google.android.gms.ads.formats.NativeContentAd
import com.google.android.gms.ads.formats.NativeContentAdView
import com.larvaesoft.opengst.Constants
import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.model.AdItem
import com.larvaesoft.opengst.model.Item
import com.larvaesoft.opengst.model.MyItem
import com.larvaesoft.opengst.utils.GSTHelper
import kotlinx.android.synthetic.main.content_ad_layout.view.*
import kotlinx.android.synthetic.main.install_ad_layout.view.*
import kotlinx.android.synthetic.main.result_row.view.*
import timber.log.Timber

class ResultAdapter(private val list: List<MyItem>, private val presenter: GstRatePresenter, private val query: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val contentAd = 2
    private val installAd = 4
    private val dataItem = 3

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            contentAd -> {
                val vh = holder as AdViewHolder
                vh.bind(list[position] as AdItem)
            }
            installAd->{
                val vh = holder as InstallAdViewHolder
                vh.bind(list[position] as AdItem)
            }
            dataItem -> {
                val vh = holder as ResultViewHolder
                vh.bind(list[position] as Item)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].type) {
            Constants.installAd -> {
                installAd
            }
            Constants.contentAd -> {
                contentAd
            }
            else -> {
                dataItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            contentAd -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.content_ad_layout, parent, false)
                AdViewHolder(view)
            }
            installAd -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.install_ad_layout, parent, false)
                InstallAdViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.result_row, parent, false)
                ResultViewHolder(view)
            }
        }

    }

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val color = "#3949AB"
        fun bind(item: Item) {
            itemView.gst_type.text = item.type.toLowerCase().capitalize()
            itemView.hsn_no.text = GSTHelper.getHsn(item.hsn)
            itemView.description.text = Html.fromHtml(highlightWord(item.description))
            itemView.rate_image.setImageResource(GSTHelper.getImageId(item))
            itemView.setOnClickListener { presenter.showRateDetail(item) }
        }

        private fun highlightWord(desc: String): String {
            //find the word in the description and replace with a html tag which includes color
            val d = desc.replace(query!!, "<font color='$color'>$query</font>")
            return d
        }
    }

    inner class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: AdItem) {
            val ad = item.ad as NativeContentAd
            itemView.ad_headline.text = ad.headline
            itemView.ad_call_to_action.text = ad.callToAction
            if(ad.images.isNotEmpty()){
                itemView.ad_image.setImageDrawable(ad.images[0].drawable)
            }else{
                itemView.ad_image.visibility = View.GONE
            }
            itemView.ad_body.text = ad.body
            val adView = itemView as NativeContentAdView
            adView.headlineView = itemView.ad_headline
            adView.bodyView = itemView.ad_body
            adView.imageView = itemView.ad_image
            adView.callToActionView = itemView.ad_call_to_action
            adView.setNativeAd(ad)

        }

    }

    inner class InstallAdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: AdItem) {
            val ad = item.ad as NativeAppInstallAd
            itemView.app_ad_headline.text = ad.headline
            val adView = itemView as NativeAppInstallAdView
            adView.headlineView = itemView.app_ad_headline
            adView.setNativeAd(ad)
        }
    }

}
