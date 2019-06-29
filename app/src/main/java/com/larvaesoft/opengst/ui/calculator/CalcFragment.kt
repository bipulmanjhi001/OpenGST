package com.larvaesoft.opengst.ui.calculator


import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.model.GstView
import com.larvaesoft.opengst.utils.Calculator
import com.larvaesoft.opengst.utils.CalculatorImpl
import com.larvaesoft.opengst.utils.CalcConstants
import kotlinx.android.synthetic.main.fragment_calc.*
import kotlinx.android.synthetic.main.gst_calc_layout.view.*
import me.grantland.widget.AutofitHelper
import org.jetbrains.anko.toast
import android.net.Uri
import android.os.Environment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.jraska.falcon.Falcon
import com.larvaesoft.opengst.base.BaseFragment
import com.larvaesoft.opengst.utils.GSTHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.share
import java.io.File
import java.io.FileOutputStream
import java.util.*


class CalcFragment : BaseFragment(), Calculator {
    private var mCalc: CalculatorImpl? = null
    private lateinit var request:AdRequest

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_calc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AutofitHelper.create(result)
        AutofitHelper.create(formula)
        mCalc = CalculatorImpl(this)
        setupClickEvents()
        MobileAds.initialize(activity.applicationContext,getString(R.string.app_id))
        request = AdRequest.Builder()
                //.addTestDevice("8AF196582EF001C05AE6DAC416B8FB35")
                .build()
        adView.loadAd(request)

    }

    override fun setValue(value: String?) {
        result.text = value
    }

    override fun setValueDouble(d: Double) {
    }

    override fun setFormula(value: String?) {
        formula.text = value
    }


    private fun setupClickEvents() {
        btn_plus.setOnClickListener { mCalc!!.handleOperation(CalcConstants.PLUS) }
        btn_minus.setOnClickListener { mCalc!!.handleOperation(CalcConstants.MINUS) }
        btn_multiply.setOnClickListener { mCalc!!.handleOperation(CalcConstants.MULTIPLY) }
        btn_divide.setOnClickListener { mCalc!!.handleOperation(CalcConstants.DIVIDE) }
        btn_clear.setOnClickListener { mCalc!!.handleClear() }
        btn_clear.setOnLongClickListener {
            mCalc!!.handleReset()
            true
        }
        btn_equals.setOnClickListener { mCalc!!.handleEquals() }

        btn_1.setOnClickListener { v -> handleDigitClick(v) }
        btn_2.setOnClickListener { v -> handleDigitClick(v) }
        btn_3.setOnClickListener { v -> handleDigitClick(v) }
        btn_4.setOnClickListener { v -> handleDigitClick(v) }
        btn_5.setOnClickListener { v -> handleDigitClick(v) }
        btn_6.setOnClickListener { v -> handleDigitClick(v) }
        btn_7.setOnClickListener { v -> handleDigitClick(v) }
        btn_8.setOnClickListener { v -> handleDigitClick(v) }
        btn_9.setOnClickListener { v -> handleDigitClick(v) }
        btn_0.setOnClickListener { v -> handleDigitClick(v) }
        btn_decimal.setOnClickListener { v -> handleDigitClick(v) }

        gst_btn5.setOnClickListener { v -> handleGtsClick(v.id) }
        gst_btn12.setOnClickListener { v -> handleGtsClick(v.id) }
        gst_btn18.setOnClickListener { v -> handleGtsClick(v.id) }
        gst_btn28.setOnClickListener { v -> handleGtsClick(v.id) }

        gst_mbtn5.setOnClickListener { v -> handleGtsClick(v.id) }
        gst_mbtn12.setOnClickListener { v -> handleGtsClick(v.id) }
        gst_mbtn18.setOnClickListener { v -> handleGtsClick(v.id) }
        gst_mbtn28.setOnClickListener { v -> handleGtsClick(v.id) }
    }

    private fun handleGtsClick(id: Int) {
        mCalc!!.handleGTSButtons(id)
    }

    private fun handleDigitClick(v: View) {
        mCalc!!.numpadClicked(v.id)
    }

    private lateinit var currentCal: GstView

    override fun showGSTDialog(g: GstView) {
        currentCal = g
        val dialog = activity.layoutInflater.inflate(R.layout.gst_calc_layout, null)
        dialog.base.text = format(g.base)
        dialog.gst_label.text = g.gstLabel
        dialog.cgst_label.text = g.cgstLabel
        dialog.sgst_label.text = g.sgstLabel
        dialog.cgst_value.text = format(g.cgstValue)
        dialog.sgst_value.text = format(g.sgstValue)
        dialog.gst_value.text = format(g.gstValue)
        dialog.total_value.text = format(g.total)
        dialog.adViewRectangle.loadAd(request)
        dialog.screen_short_btn.setOnClickListener { checkPermissionForScreenShot() }
        dialog.copy_btn.setOnClickListener { copyCalculation() }
        dialog.share_btn.setOnClickListener { shareCalculation() }
        val builder = AlertDialog.Builder(activity)
        builder.setView(dialog)
        builder.create().show()
    }

    private fun createCalculationString(): String {
        if (currentCal == null) {
            return ""
        } else {
            var plus = " "
            if (currentCal.gstValue > 0) {
                plus = " + "
            }
            return "${format(currentCal.base)}$plus${format(currentCal.gstValue)} ${currentCal.gstLabel}  = ${format(currentCal.total)}\n" + GSTHelper.playStoreShareText
        }

    }

    private fun shareCalculation() {
        val calculation = createCalculationString()
        if (calculation.isNotEmpty()) {
            activity.share(calculation)
        }
    }

    private fun copyCalculation() {
        val calculationText = createCalculationString()
        if(calculationText.isNotEmpty()){
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("calc", calculationText)
            clipboard.primaryClip = clip
            showMessage("Copied")
        }
    }

    private val WRITE_REQUEST: Int = 21

    private fun checkPermissionForScreenShot() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), WRITE_REQUEST)
        } else {
            takeScreenShot()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == WRITE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeScreenShot()
            } else {
                showPermissionError()
            }
        }
    }

    private fun showPermissionError() {
        Snackbar.make(activity.main_layout, "Write permission is required for this feature", Snackbar.LENGTH_LONG)
                .setAction("Retry", { v -> checkPermissionForScreenShot() })
                .show()
    }

    private fun takeScreenShot() {
        val bitmap = Falcon.takeScreenshotBitmap(activity)
        val file = storeInFs(bitmap)
        if (file != null) {
            shareImageFile(file)
        } else {
            showImageSaveError()
        }
    }

    private fun shareImageFile(file: File) {
        val uri = Uri.fromFile(file)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, GSTHelper.playStoreShareText)
        try {
            startActivity(Intent.createChooser(intent, "Share screenshot"))
        } catch (e: ActivityNotFoundException) {
            showMessage("No app found to share")
        }
    }

    private fun storeInFs(bitmap: Bitmap?): File? {
        val path = Environment.getExternalStorageDirectory().absolutePath.toString() + "/OpenGST"
        val dir = File(path)
        if (!dir.exists()) dir.mkdir()
        val filename = "openGst_screenshot_" + Date().time.toString() + ".jpg"
        val bFile = File(dir, filename)
        try {
            val out = FileOutputStream(bFile)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 85, out)
            out.flush()
            out.close()
            return bFile
        } catch (ex: Exception) {
            showImageSaveError()
            return null
        }
    }

    private fun showImageSaveError() {
        showMessage("Unable to take screenshot")
    }

    override fun showMessage(s: String) {
        activity.toast(s)
    }

    private fun format(d: Double): String {
        return String.format("%.2f", d)
    }
}
