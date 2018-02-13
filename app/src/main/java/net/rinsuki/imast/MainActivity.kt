package net.rinsuki.imast

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import net.rinsuki.imast.mastodon.MastodonInstance
import net.rinsuki.imast.mastodon.MastodonService
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.authStartButton).setOnClickListener {
            val authType = findViewById<RadioGroup>(R.id.radioGroup).checkedRadioButtonId
            val instanceHostName = instanceEditText.text.toString()
            when (authType) {
                R.id.radioBrowserAuth -> {
                    val job = launch(UI) {
                        val moshi = Moshi.Builder()
                                .add(KotlinJsonAdapterFactory())
                                .build()
                        val client = Retrofit.Builder()
                                .baseUrl("https://"+instanceHostName+"/")
                                .addConverterFactory(MoshiConverterFactory.create(moshi))
                                .build()
                        val service = client.create(MastodonService::class.java)
                        val appInfo = async(CommonPool) {
                            service.createApp("iMast for Android", "imast://callback").execute()
                        }.await().body()
                        if (appInfo != null) {
                            val url = appInfo.getAuthorizeUrl(instanceHostName)
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}
