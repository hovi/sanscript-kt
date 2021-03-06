package com.wellebee.sanskrit

import org.junit.Assert
import java.util.*

open class SanscriptTest internal constructor() {
    protected var sanscript = Sanscript()

    protected inner class DataSet : HashMap<String?, String?>()
    protected inner class DataSets : HashMap<String?, DataSet?>()

    protected var dataSets = DataSets()
    fun initializeDataSets() {
        var dataSet: DataSet
        dataSet = DataSet()
        dataSet["vowels"] = "অ আ ই ঈ উ ঊ ঋ ৠ ঌ ৡ এ ঐ ও ঔ"
        dataSet["marks"] = "ক খা গি ঘী ঙু চূ ছৃ জৄ ঝৢ ঞৣ টে ঠৈ ডো ঢৌ ণং তঃ থ্"
        dataSet["consonants"] = "ক খ গ ঘ ঙ চ ছ জ ঝ ঞ ট ঠ ড ঢ ণ ত থ দ ধ ন প ফ ব ভ ম"
        dataSet["other"] = "য র ল ব শ ষ স হ ळ"
        dataSet["symbols"] = "ॐ । ॥ ০ ১ ২ ৩ ৪ ৫ ৬ ৭ ৮ ৯"
        dataSet["putra"] = "পুত্র"
        dataSet["naraIti"] = "নর ইতি"
        dataSet["sentence"] = "ধর্মক্ষেত্রে কুরুক্ষেত্রে সমবেতা যুযুত্সবঃ ।"
        dataSets["bengali"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "अ आ इ ई उ ऊ ऋ ॠ ऌ ॡ ए ऐ ओ औ"
        dataSet["short_vowels"] = "ऎ ए ऒ ओ"
        dataSet["marks"] = "क खा गि घी ङु चू छृ जॄ झॢ ञॣ टे ठै डो ढौ णं तः थ्"
        dataSet["short_marks"] = "कॆ के कॊ को"
        dataSet["consonants"] = "क ख ग घ ङ च छ ज झ ञ ट ठ ड ढ ण त थ द ध न प फ ब भ म"
        dataSet["other"] = "य र ल व श ष स ह ळ"
        dataSet["symbols"] = "ॐ । ॥ ० १ २ ३ ४ ५ ६ ७ ८ ९"
        dataSet["putra"] = "पुत्र"
        dataSet["naraIti"] = "नर इति"
        dataSet["sentence"] = "धर्मक्षेत्रे कुरुक्षेत्रे समवेता युयुत्सवः ।"
        dataSets["devanagari"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "અ આ ઇ ઈ ઉ ઊ ઋ ૠ ઌ ૡ એ ઐ ઓ ઔ"
        dataSet["marks"] = "ક ખા ગિ ઘી ઙુ ચૂ છૃ જૄ ઝૢ ઞૣ ટે ઠૈ ડો ઢૌ ણં તઃ થ્"
        dataSet["consonants"] = "ક ખ ગ ઘ ઙ ચ છ જ ઝ ઞ ટ ઠ ડ ઢ ણ ત થ દ ધ ન પ ફ બ ભ મ"
        dataSet["other"] = "ય ર લ વ શ ષ સ હ ળ"
        dataSet["symbols"] = "ૐ ૤ ૥ ૦ ૧ ૨ ૩ ૪ ૫ ૬ ૭ ૮ ૯"
        dataSet["putra"] = "પુત્ર"
        dataSet["naraIti"] = "નર ઇતિ"
        dataSet["sentence"] = "ધર્મક્ષેત્રે કુરુક્ષેત્રે સમવેતા યુયુત્સવઃ ૤"
        dataSets["gujarati"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "ਅ ਆ ਇ ਈ ਉ ਊ ਏ ਐ ਓ ਔ"
        dataSet["marks"] = "ਕ ਖਾ ਗਿ ਘੀ ਙੁ ਚੂ ਟੇ ਠੈ ਡੋ ਢੌ ਣਂ ਤਃ ਥ੍"
        dataSet["consonants"] = "ਕ ਖ ਗ ਘ ਙ ਚ ਛ ਜ ਝ ਞ ਟ ਠ ਡ ਢ ਣ ਤ ਥ ਦ ਧ ਨ ਪ ਫ ਬ ਭ ਮ"
        dataSet["other"] = "ਯ ਰ ਲ ਵ ਸ਼ ਸ਼ ਸ ਹ ਲ਼"
        dataSet["symbols"] = "ॐ । ॥ ੦ ੧ ੨ ੩ ੪ ੫ ੬ ੭ ੮ ੯"
        dataSet["putra"] = "ਪੁਤ੍ਰ"
        dataSet["naraIti"] = "ਨਰ ਇਤਿ"
        dataSet["sentence"] = "ਧਰ੍ਮਕ੍ਸ਼ੇਤ੍ਰੇ ਕੁਰੁਕ੍ਸ਼ੇਤ੍ਰੇ ਸਮਵੇਤਾ ਯੁਯੁਤ੍ਸਵਃ ।"
        dataSets["gurmukhi"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "a A i I u U R RR lR lRR e ai o au"
        dataSet["marks"] = "ka khA gi ghI Gu cU chR jRR jhlR JlRR Te Thai Do Dhau NaM taH th"
        dataSet["consonants"] = "ka kha ga gha Ga ca cha ja jha Ja Ta Tha Da Dha Na ta tha da dha na pa pha ba bha ma"
        dataSet["other"] = "ya ra la va za Sa sa ha La"
        dataSet["symbols"] = "OM | || 0 1 2 3 4 5 6 7 8 9"
        dataSet["putra"] = "putra"
        dataSet["naraIti"] = "nara iti"
        dataSet["sentence"] = "dharmakSetre kurukSetre samavetA yuyutsavaH |"
        dataSets["hk"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "a ā i ī u ū ṛ ṝ ḷ ḹ e ai o au"
        dataSet["marks"] = "ka khā gi ghī ṅu cū chṛ jṝ jhḷ ñḹ ṭe ṭhai ḍo ḍhau ṇaṃ taḥ th"
        dataSet["consonants"] = "ka kha ga gha ṅa ca cha ja jha ña ṭa ṭha ḍa ḍha ṇa ta tha da dha na pa pha ba bha ma"
        dataSet["other"] = "ya ra la va śa ṣa sa ha ḻa"
        dataSet["symbols"] = "oṃ । ॥ 0 1 2 3 4 5 6 7 8 9"
        dataSet["putra"] = "putra"
        dataSet["naraIti"] = "nara iti"
        dataSet["sentence"] = "dharmakṣetre kurukṣetre samavetā yuyutsavaḥ ।"
        dataSets["iast"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "a A i I u U RRi RRI LLi LLI e ai o au"
        dataSet["marks"] = "ka khA gi ghI ~Nu chU ChRRi jRRI jhLLi ~nLLI Te Thai Do Dhau NaM taH th"
        dataSet["consonants"] =
            "ka kha ga gha ~Na cha Cha ja jha ~na Ta Tha Da Dha Na ta tha da dha na pa pha ba bha ma"
        dataSet["other"] = "ya ra la va sha Sha sa ha La"
        dataSet["symbols"] = "OM | || 0 1 2 3 4 5 6 7 8 9"
        dataSet["putra"] = "putra"
        dataSet["naraIti"] = "nara iti"
        dataSet["sentence"] = "dharmakShetre kurukShetre samavetA yuyutsavaH |"
        dataSets["itrans"] = dataSet
        dataSet = DataSet()
        dataSet["short_vowels"] = "e E o O"
        dataSet["short_marks"] = "ke kE ko kO"
        dataSets["itrans_dravidian"] = dataSet
        dataSet = DataSet()
        dataSet["short_vowels"] = "e ē o ō"
        dataSet["short_marks"] = "ke kē ko kō"
        dataSets["kolkata"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "ಅ ಆ ಇ ಈ ಉ ಊ ಋ ೠ ಌ ೡ ಏ ಐ ಓ ಔ"
        dataSet["short_vowels"] = "ಎ ಏ ಒ ಓ"
        dataSet["marks"] = "ಕ ಖಾ ಗಿ ಘೀ ಙು ಚೂ ಛೃ ಜೄ ಝೢ ಞೣ ಟೇ ಠೈ ಡೋ ಢೌ ಣಂ ತಃ ಥ್"
        dataSet["short_marks"] = "ಕೆ ಕೇ ಕೊ ಕೋ"
        dataSet["consonants"] = "ಕ ಖ ಗ ಘ ಙ ಚ ಛ ಜ ಝ ಞ ಟ ಠ ಡ ಢ ಣ ತ ಥ ದ ಧ ನ ಪ ಫ ಬ ಭ ಮ"
        dataSet["other"] = "ಯ ರ ಲ ವ ಶ ಷ ಸ ಹ ಳ"
        dataSet["symbols"] = "ಓಂ । ॥ ೦ ೧ ೨ ೩ ೪ ೫ ೬ ೭ ೮ ೯"
        dataSet["putra"] = "ಪುತ್ರ"
        dataSet["naraIti"] = "ನರ ಇತಿ"
        dataSet["sentence"] = "ಧರ್ಮಕ್ಷೇತ್ರೇ ಕುರುಕ್ಷೇತ್ರೇ ಸಮವೇತಾ ಯುಯುತ್ಸವಃ ।"
        dataSets["kannada"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "അ ആ ഇ ഈ ഉ ഊ ഋ ൠ ഌ ൡ ഏ ഐ ഓ ഔ"
        dataSet["short_vowels"] = "എ ഏ ഒ ഓ"
        dataSet["marks"] = "ക ഖാ ഗി ഘീ ങു ചൂ ഛൃ ജൄ ഝൢ ഞൣ ടേ ഠൈ ഡോ ഢൌ ണം തഃ ഥ്"
        dataSet["short_marks"] = "കെ കേ കൊ കോ"
        dataSet["consonants"] = "ക ഖ ഗ ഘ ങ ച ഛ ജ ഝ ഞ ട ഠ ഡ ഢ ണ ത ഥ ദ ധ ന പ ഫ ബ ഭ മ"
        dataSet["other"] = "യ ര ല വ ശ ഷ സ ഹ ള"
        dataSet["symbols"] = "ഓം । ॥ ൦ ൧ ൨ ൩ ൪ ൫ ൬ ൭ ൮ ൯"
        dataSet["putra"] = "പുത്ര"
        dataSet["naraIti"] = "നര ഇതി"
        dataSet["sentence"] = "ധര്മക്ഷേത്രേ കുരുക്ഷേത്രേ സമവേതാ യുയുത്സവഃ ।"
        dataSets["malayalam"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "ଅ ଆ ଇ ଈ ଉ ଊ ଋ ୠ ଌ ୡ ଏ ଐ ଓ ଔ"
        dataSet["marks"] = "କ ଖା ଗି ଘୀ ଙୁ ଚୂ ଛୃ ଜୄ ଟେ ଠୈ ଡୋ ଢୌ ଣଂ ତଃ ଥ୍"
        dataSet["consonants"] = "କ ଖ ଗ ଘ ଙ ଚ ଛ ଜ ଝ ଞ ଟ ଠ ଡ ଢ ଣ ତ ଥ ଦ ଧ ନ ପ ଫ ବ ଭ ମ"
        dataSet["other"] = "ଯ ର ଲ ଵ ଶ ଷ ସ ହ ଳ"
        dataSet["symbols"] = "ଓଂ । ॥ ୦ ୧ ୨ ୩ ୪ ୫ ୬ ୭ ୮ ୯"
        dataSet["putra"] = "ପୁତ୍ର"
        dataSet["naraIti"] = "ନର ଇତି"
        dataSet["sentence"] = "ଧର୍ମକ୍ଷେତ୍ରେ କୁରୁକ୍ଷେତ୍ରେ ସମଵେତା ଯୁଯୁତ୍ସଵଃ ।"
        dataSets["oriya"] = dataSet
        dataSet = DataSet()
        dataSet["short_vowels"] = "எ ஏ ஒ ஓ"
        dataSet["short_marks"] = "கெ கே கொ கோ"
        dataSets["tamil"] = dataSet
        dataSet = DataSet()
        dataSet["vowels"] = "అ ఆ ఇ ఈ ఉ ఊ ఋ ౠ ఌ ౡ ఏ ఐ ఓ ఔ"
        dataSet["short_vowels"] = "ఎ ఏ ఒ ఓ"
        dataSet["marks"] = "క ఖా గి ఘీ ఙు చూ ఛృ జౄ ఝౢ ఞౣ టే ఠై డో ఢౌ ణం తః థ్"
        dataSet["short_marks"] = "కె కే కొ కో"
        dataSet["consonants"] = "క ఖ గ ఘ ఙ చ ఛ జ ఝ ఞ ట ఠ డ ఢ ణ త థ ద ధ న ప ఫ బ భ మ"
        dataSet["other"] = "య ర ల వ శ ష స హ ళ"
        dataSet["symbols"] = "ఓం । ॥ ౦ ౧ ౨ ౩ ౪ ౫ ౬ ౭ ౮ ౯"
        dataSet["putra"] = "పుత్ర"
        dataSet["naraIti"] = "నర ఇతి"
        dataSet["sentence"] = "ధర్మక్షేత్రే కురుక్షేత్రే సమవేతా యుయుత్సవః ।"
        dataSets["telugu"] = dataSet
        dataSet = DataSet()
        dataSet["consonants"] = "ka Ka ga Ga fa ca Ca ja Ja Fa ta Ta da Da Na wa Wa xa Xa na pa Pa ba Ba ma"
        dataSet["symbols"] = "oM | || 0 1 2 3 4 5 6 7 8 9"
        dataSet["putra"] = "puwra"
        dataSet["naraIti"] = "nara iwi"
        dataSet["sentence"] = "XarmakRewre kurukRewre samavewA yuyuwsavaH |"
        dataSets["wx"] = dataSet
    }

    /**
     * The functional interface for a test runner.
     */
    protected interface TransHelper {
        fun run(input: String?, output: String?, description: String?)
    }

    /**
     * For a script pair (f, t), return a function that takes two strings s1 and
     * s2 and asserts that s1, when transliterated from f to t, equals s2. The
     * returned function takes an optional 'description' parameter for JUnit.
     *
     * @param from      the source script
     * @param to        the destination script
     * @param options   transliteration options
     * @return          the function described above.
     */
// Version of transHelper() that supplies null options.
    protected fun transHelper(
        from: String?,
        to: String?,
        options: Sanscript.Options = Sanscript.Options()
    ): TransHelper {
        return object : TransHelper {
            override fun run(input: String?, output: String?, description: String?) {
                Assert.assertEquals(
                    description, output, sanscript.t(
                        input!!,
                        from!!,
                        to!!,
                        options
                    )
                )
            }
        }
    }

    init {
        initializeDataSets()
    }
}