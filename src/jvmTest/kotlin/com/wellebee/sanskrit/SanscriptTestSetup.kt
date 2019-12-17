package com.wellebee.sanskrit

import eu.karelhovorka.tools.Scheme
import eu.karelhovorka.tools.Schemes
import org.junit.Assert
import org.junit.Test
import java.util.*

class SanscriptTestSetup : SanscriptTest() {
    @Test
    fun testSchemeDefinitions() { // Find the typical lengths of each category. We use Devanagari because it
// contains every category, including "marks".
        val schemes: Schemes = sanscript.schemes
        val devanagari = schemes["devanagari"]
        val lengths: MutableMap<String, Int> = HashMap()
        for ((key, value) in devanagari!!) {
            lengths[key] = value.size
        }
        for ((name, scheme) in schemes) {
            for ((key, value) in scheme!!) {
                Assert.assertEquals("$name.$key", lengths[key], value.size)
            }
        }
    }

    @Test
    fun testRomanSchemeMembership() { // Find the typical lengths of each category. We use Devanagari because it
// contains every category, including "marks".
        val roman = arrayOf("iast", "itrans", "hk", "kolkata", "slp1", "velthuis", "wx")
        val other =
            arrayOf("bengali", "devanagari", "gujarati", "gurmukhi", "kannada", "malayalam", "oriya", "tamil", "telugu")
        for (name in roman) {
            Assert.assertTrue(name, sanscript.isRomanScheme(name))
        }
        for (name in other) {
            Assert.assertTrue(name, !sanscript.isRomanScheme(name))
        }
    }

    @Test
    fun testAddingSchemes() {
        val sanskritOCR: Scheme = mutableMapOf()
        sanskritOCR["vowels"] = arrayOf("a", "å", "i", "ï", "u", "÷", "Ÿ", "", "", "", "e", "ai", "o", "au")
        sanskritOCR["consonants"] = arrayOf(
            "k", "kh", "g", "gh", "¼",
            "c", "ch", "j", "jh", "ñ",
            "¶", "¶h", "·", "·h", "½",
            "t", "th", "d", "dh", "n",
            "p", "ph", "b", "bh", "m",
            "y", "r", "l", "v",
            "¸", "¹", "s", "h",
            "", "k¹", "jñ"
        )
        sanscript.addRomanScheme("sanskritOCR", sanskritOCR)
        val f: TransHelper = transHelper("sanskritOCR", "devanagari")
        f.run("bhïma", "भीम", "")
        f.run("narå½åm", "नराणाम्", "")
    }
}