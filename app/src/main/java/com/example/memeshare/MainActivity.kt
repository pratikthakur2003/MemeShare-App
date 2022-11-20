package com.example.memeshare

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    var imageUrl : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
        val wrong = somethingWentWrong
        val btn = modeSwitch
        nightImage.visibility = View.GONE


        btn.setOnCheckedChangeListener { _, isChecked ->


            if (btn.isChecked) {
                nightImage.visibility = View.VISIBLE
                wrong.setTextColor(Color.parseColor("#FFFFFF"))

            } else {
                nightImage.visibility = View.GONE
                wrong.setTextColor(Color.parseColor("#000000"))
            }
        }
    }

    private fun loadMeme(){
        progressBar.visibility = View.VISIBLE
        somethingWentWrong.visibility = View.GONE

        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val req = JsonObjectRequest(Request.Method.GET , url , null,
            { response -> imageUrl = response.getString("url")
                Glide.with(this).load(imageUrl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                       progressBar.visibility = View.GONE
                        return false
                    }
                }).into(memeImage)
            },
            {
                Toast.makeText(this, "Connect to Internet", Toast.LENGTH_LONG).show()
                somethingWentWrong.visibility = View.VISIBLE

    })

        // Add the request to the RequestQueue.
        queue.add(req)
    }

    fun shareClick(view: View) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT , "Check out this Meme : \n$imageUrl")
        val chooser = Intent.createChooser(intent, "Share the meme using : ")
        startActivity(chooser)


    }

    fun nextClick(view: View) {
        loadMeme()

    }

}
