package com.mynh.simplelist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var editTextNumber: EditText
    private lateinit var radioGroupNumbers: RadioGroup
    private lateinit var radioButtonEven: RadioButton
    private lateinit var radioButtonOdd: RadioButton
    private lateinit var radioButtonSquare: RadioButton
    private lateinit var buttonShow: Button
    private lateinit var listViewNumbers: ListView
    private lateinit var textViewError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextNumber = findViewById(R.id.editTextNumber)
        radioGroupNumbers = findViewById(R.id.radioGroupNumbers)
        radioButtonEven = findViewById(R.id.radioButtonEven)
        radioButtonOdd = findViewById(R.id.radioButtonOdd)
        radioButtonSquare = findViewById(R.id.radioButtonSquare)
        buttonShow = findViewById(R.id.buttonShow)
        listViewNumbers = findViewById(R.id.listViewNumbers)
        textViewError = findViewById(R.id.textViewError)

        buttonShow.setOnClickListener {
            showNumbers()
        }
    }

    private fun showNumbers() {
        textViewError.text = ""
        val input = editTextNumber.text.toString()

        if (input.isEmpty()) {
            textViewError.text = "Vui lòng nhập số!"
            return
        }

        val n = input.toIntOrNull()
        if (n == null) {
            textViewError.text = "Vui lòng nhập số hợp lệ!"
            return
        }

        if (n < 0) {
            textViewError.text = "Vui lòng nhập số nguyên dương!"
            return
        }

        val numbers = when {
            radioButtonEven.isChecked -> getEvenNumbers(n)
            radioButtonOdd.isChecked -> getOddNumbers(n)
            radioButtonSquare.isChecked -> getSquareNumbers(n)
            else -> {
                textViewError.text = "Vui lòng chọn loại số!"
                return
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, numbers)
        listViewNumbers.adapter = adapter
    }

    private fun getEvenNumbers(n: Int): List<Int> {
        return (0..n).filter { it % 2 == 0 }
    }

    private fun getOddNumbers(n: Int): List<Int> {
        return (1..n).filter { it % 2 != 0 }
    }

    private fun getSquareNumbers(n: Int): List<Int> {
        return (0..n).filter {
            val sqrt = sqrt(it.toDouble()).toInt()
            sqrt * sqrt == it
        }
    }
}