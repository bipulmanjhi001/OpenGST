package com.larvaesoft.opengst.utils


import android.widget.TextView
import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.model.GstView
import com.larvaesoft.opengst.utils.operation.OperationFactory
import timber.log.Timber

class CalculatorImpl {
    private var displayedNumber: String? = null
    private var displayedFormula: String? = null
    private var mLastKey: String? = null
    private var mLastOperation: String? = null
    private var mCallback: Calculator? = null

    private var mIsFirstOperation: Boolean = false
    private var mResetValue: Boolean = false
    private var mBaseValue: Double = 0.toDouble()
    private var mSecondValue: Double = 0.toDouble()

    private fun getDisplayedNumberAsDouble(): Double {
        val n = Formatter.stringToDouble(displayedNumber)
        Timber.d("calc base is " + n)
        return n
    }

    constructor(calculator: Calculator) {
        mCallback = calculator
        resetValues()
        setValue("0")
        setFormula("")
    }

    constructor(calculatorInterface: Calculator, value: String) {
        mCallback = calculatorInterface
        resetValues()
        displayedNumber = value
        setFormula("")
    }

    private fun resetValueIfNeeded() {
        if (mResetValue)
            displayedNumber = "0"

        mResetValue = false
    }

    private fun resetValues() {
        mBaseValue = 0.0
        mSecondValue = 0.0
        mResetValue = false
        mLastKey = ""
        mLastOperation = ""
        displayedNumber = ""
        displayedFormula = ""
        mIsFirstOperation = true
    }

    private fun setValue(value: String) {
        mCallback!!.setValue(value)
        displayedNumber = value
    }

    private fun setFormula(value: String) {
        mCallback!!.setFormula(value)
        displayedFormula = value
    }

    private fun updateFormula() {
        val first = Formatter.doubleToString(mBaseValue)
        val second = Formatter.doubleToString(mSecondValue)
        val sign = getSign(mLastOperation)

        if (!sign.isEmpty()) {
            setFormula(first + sign + second)
        }
    }

    fun setLastKey(mLastKey: String) {
        this.mLastKey = mLastKey
    }

    private fun addDigit(number: Int) {
        val currentValue = displayedNumber
        if (currentValue!!.length > 11) {
            return
        }
        val newValue = formatString(currentValue + number)
        setValue(newValue)
    }

    private fun formatString(str: String): String {
        // if the number contains a decimal, do not try removing the leading zero anymore, nor add group separator
        // it would prevent writing values like 1.02
        if (str.contains("."))
            return str

        val doubleValue = Formatter.stringToDouble(str)
        return Formatter.doubleToString(doubleValue)
    }

    private fun updateResult(value: Double) {
        Timber.d("calculation result==== " + value)
        setValue(Formatter.doubleToString(value))
        mBaseValue = value
    }

    private fun handleResult() {
        mSecondValue = getDisplayedNumberAsDouble()
        calculateResult()
        mBaseValue = getDisplayedNumberAsDouble()
    }

    private fun calculateResult() {
        if (!mIsFirstOperation) {
            updateFormula()
        }

        val operation = OperationFactory.forId(mLastOperation, mBaseValue, mSecondValue)

        if (operation != null) {
            updateResult(operation.result)
        }

        mIsFirstOperation = false
    }

    fun handleOperation(operation: String) {
        if (mLastKey == CalcConstants.DIGIT)
            handleResult()

        mResetValue = true
        mLastKey = operation
        mLastOperation = operation
    }

    fun handleClear() {
        val oldValue = displayedNumber
        var newValue = "0"
        val len = oldValue!!.length
        var minLen = 1
        if (oldValue.contains("-"))
            minLen++

        if (len > minLen)
            newValue = oldValue.substring(0, len - 1)

        newValue = newValue.replace("\\.$".toRegex(), "")
        newValue = formatString(newValue)
        setValue(newValue)
        mBaseValue = Formatter.stringToDouble(newValue)
    }

    fun handleReset() {
        resetValues()
        setValue("0")
        setFormula("")
    }

    fun handleEquals() {
        if (mLastKey == CalcConstants.EQUALS)
            calculateResult()

        if (mLastKey != CalcConstants.DIGIT)
            return

        mSecondValue = getDisplayedNumberAsDouble()
        calculateResult()
        mLastKey = CalcConstants.EQUALS
    }

    private fun decimalClicked() {
        var value = displayedNumber
        if (!value!!.contains("."))
            value += "."
        setValue(value)
    }

    private fun zeroClicked() {
        val value = displayedNumber
        if (value != "0")
            addDigit(0)
    }

    private fun getSign(lastOperation: String?): String {
        when (lastOperation) {
            CalcConstants.PLUS -> return "+"
            CalcConstants.MINUS -> return "-"
            CalcConstants.MULTIPLY -> return "*"
            CalcConstants.DIVIDE -> return "/"
            CalcConstants.MODULO -> return "%"
            CalcConstants.POWER -> return "^"
            CalcConstants.ROOT -> return "√"
        }
        return ""
    }

    fun numpadClicked(id: Int) {
        if (mLastKey == CalcConstants.EQUALS)
            mLastOperation = CalcConstants.EQUALS
        mLastKey = CalcConstants.DIGIT
        resetValueIfNeeded()

        when (id) {
            R.id.btn_decimal -> decimalClicked()
            R.id.btn_0 -> zeroClicked()
            R.id.btn_1 -> addDigit(1)
            R.id.btn_2 -> addDigit(2)
            R.id.btn_3 -> addDigit(3)
            R.id.btn_4 -> addDigit(4)
            R.id.btn_5 -> addDigit(5)
            R.id.btn_6 -> addDigit(6)
            R.id.btn_7 -> addDigit(7)
            R.id.btn_8 -> addDigit(8)
            R.id.btn_9 -> addDigit(9)
            else -> {
            }
        }
    }

    fun handleGTSButtons(id: Int) {
        when (id) {
            R.id.gst_btn5 -> calculatePositiveGst(5.0)
            R.id.gst_btn12 -> calculatePositiveGst(12.0)
            R.id.gst_btn18 -> calculatePositiveGst(18.0)
            R.id.gst_btn28 -> calculatePositiveGst(28.0)

            R.id.gst_mbtn5 -> calculateNegativeGst(5.0)
            R.id.gst_mbtn12 -> calculateNegativeGst(12.0)
            R.id.gst_mbtn18 -> calculateNegativeGst(18.0)
            R.id.gst_mbtn28 -> calculateNegativeGst(28.0)
        }
    }

    private fun calculateNegativeGst(rate: Double) {
        if (!canCalculate(displayedNumber!!)) return
        val base = getDisplayedNumberAsDouble()
        Timber.d("base is " + base)
        if (base == 0.0) return
        val halfRate = getHalfRate(rate)
        val g = GstView()
        g.base = base
        g.cgstLabel = halfRate.toString()
        g.sgstLabel = halfRate.toString()
        g.gstLabel = rate.toString()
        //g.cgstValue = (calculateReversePercentage(base, halfRate) * -1)
        //g.sgstValue = g.cgstValue // both will remain same
        g.gstValue = (calculateReversePercentage(base, rate) * -1)
        g.cgstValue = getHalfRate(g.gstValue)
        g.sgstValue = g.cgstValue
        g.total = base + g.gstValue
        mCallback!!.showGSTDialog(g)
    }


    private fun canCalculate(num: String): Boolean {
        val number = Formatter.removeComma(num)
        if (number.length > 10) {
            mCallback!!.showMessage("Number too long for calculation")
            return false
        }
        return true
    }

    private fun calculatePositiveGst(rate: Double) {
        if (!canCalculate(displayedNumber!!)) return
        val base = getDisplayedNumberAsDouble()
        Timber.d("base is " + base)
        if (base == 0.0) return
        val halfRate = getHalfRate(rate)

        val g = GstView()
        g.base = base
        g.cgstLabel = halfRate.toString()
        g.sgstLabel = halfRate.toString()
        g.gstLabel = rate.toString()
        //g.cgstValue = calcPercentage(base, halfRate)
        //g.sgstValue = g.cgstValue // both will remain same
        g.gstValue = calcPercentage(base, rate)
        g.cgstValue = getHalfRate(g.gstValue)
        g.sgstValue = g.cgstValue
        g.total = g.gstValue + base
        mCallback!!.showGSTDialog(g)
    }

    private fun getHalfRate(rate: Double): Double {
        return rate / 2.0
    }

    private fun calculateReversePercentage(base: Double, percentage: Double): Double {
        //return base - (((100 + percentage) / 100) * base)
        return base - (base*(100/(100+percentage)))
    }

    private fun calcPercentage(base: Double, percentage: Double): Double {
        return (base * percentage) / 100
    }

}
