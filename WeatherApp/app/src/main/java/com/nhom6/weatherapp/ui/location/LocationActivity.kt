package com.nhom6.weatherapp.ui.location

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.nhom6.weatherapp.R
import com.nhom6.weatherapp.ui.search.SearchActivity

class LocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)

        // Sửa lại đoạn này để lấy EditText từ TextInputLayout
        val inputSearch = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputSearch).editText
        val header = findViewById<View>(R.id.headerLayout)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnCancel.visibility = View.GONE // ban đầu ẩn nút Huỷ

        inputSearch?.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                header.visibility = View.GONE
//                btnCancel.visibility = View.VISIBLE
//            }
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        btnCancel.setOnClickListener {
            inputSearch?.clearFocus()
            inputSearch?.text?.clear()
            header.visibility = View.VISIBLE
            btnCancel.visibility = View.GONE
        }
    }
}
