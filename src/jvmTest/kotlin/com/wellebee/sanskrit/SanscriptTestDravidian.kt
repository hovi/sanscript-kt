package com.wellebee.sanskrit

import org.junit.Test

class SanscriptTestDravidian : SanscriptTest() {
    fun dravidianTest(fromScript: String?, toScript: String?) {
        val f: TransHelper = transHelper(fromScript, toScript)
        val from: DataSet = dataSets.get(fromScript)!!
        val to: DataSet = dataSets.get(toScript)!!
        f.run(from.get("short_vowels"), to.get("short_vowels"), "Vowels (forward)")
        f.run(from.get("short_marks"), to.get("short_marks"), "Vowel marks (forward)")
    }

    @Test
    @Throws(Exception::class)
    fun testDravidianToKolkata() {
        dravidianTest("itrans_dravidian", "kolkata")
    }

    @Test
    @Throws(Exception::class)
    fun testDravidianToDevanagari() {
        dravidianTest("itrans_dravidian", "devanagari")
    }

    @Test
    @Throws(Exception::class)
    fun testDravidianToKannada() {
        dravidianTest("itrans_dravidian", "kannada")
    }

    @Test
    @Throws(Exception::class)
    fun testDravidianToMalayalam() {
        dravidianTest("itrans_dravidian", "malayalam")
    }

    @Throws(Exception::class)
    fun testDravidianToTamil() {
        dravidianTest("itrans_dravidian", "tamil")
    }

    @Test
    @Throws(Exception::class)
    fun testDravidianToTelugu() {
        dravidianTest("itrans_dravidian", "telugu")
    }

    @Test
    @Throws(Exception::class)
    fun testKolkataToDevanagari() {
        dravidianTest("kolkata", "devanagari")
    }
}