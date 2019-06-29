package com.larvaesoft.opengst.utils

import android.content.Context
import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.model.Item
import android.content.pm.PackageManager
import com.larvaesoft.opengst.Constants
import org.jetbrains.anko.browse
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.share


class GSTHelper {
    companion object {
        val playStoreShareText = "Generated from OpenGST  https://goo.gl/GwgdtH"
        fun getImageId(item: Item): Int {
            var imageID = 0
            when (item.rate) {
                "0"->{
                    imageID =R.drawable.gst_0_percent
                }
                "3"->{
                    imageID = R.drawable.gst_3_percent
                }
                "5" -> {
                    imageID = R.drawable.gst_5_percent
                }
                "12" -> {
                    imageID = R.drawable.gst_12_percent
                }
                "18" -> {
                    imageID = R.drawable.gst_18_percent
                }
                "28" -> {
                    imageID = R.drawable.gst_28_percent
                }
                "0.25"->{
                    imageID = R.drawable.gst_025_percent
                }
            }
            return imageID
        }
        fun getHsn(hsn:String):String{
            return if(hsn.contains("heading",ignoreCase = true)){
                hsn
            }else{
                "HSN $hsn"
            }
        }
        private val fbUrl = "https://www.facebook.com/LarvaeSoftSolutions/"
        private val fbPageId = "945988228771149"
        fun getFacebookPageURL(context: Context): String {
            val packageManager = context.packageManager
            try {
                if (!packageManager.getPackageInfo("com.facebook.katana", 0).applicationInfo.enabled) {
                    return fbUrl
                }

                packageManager.getPackageInfo("com.facebook.katana", 0)
                return "fb://page/" + fbPageId
            } catch (e: PackageManager.NameNotFoundException) {
                return fbUrl
            }

        }

        fun startWebsite(activity: Context) {
            activity.browse("http://www.larvaesoft.com")
        }

        fun startTwitter(activity: Context) {
            activity.browse("https://twitter.com/LarvaeSoft/?utm_source=codejio&utm_medium=android&utm_campaign=OpenGST")
        }

        fun startShare(activity: Context) {
            activity.share("OpenGST "+Constants.playStoreURL)
        }

        fun startFacebook(activity: Context) {
            activity.browse(getFacebookPageURL(activity))
        }
        fun rateApp(activity: Context){
            activity.browse("market://details?id=com.larvaesoft.opengst&hl=en")
        }
    }

}