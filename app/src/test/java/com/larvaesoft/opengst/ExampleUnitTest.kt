package com.larvaesoft.opengst

import io.reactivex.rxkotlin.toMaybe
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun hightlightWord() {
        val color = "#555553"
        val desc = "fish in the sea"
        val query = "in"
        //find the word in the description and replace with a html tag which includes color
        val s = desc.replace("/$query/", "<i style='text-color=$color'>$query</i>")
        System.out.println(s)
    }

    @Test
    fun canShowNotification(){
        val currentDate: Calendar = Calendar.getInstance()
        currentDate.set(Calendar.DAY_OF_MONTH, 1)
        currentDate.set(Calendar.MONTH, 1)
        currentDate.set(Calendar.YEAR, 1990)

        val d1 = currentDate
        d1.set(Calendar.HOUR_OF_DAY, 8)
        d1.set(Calendar.MINUTE, 0)
        System.out.println(d1.time.toString())
        System.out.println(currentDate.time.toString())
    }
}
