package com.wellebee.sanskrit

import org.junit.Test

class SanscriptTestTransliteration : SanscriptTest() {
    /* Letter transliteration tests
     * ----------------------------
     * Basic checks on letters and symbols.
     *
     * @param from  the source data
     * @param to    the destination data
     * @param f     the function to use
     */
    protected fun letterTests(from: DataSet, to: DataSet, f: TransHelper) {
        f.run(from.get("vowels"), to.get("vowels"), "Vowels")
        f.run(from.get("marks"), to.get("marks"), "Marks")
        f.run(from.get("consonants"), to.get("consonants"), "Stops and nasals")
        f.run(from.get("other"), to.get("other"), "Other consonants")
        f.run(from.get("symbols"), to.get("symbols"), "Symbols and punctuation")
    }

    /* Text transliteration tests
     * --------------------------
     * Basic checks on words and sentences.
     *
     * @param from  the source data
     * @param to    the destination data
     * @param f     the void to use
     */
    protected fun textTests(from: DataSet, to: DataSet, f: TransHelper) {
        f.run(from.get("putra"), to.get("putra"), "Single word")
        f.run(from.get("naraIti"), to.get("naraIti"), "Two words, one with explicit vowel")
        f.run(from.get("sentence"), to.get("sentence"), "Basic sentence")
    }

    @Test
    fun testDevanagariToBengali() {
        val from: DataSet = dataSets.get("devanagari")!!
        val to: DataSet = dataSets.get("bengali")!!
        val f: TransHelper = transHelper("devanagari", "bengali")
        letterTests(from, to, f)
        textTests(from, to, f)
        f.run("व", "ব", "व transliteration")
        f.run("ब", "ব", "ब transliteration")
    }

    @Test
    fun testDevanagariToHarvardKyoto() {
        val from: DataSet = dataSets.get("devanagari")!!
        val to: DataSet = dataSets.get("hk")!!
        val f: TransHelper = transHelper("devanagari", "hk")
        letterTests(from, to, f)
        textTests(from, to, f)
        // Other
        f.run("wwॠww", "wwRRww", "Vowel among other letters")
        f.run("wwकww", "wwkaww", "Consonant among other letters")
    }

    @Test
    fun testDevanagariToGujarati() {
        val from: DataSet = dataSets.get("devanagari")!!
        val to: DataSet = dataSets.get("gujarati")!!
        val f: TransHelper = transHelper("devanagari", "gujarati")
        letterTests(from, to, f)
        textTests(from, to, f)
    }

    @Test
    fun testDevanagariToGurmukhi() {
        val from: DataSet = dataSets.get("devanagari")!!
        val to: DataSet = dataSets.get("gurmukhi")!!
        val f: TransHelper = transHelper("devanagari", "gurmukhi")
        f.run("अ आ इ ई उ ऊ ए ऐ ओ औ", to.get("vowels"), "Vowels") // no ऋ/ॠ/ऌ/ॡ
        f.run("क खा गि घी ङु चू टे ठै डो ढौ णं तः थ्", to.get("marks"), "Marks") // no ऋ/ॠ/ऌ/ॡ
        f.run(from.get("consonants"), to.get("consonants"), "Stops and nasals")
        f.run(from.get("other"), to.get("other"), "Other consonants")
        f.run(from.get("symbols"), to.get("symbols"), "Symbols and punctuation")
        textTests(from, to, f)
    }

    @Test
    fun testDevanagariToKannada() { // Letters
        val from: DataSet = dataSets.get("devanagari")!!
        val to: DataSet = dataSets.get("kannada")!!
        val f: TransHelper = transHelper("devanagari", "kannada")
        letterTests(from, to, f)
        textTests(from, to, f)
    }

    @Test
    fun testDevanagariToMalayalam() {
        val from: DataSet = dataSets.get("devanagari")!!
        val to: DataSet = dataSets.get("malayalam")!!
        val f: TransHelper = transHelper("devanagari", "malayalam")
        letterTests(from, to, f)
        textTests(from, to, f)
    }

    @Test
    fun testDevanagariToOriya() {
        val from: DataSet = dataSets.get("devanagari")!!
        val to: DataSet = dataSets.get("oriya")!!
        val f: TransHelper = transHelper("devanagari", "oriya")
        f.run(from.get("vowels"), to.get("vowels"), "Vowels")
        f.run("क खा गि घी ङु चू छृ जॄ टे ठै डो ढौ णं तः थ्", to.get("marks"), "Marks") // no ऌ or ॡ
        f.run(from.get("consonants"), to.get("consonants"), "Stops and nasals")
        f.run(from.get("other"), to.get("other"), "Other consonants")
        f.run(from.get("symbols"), to.get("symbols"), "Symbols and punctuation")
        textTests(from, to, f)
        textTests(from, to, f)
    }

    @Test
    fun testDevanagariToTelugu() {
        val from: DataSet = dataSets.get("devanagari")!!
        val to: DataSet = dataSets.get("telugu")!!
        val f: TransHelper = transHelper("devanagari", "telugu")
        letterTests(from, to, f)
        textTests(from, to, f)
    }

    @Test
    fun testHarvardKyotoToDevanagari() {
        val from: DataSet = dataSets.get("hk")!!
        val to: DataSet = dataSets.get("devanagari")!!
        val f: TransHelper = transHelper("hk", "devanagari")
        letterTests(from, to, f)
        textTests(from, to, f)
        f.run("naraxiti", "नरxइति", "Undefined letters")
    }

    @Test
    fun testHarvardKyotoToIAST() {
        val from: DataSet = dataSets.get("hk")!!
        val to: DataSet = dataSets.get("iast")!!
        val f: TransHelper = transHelper("hk", "iast")
        letterTests(from, to, f)
        textTests(from, to, f)
        f.run("tAmxiti", "tāmxiti", "Undefined letters")
    }

    @Test
    fun testITRANSToDevanagari() {
        val from: DataSet = dataSets.get("itrans")!!
        val to: DataSet = dataSets.get("devanagari")!!
        val f: TransHelper = transHelper("itrans", "devanagari")
        letterTests(from, to, f)
        textTests(from, to, f)
    }

    @Test
    fun testWXToDevanagari() {
        val from: DataSet = dataSets.get("wx")!!
        val to: DataSet = dataSets.get("devanagari")!!
        val f: TransHelper = transHelper("wx", "devanagari")
        f.run(from.get("consonants"), to.get("consonants"), "Stops and nasals")
        f.run(from.get("symbols"), to.get("symbols"), "Symbols and punctuation")
        textTests(from, to, f)
    }

    @Test
    fun testTeluguToDevanagari() {
        val from: DataSet = dataSets.get("telugu")!!
        val to: DataSet = dataSets.get("devanagari")!!
        val f: TransHelper = transHelper("telugu", "devanagari")
        textTests(from, to, f)
    }

    @Test
    fun testUndefinedLetters() {
        val f: TransHelper = transHelper("devanagari", "gurmukhi")
        f.run("ऋच्छति", "ऋਚ੍ਛਤਿ", "")
    }
}