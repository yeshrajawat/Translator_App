package com.university.translatetextapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*

class MainActivity2 : AppCompatActivity() {
    lateinit var mTTS:TextToSpeech
    lateinit var language:String
    var languageFrom = TranslateLanguage.ENGLISH
    lateinit var languages:Array<String>
    var languageTo = TranslateLanguage.GERMAN
    lateinit var translator: Translator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        languages = resources.getStringArray(R.array.language)
        val adapter = ArrayAdapter(this, R.layout.drop_down_item, languages)
        autoCompleteTextFrom.setAdapter(adapter)
        autoCompleteTextTo.setAdapter(adapter)

    autoCompleteTextFrom.setOnItemClickListener(object :AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){
                0->{
                    languageFrom = TranslateLanguage.ENGLISH
                }1->{
                    languageFrom = TranslateLanguage.GERMAN
                }2->{
                    languageFrom = TranslateLanguage.SPANISH
                }3->{
                    languageFrom = TranslateLanguage.HINDI
                }4->{
                    languageFrom = TranslateLanguage.RUSSIAN
                }5->{
                    languageFrom = TranslateLanguage.KOREAN
                }6->{
                    languageFrom = TranslateLanguage.CHINESE
                }
                else->{
                    languageFrom = TranslateLanguage.ENGLISH
                }
            }
            Log.e("TAG",languageFrom)


        }

    })
        autoCompleteTextTo.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->{
                        languageTo = TranslateLanguage.ENGLISH
                    }1->{
                    languageTo = TranslateLanguage.GERMAN
                }2->{
                    languageTo = TranslateLanguage.SPANISH
                }3->{
                    languageTo = TranslateLanguage.HINDI
                }4->{
                    languageTo = TranslateLanguage.RUSSIAN
                }5->{
                    languageTo = TranslateLanguage.KOREAN
                }6->{
                    languageTo = TranslateLanguage.CHINESE
                }
                    else->{
                        languageTo = TranslateLanguage.ENGLISH
                    }
                }
                Log.e("TAG",languageTo)



            }

        })



        translateIcon.setOnClickListener {


            val inputText:String = textFrom.editableText.toString()
            if(!inputText.equals(""))
            {
                Toast.makeText(this,"Yas"+languageTo,Toast.LENGTH_LONG).show()
                downloadModel(inputText)

            }
           else{
                Toast.makeText(this,"No Input entered", Toast.LENGTH_SHORT).show()
            }
        }
        audioIcon.setOnClickListener {
            TextToSpeech()
        }

    }
    private fun downloadModel(input:String) {
        val option = TranslatorOptions.Builder()
            .setSourceLanguage(languageFrom)
            .setTargetLanguage(languageTo)
            .build()
        translator = Translation.getClient(option)
        val conditions = DownloadConditions.Builder().requireWifi().build()


        translator.downloadModelIfNeeded(conditions).addOnSuccessListener {
            Toast.makeText(this,"Successfully Downloaded "+languageTo, Toast.LENGTH_SHORT).show()
            translate(input)

            Log.e("TAG",input)
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to Download Model", Toast.LENGTH_SHORT).show()
        }
    }

    private fun translate(input:String)
    {
        translator.translate(input).addOnSuccessListener {
            textTo.setText(it)
            Log.e("TAG",it)
        }.addOnFailureListener {
            Toast.makeText(this,"Error Translating Text", Toast.LENGTH_SHORT).show()

        }
    }



    private fun TextToSpeech(){
        mTTS = TextToSpeech(this,object :TextToSpeech.OnInitListener{
            override fun onInit(status: Int) {
                if(status ==TextToSpeech.SUCCESS)
                {
                    val languageNow = ttsLanguage(languageTo)
                    var result = mTTS?.setLanguage(languageNow as Locale?)
                    Log.e("TAG",languageNow.toString()+" "+languageTo)
                    if(languageNow == Locale.CANADA && result == TextToSpeech.LANG_MISSING_DATA or TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e("TTS","Language not supported")
                    }
                    else if(languageNow== Locale.CANADA)
                    {
                        Toast.makeText(this@MainActivity2,"Language Not Supported",Toast.LENGTH_LONG).show()
                    }
                    else if(result== TextToSpeech.SUCCESS && languageNow!=Locale.CANADA)
                    {
                        mTTS.speak(textTo.editableText.toString(),TextToSpeech.QUEUE_FLUSH,null)

                    }

                }
            }

        })
    }
    private fun ttsLanguage(language:String):Any
    {
        if(language.equals("en"))
        {
            return Locale.ENGLISH
        }
        else if(language.equals("de"))
        {
            return Locale.GERMAN
        }
        else if(language.equals("zh"))
        {
            return Locale.CHINESE
        }
        else if(language.equals("ko"))
        {
            return Locale.KOREAN
        }
        else{
            return Locale.CANADA
        }
    }
}