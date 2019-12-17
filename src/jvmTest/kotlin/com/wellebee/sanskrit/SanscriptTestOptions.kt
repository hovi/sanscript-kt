package com.wellebee.sanskrit

import eu.karelhovorka.tools.Sanscript
import org.junit.Test

class SanscriptTestOptions : SanscriptTest() {
    @Test
    fun testHindiStyleTransliteration() {
        val options: Sanscript.Options = Sanscript.Options(syncope = true)
        val f: TransHelper = transHelper("itrans", "devanagari", options)
        f.run("karaN", "करण", "")
        f.run("rAj ke lie", "राज के लिए", "")
    }

    @Test
    fun testSkippingSGML() {
        val options1: Sanscript.Options = Sanscript.Options(skipSgml = false)
        val options2: Sanscript.Options = Sanscript.Options(skipSgml = true)
        val f1: TransHelper = transHelper("hk", "devanagari")
        val f2: TransHelper = transHelper("hk", "devanagari", options1)
        val f3: TransHelper = transHelper("hk", "devanagari", options2)
        f1.run("<p>nara iti</p>", "<प्>नर इति</प्>", "")
        f2.run("<p>nara iti</p>", "<प्>नर इति</प्>", "")
        f3.run("<p>nara iti</p>", "<p>नर इति</p>", "")
        f3.run("##<p>nara iti</p>", "<p>nara iti</p>", "")
    }
}