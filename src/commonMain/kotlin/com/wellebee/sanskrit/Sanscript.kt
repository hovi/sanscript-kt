package com.wellebee.sanskrit

import kotlin.math.min


/**
 * Sanscript
 *
 * Sanscript is a Sanskrit transliteration library. Currently, it supports
 * other Indian languages only incidentally.
 *
 * Released under the MIT and GPL Licenses.
 */
typealias AlternateMap = HashMap<String, Array<String>>

typealias Schemes = MutableMap<String, Scheme>

typealias Scheme = MutableMap<String, Array<String>>

typealias Alternates = HashMap<String, AlternateMap>

typealias SMap = HashMap<String, String>


fun <T> Array<T>.withoutFirst(): Array<T> {
    return sliceArray(1 until size)
}


fun Scheme.cheapCopy(): Scheme {
    val copy: Scheme = mutableMapOf<String, Array<String>>()
    for ((key, value) in entries) {
        copy[key] = value.toList().toTypedArray()
    }
    return copy
}

class Sanscript {
    // Options interface.
    data class Options(
        val skipSgml: Boolean = false,
        val syncope: Boolean = false,
        val skipStarters: Array<String> = arrayOf("["),
        val skipEnds: Array<String> = arrayOf("]")
    )

    /**
     * Returns the collection of all schemes.
     *
     * @return Schemes
     */
    val schemes = mutableMapOf<String, Scheme>()

    /* Schemes
     * =======
     * Schemes are of two kinds: "Brahmic" and "roman." "Brahmic" schemes
     * describe abugida scripts found in India. "Roman" schemes describe
     * manufactured alphabets that are meant to describe or encode Brahmi
     * scripts. Abugidas and alphabets are processed by separate algorithms
     * because of the unique difficulties involved with each.
     *
     * Brahmic consonants are stated without a virama. Roman consonants are
     * stated without the vowel 'a'.
     *
     * (Since "abugida" is not a well-known term, Sanscript uses "Brahmic"
     * and "roman" for clarity.)
     */
    private fun initializeSchemes() {
        var scheme: Scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("অ", "আ", "ই", "ঈ", "উ", "ঊ", "ঋ", "ৠ", "ঌ", "ৡ", "", "এ", "ঐ", "", "ও", "ঔ")
        scheme["vowel_marks"] = arrayOf("া", "ি", "ী", "ু", "ূ", "ৃ", "ৄ", "ৢ", "ৣ", "", "ে", "ৈ", "", "ো", "ৌ")
        scheme["other_marks"] = arrayOf("ং", "ঃ", "ঁ")
        scheme["virama"] = arrayOf("্")
        scheme["consonants"] = arrayOf(
            "ক",
            "খ",
            "গ",
            "ঘ",
            "ঙ",
            "চ",
            "ছ",
            "জ",
            "ঝ",
            "ঞ",
            "ট",
            "ঠ",
            "ড",
            "ঢ",
            "ণ",
            "ত",
            "থ",
            "দ",
            "ধ",
            "ন",
            "প",
            "ফ",
            "ব",
            "ভ",
            "ম",
            "য",
            "র",
            "ল",
            "ব",
            "শ",
            "ষ",
            "স",
            "হ",
            "ळ",
            "ক্ষ",
            "জ্ঞ"
        )
        scheme["symbols"] = arrayOf("০", "১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯", "ॐ", "ঽ", "।", "॥")
        scheme["other"] = arrayOf("", "", "", "", "ড", "ঢ", "", "য", "")
        schemes["bengali"] = scheme
        /* Devanagari
         * ----------
         * The most comprehensive and unambiguous Brahmic script listed.
         */scheme = mutableMapOf()
        // "Independent" forms of the vowels. These are used whenever the
// vowel does not immediately follow a consonant.
        scheme["vowels"] = arrayOf("अ", "आ", "इ", "ई", "उ", "ऊ", "ऋ", "ॠ", "ऌ", "ॡ", "ऎ", "ए", "ऐ", "ऒ", "ओ", "औ")
        // "Dependent" forms of the vowels. These are used whenever the
// vowel immediately follows a consonant. If a letter is not
// listed in `vowels`, it should not be listed here.
        scheme["vowel_marks"] = arrayOf("ा", "ि", "ी", "ु", "ू", "ृ", "ॄ", "ॢ", "ॣ", "ॆ", "े", "ै", "ॊ", "ो", "ौ")
        // Miscellaneous marks, all of which are used in Sanskrit.
        scheme["other_marks"] = arrayOf("ं", "ः", "ँ")
        // In syllabic scripts like Devanagari, consonants have an inherent
// vowel that must be suppressed explicitly. We do so by putting a
// virama after the consonant.
        scheme["virama"] = arrayOf("्")
        // Various Sanskrit consonants and consonant clusters. Every token
// here has an explicit vowel. Thus "क" is "ka" instead of "k".
        scheme["consonants"] = arrayOf(
            "क",
            "ख",
            "ग",
            "घ",
            "ङ",
            "च",
            "छ",
            "ज",
            "झ",
            "ञ",
            "ट",
            "ठ",
            "ड",
            "ढ",
            "ण",
            "त",
            "थ",
            "द",
            "ध",
            "न",
            "प",
            "फ",
            "ब",
            "भ",
            "म",
            "य",
            "र",
            "ल",
            "व",
            "श",
            "ष",
            "स",
            "ह",
            "ळ",
            "क्ष",
            "ज्ञ"
        )
        // Numbers and punctuation
        scheme["symbols"] = arrayOf("०", "१", "२", "३", "४", "५", "६", "७", "८", "९", "ॐ", "ऽ", "।", "॥")
        // Zero-width joiner. This is used to separate a consonant cluster
// and avoid a complex ligature.
        scheme["zwj"] = arrayOf("\u200D")
        // Dummy consonant. This is used in ITRANS to prevert certain types
// of parser ambiguity. Thus "barau" -> बरौ but "bara_u" -> बरउ.
        scheme["skip"] = arrayOf("")
        // Vedic accent. Udatta and anudatta.
        scheme["accent"] = arrayOf("\u0951", "\u0952")
        // Accent combined with anusvara and and visarga. For compatibility
// with ITRANS, which allows the reverse of these four.
        scheme["combo_accent"] = arrayOf("ः॑", "ः॒", "ं॑", "ं॒")
        scheme["candra"] = arrayOf("ॅ")
        // Non-Sanskrit consonants
        scheme["other"] = arrayOf("क़", "ख़", "ग़", "ज़", "ड़", "ढ़", "फ़", "य़", "ऱ")
        schemes["devanagari"] = scheme
        /* Gujarati
         * --------
         * Sanskrit-complete.
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("અ", "આ", "ઇ", "ઈ", "ઉ", "ઊ", "ઋ", "ૠ", "ઌ", "ૡ", "", "એ", "ઐ", "", "ઓ", "ઔ")
        scheme["vowel_marks"] = arrayOf("ા", "િ", "ી", "ુ", "ૂ", "ૃ", "ૄ", "ૢ", "ૣ", "", "ે", "ૈ", "", "ો", "ૌ")
        scheme["other_marks"] = arrayOf("ં", "ઃ", "ઁ")
        scheme["virama"] = arrayOf("્")
        scheme["consonants"] = arrayOf(
            "ક",
            "ખ",
            "ગ",
            "ઘ",
            "ઙ",
            "ચ",
            "છ",
            "જ",
            "ઝ",
            "ઞ",
            "ટ",
            "ઠ",
            "ડ",
            "ઢ",
            "ણ",
            "ત",
            "થ",
            "દ",
            "ધ",
            "ન",
            "પ",
            "ફ",
            "બ",
            "ભ",
            "મ",
            "ય",
            "ર",
            "લ",
            "વ",
            "શ",
            "ષ",
            "સ",
            "હ",
            "ળ",
            "ક્ષ",
            "જ્ઞ"
        )
        scheme["symbols"] = arrayOf("૦", "૧", "૨", "૩", "૪", "૫", "૬", "૭", "૮", "૯", "ૐ", "ઽ", "૤", "૥")
        scheme["candra"] = arrayOf("ૅ")
        schemes["gujarati"] = scheme
        /* Gurmukhi
         * --------
         * Missing R/RR/lR/lRR
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("ਅ", "ਆ", "ਇ", "ਈ", "ਉ", "ਊ", "", "", "", "", "", "ਏ", "ਐ", "", "ਓ", "ਔ")
        scheme["vowel_marks"] = arrayOf("ਾ", "ਿ", "ੀ", "ੁ", "ੂ", "", "", "", "", "", "ੇ", "ੈ", "", "ੋ", "ੌ")
        scheme["other_marks"] = arrayOf("ਂ", "ਃ", "ਁ")
        scheme["virama"] = arrayOf("੍")
        scheme["consonants"] = arrayOf(
            "ਕ",
            "ਖ",
            "ਗ",
            "ਘ",
            "ਙ",
            "ਚ",
            "ਛ",
            "ਜ",
            "ਝ",
            "ਞ",
            "ਟ",
            "ਠ",
            "ਡ",
            "ਢ",
            "ਣ",
            "ਤ",
            "ਥ",
            "ਦ",
            "ਧ",
            "ਨ",
            "ਪ",
            "ਫ",
            "ਬ",
            "ਭ",
            "ਮ",
            "ਯ",
            "ਰ",
            "ਲ",
            "ਵ",
            "ਸ਼",
            "ਸ਼",
            "ਸ",
            "ਹ",
            "ਲ਼",
            "ਕ੍ਸ਼",
            "ਜ੍ਞ"
        )
        scheme["symbols"] = arrayOf("੦", "੧", "੨", "੩", "੪", "੫", "੬", "੭", "੮", "੯", "ॐ", "ऽ", "।", "॥")
        scheme["other"] = arrayOf("", "ਖ", "ਗ", "ਜ", "ਡ", "", "ਫ", "", "")
        schemes["gurmukhi"] = scheme
        /* Kannada
         * -------
         * Sanskrit-complete.
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("ಅ", "ಆ", "ಇ", "ಈ", "ಉ", "ಊ", "ಋ", "ೠ", "ಌ", "ೡ", "ಎ", "ಏ", "ಐ", "ಒ", "ಓ", "ಔ")
        scheme["vowel_marks"] = arrayOf("ಾ", "ಿ", "ೀ", "ು", "ೂ", "ೃ", "ೄ", "ೢ", "ೣ", "ೆ", "ೇ", "ೈ", "ೊ", "ೋ", "ೌ")
        scheme["other_marks"] = arrayOf("ಂ", "ಃ", "ँ")
        scheme["virama"] = arrayOf("್")
        scheme["consonants"] = arrayOf(
            "ಕ",
            "ಖ",
            "ಗ",
            "ಘ",
            "ಙ",
            "ಚ",
            "ಛ",
            "ಜ",
            "ಝ",
            "ಞ",
            "ಟ",
            "ಠ",
            "ಡ",
            "ಢ",
            "ಣ",
            "ತ",
            "ಥ",
            "ದ",
            "ಧ",
            "ನ",
            "ಪ",
            "ಫ",
            "ಬ",
            "ಭ",
            "ಮ",
            "ಯ",
            "ರ",
            "ಲ",
            "ವ",
            "ಶ",
            "ಷ",
            "ಸ",
            "ಹ",
            "ಳ",
            "ಕ್ಷ",
            "ಜ್ಞ"
        )
        scheme["symbols"] = arrayOf("೦", "೧", "೨", "೩", "೪", "೫", "೬", "೭", "೮", "೯", "ಓಂ", "ಽ", "।", "॥")
        scheme["other"] = arrayOf("", "", "", "", "", "", "ಫ", "", "ಱ")
        schemes["kannada"] = scheme
        /* Malayalam
         * ---------
         * Sanskrit-complete.
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("അ", "ആ", "ഇ", "ഈ", "ഉ", "ഊ", "ഋ", "ൠ", "ഌ", "ൡ", "എ", "ഏ", "ഐ", "ഒ", "ഓ", "ഔ")
        scheme["vowel_marks"] = arrayOf("ാ", "ി", "ീ", "ു", "ൂ", "ൃ", "ൄ", "ൢ", "ൣ", "െ", "േ", "ൈ", "ൊ", "ോ", "ൌ")
        scheme["other_marks"] = arrayOf("ം", "ഃ", "ँ")
        scheme["virama"] = arrayOf("്")
        scheme["consonants"] = arrayOf(
            "ക",
            "ഖ",
            "ഗ",
            "ഘ",
            "ങ",
            "ച",
            "ഛ",
            "ജ",
            "ഝ",
            "ഞ",
            "ട",
            "ഠ",
            "ഡ",
            "ഢ",
            "ണ",
            "ത",
            "ഥ",
            "ദ",
            "ധ",
            "ന",
            "പ",
            "ഫ",
            "ബ",
            "ഭ",
            "മ",
            "യ",
            "ര",
            "ല",
            "വ",
            "ശ",
            "ഷ",
            "സ",
            "ഹ",
            "ള",
            "ക്ഷ",
            "ജ്ഞ"
        )
        scheme["symbols"] = arrayOf("൦", "൧", "൨", "൩", "൪", "൫", "൬", "൭", "൮", "൯", "ഓം", "ഽ", "।", "॥")
        scheme["other"] = arrayOf("", "", "", "", "", "", "", "", "റ")
        schemes["malayalam"] = scheme
        /* Oriya
         * -----
         * Sanskrit-complete.
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("ଅ", "ଆ", "ଇ", "ଈ", "ଉ", "ଊ", "ଋ", "ୠ", "ଌ", "ୡ", "", "ଏ", "ଐ", "", "ଓ", "ଔ")
        scheme["vowel_marks"] = arrayOf("ା", "ି", "ୀ", "ୁ", "ୂ", "ୃ", "ୄ", "ୢ", "ୣ", "", "େ", "ୈ", "", "ୋ", "ୌ")
        scheme["other_marks"] = arrayOf("ଂ", "ଃ", "ଁ")
        scheme["virama"] = arrayOf("୍")
        scheme["consonants"] = arrayOf(
            "କ",
            "ଖ",
            "ଗ",
            "ଘ",
            "ଙ",
            "ଚ",
            "ଛ",
            "ଜ",
            "ଝ",
            "ଞ",
            "ଟ",
            "ଠ",
            "ଡ",
            "ଢ",
            "ଣ",
            "ତ",
            "ଥ",
            "ଦ",
            "ଧ",
            "ନ",
            "ପ",
            "ଫ",
            "ବ",
            "ଭ",
            "ମ",
            "ଯ",
            "ର",
            "ଲ",
            "ଵ",
            "ଶ",
            "ଷ",
            "ସ",
            "ହ",
            "ଳ",
            "କ୍ଷ",
            "ଜ୍ଞ"
        )
        scheme["symbols"] = arrayOf("୦", "୧", "୨", "୩", "୪", "୫", "୬", "୭", "୮", "୯", "ଓଂ", "ଽ", "।", "॥")
        scheme["other"] = arrayOf("", "", "", "", "ଡ", "ଢ", "", "ଯ", "")
        schemes["oriya"] = scheme
        /* Tamil
         * -----
         * Missing R/RR/lR/lRR vowel marks and voice/aspiration distinctions.
         * The most incomplete of the Sanskrit schemes here.
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("அ", "ஆ", "இ", "ஈ", "உ", "ஊ", "", "", "", "", "எ", "ஏ", "ஐ", "ஒ", "ஓ", "ஔ")
        scheme["vowel_marks"] = arrayOf("ா", "ி", "ீ", "ு", "ூ", "", "", "", "", "ெ", "ே", "ை", "ொ", "ோ", "ௌ")
        scheme["other_marks"] = arrayOf("ஂ", "ஃ", "")
        scheme["virama"] = arrayOf("்")
        scheme["consonants"] = arrayOf(
            "க",
            "க",
            "க",
            "க",
            "ங",
            "ச",
            "ச",
            "ஜ",
            "ச",
            "ஞ",
            "ட",
            "ட",
            "ட",
            "ட",
            "ண",
            "த",
            "த",
            "த",
            "த",
            "ந",
            "ப",
            "ப",
            "ப",
            "ப",
            "ம",
            "ய",
            "ர",
            "ல",
            "வ",
            "ஶ",
            "ஷ",
            "ஸ",
            "ஹ",
            "ள",
            "க்ஷ",
            "ஜ்ஞ"
        )
        scheme["symbols"] = arrayOf("௦", "௧", "௨", "௩", "௪", "௫", "௬", "௭", "௮", "௯", "ௐ", "ऽ", "।", "॥")
        scheme["other"] = arrayOf("", "", "", "", "", "", "", "", "ற")
        schemes["tamil"] = scheme
        /* Telugu
         * ------
         * Sanskrit-complete.
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("అ", "ఆ", "ఇ", "ఈ", "ఉ", "ఊ", "ఋ", "ౠ", "ఌ", "ౡ", "ఎ", "ఏ", "ఐ", "ఒ", "ఓ", "ఔ")
        scheme["vowel_marks"] = arrayOf("ా", "ి", "ీ", "ు", "ూ", "ృ", "ౄ", "ౢ", "ౣ", "ె", "ే", "ై", "ొ", "ో", "ౌ")
        scheme["other_marks"] = arrayOf("ం", "ః", "ఁ")
        scheme["virama"] = arrayOf("్")
        scheme["consonants"] = arrayOf(
            "క",
            "ఖ",
            "గ",
            "ఘ",
            "ఙ",
            "చ",
            "ఛ",
            "జ",
            "ఝ",
            "ఞ",
            "ట",
            "ఠ",
            "డ",
            "ఢ",
            "ణ",
            "త",
            "థ",
            "ద",
            "ధ",
            "న",
            "ప",
            "ఫ",
            "బ",
            "భ",
            "మ",
            "య",
            "ర",
            "ల",
            "వ",
            "శ",
            "ష",
            "స",
            "హ",
            "ళ",
            "క్ష",
            "జ్ఞ"
        )
        scheme["symbols"] = arrayOf("౦", "౧", "౨", "౩", "౪", "౫", "౬", "౭", "౮", "౯", "ఓం", "ఽ", "।", "॥")
        scheme["other"] = arrayOf("", "", "", "", "", "", "", "", "ఱ")
        schemes["telugu"] = scheme
        /* International Alphabet of Sanskrit Transliteration
         * --------------------------------------------------
         * The most "professional" Sanskrit romanization scheme.put("
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("a", "ā", "i", "ī", "u", "ū", "ṛ", "ṝ", "ḷ", "ḹ", "", "e", "ai", "", "o", "au")
        scheme["other_marks"] = arrayOf("ṃ", "ḥ", "~")
        scheme["virama"] = arrayOf("")
        scheme["consonants"] = arrayOf(
            "k",
            "kh",
            "g",
            "gh",
            "ṅ",
            "c",
            "ch",
            "j",
            "jh",
            "ñ",
            "ṭ",
            "ṭh",
            "ḍ",
            "ḍh",
            "ṇ",
            "t",
            "th",
            "d",
            "dh",
            "n",
            "p",
            "ph",
            "b",
            "bh",
            "m",
            "y",
            "r",
            "l",
            "v",
            "ś",
            "ṣ",
            "s",
            "h",
            "ḻ",
            "kṣ",
            "jñ"
        )
        scheme["symbols"] = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "oṃ", "'", "।", "॥")
        schemes["iast"] = scheme
        /* ITRANS
         * ------
         * One of the first romanization schemes -- and one of the most
         * complicated. For alternate forms, see the "allAlternates" variable
         * below.
         *
         * '_' is a "null" letter, which allows adjacent vowels.
         */scheme = mutableMapOf()
        scheme["vowels"] =
            arrayOf("a", "A", "i", "I", "u", "U", "RRi", "RRI", "LLi", "LLI", "", "e", "ai", "", "o", "au")
        scheme["other_marks"] = arrayOf("M", "H", ".N")
        scheme["virama"] = arrayOf("")
        scheme["consonants"] = arrayOf(
            "k",
            "kh",
            "g",
            "gh",
            "~N",
            "ch",
            "Ch",
            "j",
            "jh",
            "~n",
            "T",
            "Th",
            "D",
            "Dh",
            "N",
            "t",
            "th",
            "d",
            "dh",
            "n",
            "p",
            "ph",
            "b",
            "bh",
            "m",
            "y",
            "r",
            "l",
            "v",
            "sh",
            "Sh",
            "s",
            "h",
            "L",
            "kSh",
            "j~n"
        )
        scheme["symbols"] = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "OM", ".a", "|", "||")
        scheme["candra"] = arrayOf(".c")
        scheme["zwj"] = arrayOf("{}")
        scheme["skip"] = arrayOf("_")
        scheme["accent"] = arrayOf("\\'", "\\_")
        scheme["combo_accent"] = arrayOf("\\'H", "\\_H", "\\'M", "\\_M")
        scheme["other"] = arrayOf("q", "K", "G", "z", ".D", ".Dh", "f", "Y", "R")
        schemes["itrans"] = scheme
        /* Harvard-Kyoto
         * -------------
         * A simple 1:1 mapping.
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("a", "A", "i", "I", "u", "U", "R", "RR", "lR", "lRR", "", "e", "ai", "", "o", "au")
        scheme["other_marks"] = arrayOf("M", "H", "~")
        scheme["virama"] = arrayOf("")
        scheme["consonants"] = arrayOf(
            "k",
            "kh",
            "g",
            "gh",
            "G",
            "c",
            "ch",
            "j",
            "jh",
            "J",
            "T",
            "Th",
            "D",
            "Dh",
            "N",
            "t",
            "th",
            "d",
            "dh",
            "n",
            "p",
            "ph",
            "b",
            "bh",
            "m",
            "y",
            "r",
            "l",
            "v",
            "z",
            "S",
            "s",
            "h",
            "L",
            "kS",
            "jJ"
        )
        scheme["symbols"] = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "OM", "'", "|", "||")
        schemes["hk"] = scheme
        /* National Library at Kolkata
         * ---------------------------
         * Apart from using "ē" and "ō" instead of "e" and "o", this scheme is
         * identical to IAST. ṝ, ḷ, and ḹ are not part of the scheme proper.
         *
         * This is defined further below.
         */
/* Sanskrit Library Phonetic Basic
         * -------------------------------
         * With one ASCII letter per phoneme, this is the tersest transliteration
         * scheme in use today and is especially suited to computer processing.
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("a", "A", "i", "I", "u", "U", "f", "F", "x", "X", "", "e", "E", "", "o", "O")
        scheme["other_marks"] = arrayOf("M", "H", "~")
        scheme["virama"] = arrayOf("")
        scheme["consonants"] = arrayOf(
            "k",
            "K",
            "g",
            "G",
            "N",
            "c",
            "C",
            "j",
            "J",
            "Y",
            "w",
            "W",
            "q",
            "Q",
            "R",
            "t",
            "T",
            "d",
            "D",
            "n",
            "p",
            "P",
            "b",
            "B",
            "m",
            "y",
            "r",
            "l",
            "v",
            "S",
            "z",
            "s",
            "h",
            "L",
            "kz",
            "jY"
        )
        scheme["symbols"] = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "oM", "'", ".", "..")
        schemes["slp1"] = scheme
        /* Velthuis
         * --------
         * A case-insensitive Sanskrit encoding.
         */scheme = mutableMapOf()
        scheme["vowels"] =
            arrayOf("a", "aa", "i", "ii", "u", "uu", ".r", ".rr", ".li", ".ll", "", "e", "ai", "", "o", "au")
        scheme["other_marks"] = arrayOf(".m", ".h", "")
        scheme["virama"] = arrayOf("")
        scheme["consonants"] = arrayOf(
            "k",
            "kh",
            "g",
            "gh",
            "\"n",
            "c",
            "ch",
            "j",
            "jh",
            "~n",
            ".t",
            ".th",
            ".d",
            ".d",
            ".n",
            "t",
            "th",
            "d",
            "dh",
            "n",
            "p",
            "ph",
            "b",
            "bh",
            "m",
            "y",
            "r",
            "l",
            "v",
            "~s",
            ".s",
            "s",
            "h",
            "L",
            "k.s",
            "j~n"
        )
        scheme["symbols"] = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "o.m", "'", "|", "||")
        schemes["velthuis"] = scheme
        /* WX
         * --
         * As terse as SLP1.
         */scheme = mutableMapOf()
        scheme["vowels"] = arrayOf("a", "A", "i", "I", "u", "U", "q", "Q", "L", "", "", "e", "E", "", "o", "O")
        scheme["other_marks"] = arrayOf("M", "H", "z")
        scheme["virama"] = arrayOf("")
        scheme["consonants"] = arrayOf(
            "k",
            "K",
            "g",
            "G",
            "f",
            "c",
            "C",
            "j",
            "J",
            "F",
            "t",
            "T",
            "d",
            "D",
            "N",
            "w",
            "W",
            "x",
            "X",
            "n",
            "p",
            "P",
            "b",
            "B",
            "m",
            "y",
            "r",
            "l",
            "v",
            "S",
            "R",
            "s",
            "h",
            "",
            "kR",
            "jF"
        )
        scheme["symbols"] = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "oM", "'", "|", "||")
        schemes["wx"] = scheme
    }

    // Set of names of Roman schemes.
    private val romanSchemes: MutableMap<String, Boolean> = HashMap()

    // Map of alternate encodings.

    private val allAlternates = Alternates()
    private fun initializeAlternates() {
        val map = AlternateMap()
        map["A"] = arrayOf("aa")
        map["I"] = arrayOf("ii", "ee")
        map["U"] = arrayOf("uu", "oo")
        map["RRi"] = arrayOf("R^i")
        map["RRI"] = arrayOf("R^I")
        map["LLi"] = arrayOf("L^i")
        map["LLI"] = arrayOf("L^I")
        map["M"] = arrayOf(".m", ".n")
        map["~N"] = arrayOf("N^")
        map["ch"] = arrayOf("c")
        map["Ch"] = arrayOf("C", "chh")
        map["~n"] = arrayOf("JN")
        map["v"] = arrayOf("w")
        map["Sh"] = arrayOf("S", "shh")
        map["kSh"] = arrayOf("kS", "x")
        map["j~n"] = arrayOf("GY", "dny")
        map["OM"] = arrayOf("AUM")
        map["\\_"] = arrayOf("\\`")
        map["\\_H"] = arrayOf("\\`H")
        map["\\'M"] = arrayOf("\\'.m", "\\'.n")
        map["\\_M"] = arrayOf("\\_.m", "\\_.n", "\\`M", "\\`.m", "\\`.n")
        map[".a"] = arrayOf("~")
        map["|"] = arrayOf(".")
        map["||"] = arrayOf("..")
        map["z"] = arrayOf("J")
        allAlternates["itrans"] = map
    }

    // object cache
    private inner class Cache {
        var from: String? = null
        var to: String? = null
        var map: TMap? = null
        var options: Options? = null
    }

    private val cache = Cache()
    /**
     * Check whether the given scheme encodes romanized Sanskrit.
     *
     * @param name  the scheme name
     * @return      boolean
     */
    fun isRomanScheme(name: String): Boolean {
        val value = romanSchemes[name]
        return value != null && value
    }

    /**
     * Add a Brahmic scheme to Sanscript.
     *
     * Schemes are of two types: "Brahmic" and "roman". Brahmic consonants
     * have an inherent vowel sound, but roman consonants do not. This is the
     * main difference between these two types of scheme.
     *
     * A scheme definition is an object ("{}") that maps a group name to a
     * list of characters. For illustration, see the "devanagari" scheme at
     * the top of this file.
     *
     * You can use whatever group names you like, but for the best results,
     * you should use the same group names that Sanscript does.
     *
     * @param name    the scheme name
     * @param scheme  the scheme data itself. This should be constructed as
     * described above.
     */
    fun addBrahmicScheme(name: String, scheme: Scheme) {
        schemes[name] = scheme
    }

    /**
     * Add a roman scheme to Sanscript.
     *
     * See the comments on addBrahmicScheme. The "vowel_marks" field
     * can be omitted.
     *
     * @param name    the scheme name
     * @param scheme  the scheme data itself
     */
    fun addRomanScheme(name: String, scheme: Scheme) {
        if (scheme["vowel_marks"] == null) {
            scheme["vowel_marks"] = scheme["vowels"]!!.withoutFirst()
        }
        schemes[name] = scheme
        romanSchemes[name] = true
    }

    // Set up various schemes
    private fun initializeSpecialSchemes() { // Set up roman schemes
        val kolkata = schemes["iast"]!!.cheapCopy()
        kolkata["vowels"] = arrayOf("a", "ā", "i", "ī", "u", "ū", "ṛ", "ṝ", "ḷ", "ḹ", "e", "ē", "ai", "o", "ō", "au")
        schemes["kolkata"] = kolkata
        // These schemes already belong to schemes. But by adding
// them again with `addRomanScheme`, we automatically build up
// `romanSchemes` and define a `vowel_marks` field for each one.
        val schemeNames = arrayOf("iast", "itrans", "hk", "kolkata", "slp1", "velthuis", "wx")
        for (name in schemeNames) {
            addRomanScheme(name, schemes[name]!!)
        }
        // ITRANS variant, which supports Dravidian short 'e' and 'o'.
        val itrans_dravidian = schemes["itrans"]!!.cheapCopy()
        itrans_dravidian["vowels"] =
            arrayOf("a", "A", "i", "I", "u", "U", "Ri", "RRI", "LLi", "LLi", "e", "E", "ai", "o", "O", "au")
        itrans_dravidian["vowel_marks"] = itrans_dravidian["vowels"]!!.withoutFirst()
        allAlternates["itrans_dravidian"] = allAlternates["itrans"]!!
        addRomanScheme("itrans_dravidian", itrans_dravidian)
    }

    private data class TMap(
        val fromRoman: Boolean = false,
        val toRoman: Boolean = false,
        val consonants: SMap,
        val letters: SMap,
        val marks: SMap,
        val virama: Array<String> = emptyArray(),
        val maxTokenLength: Int = 0
    )

    /**
     * Create a map from every character in `from` to its partner in `to`.
     * Also, store any "marks" that `from` might have.
     *
     * @param from     input scheme
     * @param to       output scheme
     * @param options  scheme options
     */
    private fun makeMap(from: String, to: String, options: Options): TMap {
        val alternates = if (allAlternates[from] != null) allAlternates[from] else AlternateMap()
        val consonants = SMap()
        val fromScheme = schemes[from]
        val letters = SMap()
        val tokenLengths: MutableList<Int> = ArrayList()
        val marks = SMap()
        val toScheme = schemes[to]
        for ((group, fromGroup) in fromScheme!!) {
            val toGroup = toScheme!![group] ?: continue
            for (i in fromGroup.indices) {
                val F = fromGroup[i]
                val T = toGroup[i]
                val alts: Array<String>? = if (alternates!![F] != null) alternates[F] else arrayOf()
                tokenLengths.add(F.length)
                for (value in alts!!) {
                    tokenLengths.add(value.length)
                }
                if (group == "vowel_marks" || group == "virama") {
                    marks[F] = T
                    for (alt in alts) {
                        marks[alt] = T
                    }
                } else {
                    letters[F] = T
                    for (alt in alts) {
                        letters[alt] = T
                    }
                    if (group == "consonants" || group == "other") {
                        consonants[F] = T
                        for (alt in alts) {
                            consonants[alt] = T
                        }
                    }
                }
            }
        }
        val map = TMap(
            consonants = consonants,
            fromRoman = isRomanScheme(from),
            letters = letters,
            marks = marks,
            maxTokenLength = (tokenLengths.max()!!),
            toRoman = isRomanScheme(to),
            virama = toScheme!!["virama"]!!
        )
        return map
    }

    /**
     * Transliterate from a romanized script.
     *
     * @param data     the string to transliterate
     * @param map      map data generated from makeMap()
     * @param options  transliteration options
     * @return         the finished string
     */
    private fun transliterateRoman(data: String, map: TMap, options: Options): String {
        val buf = StringBuilder()
        val consonants = map.consonants
        val dataLength = data.length
        var hadConsonant = false
        val letters = map.letters
        val marks = map.marks
        val maxTokenLength = map.maxTokenLength
        var tempLetter: String?
        var tempMark: String?
        var tokenBuffer = StringBuilder()
        val toRoman = map.toRoman
        val virama = map.virama
        // Transliteration state. It's controlled by these values:
// - `skippingSGML`: are we in SGML?
// - `toggledTrans`: are we in a toggled region?
//
// We combine these values into a single variable `skippingTrans`:
//
//     `skippingTrans` = skippingSGML || toggledTrans;
//
// If (and only if) this value is true, don't transliterate.
        var skippingSGML = false
        var skippingTrans: Boolean
        var toggledTrans = false
        var i = 0
        while (i < dataLength || tokenBuffer.isNotEmpty()) {
            // Fill the token buffer, if possible.
            val difference = maxTokenLength - tokenBuffer.length
            if (difference > 0 && i < dataLength) {
                tokenBuffer.append(data[i])
                if (difference > 1) {
                    i++
                    continue
                }
            }
            // Match all token substrings to our map.
            for (j in 0 until maxTokenLength) {
                val token = tokenBuffer.substring(0, min(maxTokenLength - j, tokenBuffer.length))
                if (skippingSGML) {

                    skippingSGML = (token != ">" && token !in options.skipEnds)
                } else if (token == "<") {
                    skippingSGML = options.skipSgml
                } else if (token == "##") {
                    toggledTrans = !toggledTrans
                    tokenBuffer = tokenBuffer.delete(0, 2)
                    break
                } else if (token in options.skipStarters) {
                    skippingSGML = options.skipSgml
                }
                skippingTrans = skippingSGML || toggledTrans
                if (letters[token] != null && !skippingTrans) {
                    tempLetter = letters[token]!!
                    if (toRoman) {
                        buf.append(tempLetter)
                    } else { // Handle the implicit vowel. Ignore 'a' and force
// vowels to appear as marks if we've just seen a
// consonant.
                        if (hadConsonant) {
                            if (marks[token].also { tempMark = it } != null) {
                                buf.append(tempMark)
                            } else if (token != "a") {
                                buf.append(virama[0])
                                buf.append(tempLetter)
                            }
                        } else {
                            buf.append(tempLetter)
                        }
                        hadConsonant = consonants[token] != null
                    }
                    tokenBuffer = tokenBuffer.delete(0, maxTokenLength - j)
                    break
                } else if (j == maxTokenLength - 1) {
                    if (hadConsonant) {
                        hadConsonant = false
                        if (!options.syncope) {
                            buf.append(virama[0])
                        }
                    }
                    buf.append(token)
                    tokenBuffer = tokenBuffer.delete(0, 1)
                    // 'break' is redundant here, "j == ..." is true only on
// the last iteration.
                }
            }
            i++
        }
        if (hadConsonant && !options.syncope) {
            buf.append(virama[0])
        }
        return buf.toString()
    }

    /**
     * Transliterate from a Brahmic script.
     *
     * @param data     the string to transliterate
     * @param map      map data generated from makeMap()
     * @param options  transliteration options
     * @return         the finished string
     */
    private fun transliterateBrahmic(data: String, map: TMap, options: Options): String {
        val buf = StringBuilder()
        val consonants = map.consonants
        var danglingHash = false
        var hadRomanConsonant = false
        val letters = map.letters
        val marks = map.marks
        var temp: String
        val toRoman = map.toRoman
        var skippingTrans = false
        for (element in data) {
            val L = element.toString()
            // Toggle transliteration state
            if (L == "#") {
                if (danglingHash) {
                    skippingTrans = !skippingTrans
                    danglingHash = false
                } else {
                    danglingHash = true
                }
                if (hadRomanConsonant) {
                    buf.append('a')
                    hadRomanConsonant = false
                }
                continue
            } else if (skippingTrans) {
                buf.append(L)
                continue
            }
            if (marks[L] != null) {
                temp = marks[L]!!
                buf.append(temp)
                hadRomanConsonant = false
            } else {
                if (danglingHash) {
                    buf.append('#')
                    danglingHash = false
                }
                if (hadRomanConsonant) {
                    buf.append('a')
                    hadRomanConsonant = false
                }
                // Push transliterated letter if possible. Otherwise, push
// the letter itself.
                if (letters[L] != null && letters[L] != "") {
                    temp = letters[L]!!
                    buf.append(temp)
                    hadRomanConsonant = toRoman && consonants[L] != null
                } else {
                    buf.append(L)
                }
            }
        }
        if (hadRomanConsonant) {
            buf.append('a')
        }
        return buf.toString()
    }

    /**
     * Transliterate from one script to another.
     *
     * @param data     the string to transliterate
     * @param from     the source script
     * @param to       the destination script
     * @param options  transliteration options
     * @return         the finished string
     */
// Version of t() that supplies null options.
    fun t(data: String, from: String, to: String, options: Options = Options()): String {
        var mutableData = data
        val cachedOptions = if (cache.options != null) cache.options else Options()
        var hasPriorState = cache.from == from && cache.to == to
        val map: TMap
        // Here we simultaneously build up an `options` object and compare
// these options to the options from the last run.

        if (options != cachedOptions) {
            hasPriorState = false
        }
        if (hasPriorState && cache.map != null) {
            map = cache.map!!
        } else {
            map = makeMap(from, to, options)
            cache.from = from
            cache.map = map
            cache.options = options
            cache.to = to
        }
        // Easy way out for "{\m+}", "\", and ".h".
        if (from == "itrans") {
            mutableData = mutableData.replace("\\{\\\\m\\+\\}".toRegex(), ".h.N")
            mutableData = mutableData.replace("\\.h".toRegex(), "")
            mutableData = mutableData.replace("\\\\([^'`_]|$)".toRegex(), "##$1##")
        }
        return if (map.fromRoman) {
            transliterateRoman(mutableData, map, options)
        } else {
            transliterateBrahmic(mutableData, map, options)
        }
    }

    init {
        initializeSchemes()
        initializeAlternates()
        initializeSpecialSchemes()
    }
}

private fun StringBuilder.delete(start: Int, end: Int): StringBuilder {
    return StringBuilder(this.subSequence(start, end))
}
