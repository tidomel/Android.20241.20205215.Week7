package com.mynh.studentform

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import java.util.Calendar
import com.mynh.studentform.databinding.ActivityMainBinding
import com.mynh.studentform.utils.AddressHelper
import android.transition.TransitionManager
import android.view.ViewGroup


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedDate: String = ""
    private lateinit var addressHelper: AddressHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addressHelper = AddressHelper(resources)

        setupSpinners()
        setupCalendar()
        setupSubmitButton()
    }

    private fun setupSpinners() {
        // Thiết lập Spinner Tỉnh/Thành
        val provinces = mutableListOf("Chọn Tỉnh/Thành")
        provinces.addAll(addressHelper.getProvinces())
        val provinceAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            provinces
        )
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTinh.adapter = provinceAdapter

        // Thiết lập Spinner Quận/Huyện
        binding.spinnerTinh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val selectedProvince = provinces[position]
                    val districts = mutableListOf("Chọn Quận/Huyện")
                    districts.addAll(addressHelper.getDistricts(selectedProvince))

                    val districtAdapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        districts
                    )
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerQuan.adapter = districtAdapter
                } else {
                    binding.spinnerQuan.adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Chọn Quận/Huyện")
                    )
                    binding.spinnerPhuong.adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Chọn Phường/Xã")
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Thiết lập Spinner Phường/Xã
        binding.spinnerQuan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val selectedProvince = provinces[binding.spinnerTinh.selectedItemPosition]
                    val selectedDistrict = binding.spinnerQuan.selectedItem.toString()

                    val wards = mutableListOf("Chọn Phường/Xã")
                    wards.addAll(addressHelper.getWards(selectedProvince, selectedDistrict))

                    val wardAdapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        wards
                    )
                    wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerPhuong.adapter = wardAdapter
                } else {
                    binding.spinnerPhuong.adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Chọn Phường/Xã")
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
//    private fun setupCalendar() {
//        binding.calendarView.visibility = View.GONE
//
//        binding.calendarView.maxDate = System.currentTimeMillis()
//
//        val calendar = Calendar.getInstance()
//        calendar.set(1900, 0, 1)
//        binding.calendarView.minDate = calendar.timeInMillis
//
//        binding.btnShowCalendar.setOnClickListener {
//            if (binding.calendarView.visibility == View.VISIBLE) {
//                binding.calendarView.visibility = View.GONE
//            } else {
//                binding.calendarView.visibility = View.VISIBLE
//            }
//        }
//
//        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
//            val monthStr = if (month + 1 < 10) "0${month + 1}" else (month + 1).toString()
//
//            selectedDate = "$dayStr/$monthStr/$year"
//            binding.btnShowCalendar.text = "Ngày sinh: $selectedDate"
//            binding.calendarView.visibility = View.GONE
//        }
//    }

    private fun setupCalendar() {
        Log.d("Calendar", "Setting up calendar")

        binding.calendarView.visibility = View.GONE

        binding.btnShowCalendar.setOnClickListener {
            Log.d("Calendar", "Button clicked")

            if (binding.calendarView.visibility == View.VISIBLE) {
                Log.d("Calendar", "Hiding calendar")
                binding.calendarView.visibility = View.GONE
            } else {
                Log.d("Calendar", "Showing calendar")
                binding.calendarView.visibility = View.VISIBLE
            }
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
            val monthStr = if (month + 1 < 10) "0${month + 1}" else (month + 1).toString()

            selectedDate = "$dayStr/$monthStr/$year"
            binding.btnShowCalendar.text = "Ngày sinh: $selectedDate"
            binding.calendarView.visibility = View.GONE

            Log.d("Calendar", "Date selected: $selectedDate")
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Kiểm tra MSSV
        val mssvPattern = "^\\d{8}$"
        val mssv = binding.edtMSSV.text.toString().trim()

        if (mssv.isEmpty()) {
            binding.edtMSSV.error = "Vui lòng nhập MSSV"
            isValid = false
        } else if (!mssv.matches(mssvPattern.toRegex())) {
            binding.edtMSSV.error = "MSSV phải có đúng 8 chữ số"
            isValid = false
        } else {
            val year = mssv.substring(0, 4).toInt()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            if (year < 2000 || year > currentYear) {
                binding.edtMSSV.error = "Năm trong MSSV không hợp lệ"
                isValid = false
            }
        }

        // Kiểm tra Họ tên
        if (binding.edtHoTen.text.toString().trim().isEmpty()) {
            binding.edtHoTen.error = "Vui lòng nhập họ tên"
            isValid = false
        }

        // Kiểm tra Giới tính
        if (!binding.rbNam.isChecked && !binding.rbNu.isChecked) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Kiểm tra Email
        val email = binding.edtEmail.text.toString().trim()
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Email không hợp lệ"
            isValid = false
        }

        // Kiểm tra Số điện thoại
        val phone = binding.edtPhone.text.toString().trim()
        if (phone.isEmpty() || phone.length < 10) {
            binding.edtPhone.error = "Số điện thoại không hợp lệ"
            isValid = false
        }

        // Kiểm tra Ngày sinh
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Kiểm tra địa chỉ
        if (binding.spinnerTinh.selectedItemPosition == 0 ||
            binding.spinnerQuan.selectedItemPosition == 0 ||
            binding.spinnerPhuong.selectedItemPosition == 0
        ) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ địa chỉ", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Kiểm tra sở thích
        if (!binding.cbTheThao.isChecked && !binding.cbDienAnh.isChecked && !binding.cbAmNhac.isChecked) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một sở thích", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Kiểm tra đồng ý điều khoản
        if (!binding.cbDieuKhoan.isChecked) {
            Toast.makeText(this, "Vui lòng đồng ý với điều khoản", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }
}