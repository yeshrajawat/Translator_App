package com.university.translatetextapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

   lateinit var translator:Translator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val option = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.GERMAN)
            .build()

        translator = Translation.getClient(option)

        idBtnTranslateLanguage.setOnClickListener {

            val inputText:String = idEdtLanguage.editableText.toString()
            Log.e("TAG",inputText)
            downloadModel(inputText)
        }


    }
    private fun downloadModel(input:String) {
        val conditions = DownloadConditions.Builder().requireWifi().build()


        translator.downloadModelIfNeeded(conditions).addOnSuccessListener {
            translate(input)
            Log.e("TAG",input)
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to Download Model",Toast.LENGTH_SHORT).show()
        }
    }

        private fun translate(input:String)
        {
            translator.translate(input).addOnSuccessListener {
                idTVTranslatedLanguage.text = it
                Log.e("TAG",it)
            }.addOnFailureListener {
                Toast.makeText(this,"Error Translating Text",Toast.LENGTH_SHORT).show()

            }
        }


}