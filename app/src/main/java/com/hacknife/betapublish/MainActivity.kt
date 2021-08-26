package com.hacknife.betapublish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hacknife.install.Install

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Install.init(this, "9be8e7b8347f764cec05b3aeb6838635", "8a1f0875bec2c943b63db071c672f763", "1.0.0")
        Install.install()
    }
}