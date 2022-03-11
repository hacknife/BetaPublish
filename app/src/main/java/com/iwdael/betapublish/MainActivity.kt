package com.iwdael.betapublish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iwdael.betapublish.R
import com.iwdael.install.Install

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Install.init("9be8e7b8347f764cec05b3aeb6838635", "c1aecf55db190fee734064c807b250b9", "1.0.0", "123456")
        Install.install(this)
    }
}