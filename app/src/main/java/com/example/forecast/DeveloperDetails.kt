package com.example.forecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_developer_details.*
import android.content.Intent
import android.net.Uri
import android.widget.Toast


class DeveloperDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_details)

        linkedInLogo.setOnClickListener{
            Toast.makeText(this,"Opening LinkedIn Profile", Toast.LENGTH_LONG).show()
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/arin-verma-a600051b3/"))
            startActivity(browserIntent)

            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        }

        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
        }
    }
}