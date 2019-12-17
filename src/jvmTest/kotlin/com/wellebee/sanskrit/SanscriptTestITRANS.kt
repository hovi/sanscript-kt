package com.wellebee.sanskrit

import org.junit.Assert
import org.junit.Test

class SanscriptTestITRANS : SanscriptTest() {
    @Test
    fun testZeroWidthJoiner() {
        val f: TransHelper = transHelper("itrans", "devanagari")
        f.run("bara_u", "बरउ", "Separated vowels")
        f.run("k{}Shetra", "क्‍षेत्र", "Separated consonants")
    }

    @Test
    fun testVirama() {
        val f: TransHelper = transHelper("itrans", "devanagari")
        val g: TransHelper = transHelper("devanagari", "itrans")
        f.run("tattatvam.h", "तत्तत्वम्", "ITRANS to Devanagari")
        g.run("तत्तत्वम्", "tattatvam", "Devanagari to ITRANS")
    }

    @Test
    fun testAlternates() {
        val f: TransHelper = object : TransHelper {
            override fun run(itrans1: String?, itrans2: String?, description: String?) {
                val dev1: String = sanscript.t(itrans1!!, "itrans", "devanagari")
                val dev2: String = sanscript.t(itrans2!!, "itrans", "devanagari")
                Assert.assertEquals(description, dev2, dev1)
            }
        }
        f.run("A I U RRi RRI LLi LLI", "aa ii uu R^i R^I L^i L^I", "vowels")
        f.run("kA kI kU kRRi kRRI kLLi kLLI", "kaa kii kuu kR^i kR^I kL^i kL^I", "vowels (marks)")
        f.run("I U", "ee oo", "long I and U")
        f.run("kI kU", "kee koo", "long I and U (marks)")
        f.run("aM aM", "a.m a.n", "anusvara")
        f.run("~Na", "N^a", "na (kavarga)")
        f.run("ca", "cha", "ca")
        f.run("Cha Cha", "Ca chha", "cha")
        f.run("va", "wa", "va/wa")
        f.run("Sha Sha", "Sa shha", "sha (retroflex)")
        f.run("kSha kSha kSha", "kSa kshha xa", "ksha")
        f.run("j~na j~na", "GYa dnya", "jna")
        f.run("OM", "AUM", "om")
        f.run(".a | ||", "~ . ..", "punctuation")
        f.run("za", "Ja", "Devanagari za")
        f.run("a{\\m+}", "a.h.N", "{\\m+}")
    }

    @Test
    fun testBackslashEscape() {
        val f: TransHelper = transHelper("itrans", "devanagari")
        f.run("\\nara", "nअर", "")
        f.run("na\\ra", "नrअ", "")
        f.run("nara\\", "नर", "")
    }

    @Test
    fun testAccent() {
        val f: TransHelper = transHelper("itrans", "devanagari")
        f.run("a\\_gnimI\\'le pu\\_rohi\\'tam", "अ॒ग्निमी॑ले पु॒रोहि॑तम्", "")
        f.run("naH\\' naH\\_ naH\\`", "नः॑ नः॒ नः॒", "Visarga + accent")
        f.run("na\\'H na\\_H na\\`H", "नः॑ नः॒ नः॒", "Accent + visarga")
        f.run(
            "taM\\' ta.m\\' ta.n\\' taM\\_ ta.m\\_ ta.n\\_ taM\\` ta.m\\` ta.n\\`",
            "तं॑ तं॑ तं॑ तं॒ तं॒ तं॒ तं॒ तं॒ तं॒",
            "Anusvara + accent"
        )
        f.run(
            "ta\\'M ta\\'.m ta\\'.n ta\\_M ta\\_.m ta\\_.n ta\\`M ta\\`.m ta\\`.n",
            "तं॑ तं॑ तं॑ तं॒ तं॒ तं॒ तं॒ तं॒ तं॒",
            "Accent + anusvara"
        )
    }

    @Test
    fun testNonSanskritLetters() {
        val ben: TransHelper = transHelper("itrans", "bengali")
        val dev: TransHelper = transHelper("itrans", "devanagari")
        val kan: TransHelper = transHelper("itrans", "kannada")
        val guj: TransHelper = transHelper("itrans", "gujarati")
        val gur: TransHelper = transHelper("itrans", "gurmukhi")
        val mal: TransHelper = transHelper("itrans", "malayalam")
        val ori: TransHelper = transHelper("itrans", "oriya")
        val tam: TransHelper = transHelper("itrans", "tamil")
        val tel: TransHelper = transHelper("itrans", "telugu")
        ben.run(".De .Dhe Ye", "ডে ঢে যে", "")
        dev.run("qa KA Gi zI .Du .DU fRRi YRRI RLLi", "क़ ख़ा ग़ि ज़ी ड़ु ड़ू फ़ृ य़ॄ ऱॢ", "")
        dev.run("ka.cna", "कॅन", "")
        kan.run("fI RI", "ಫೀ ಱೀ", "")
        guj.run("ka.cna", "કૅન", "")
        gur.run("Ko Go zo Jo .Do fo", "ਖੋ ਗੋ ਜੋ ਜੋ ਡੋ ਫੋ", "")
        mal.run("RI", "റീ", "")
        ori.run(".DU .DhU YU", "ଡୂ ଢୂ ଯୂ", "")
        tam.run("RI", "றீ", "")
    }
}