package com.mynh.studentform.utils

import android.content.res.Resources
import com.mynh.studentform.R
import org.json.JSONObject
import java.io.InputStreamReader

class AddressHelper(resources: Resources) {
    private val data: JSONObject

    init {
        // read json from raw file
        val inputStream = resources.openRawResource(R.raw.data)
        val reader = InputStreamReader(inputStream)
        val content = reader.readText()
        reader.close()

        data = JSONObject(content)
    }

    fun getProvinces(): List<String> {
        val list = mutableListOf<String>()
        val keys = data.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val province = data.getJSONObject(key).getString("name")
            list.add(province)
        }
        return list.sorted()
    }

    fun getDistricts(province: String): List<String> {
        val list = mutableListOf<String>()
        val provinceKeys = data.keys()

        // find province
        var selectedProvince: JSONObject? = null
        while (provinceKeys.hasNext()) {
            val key = provinceKeys.next()
            if (data.getJSONObject(key).getString("name") == province) {
                selectedProvince = data.getJSONObject(key)
                break
            }
        }

        // get list of districts
        selectedProvince?.let { provinceObj ->
            val districts = provinceObj.getJSONObject("quan-huyen")
            val districtKeys = districts.keys()
            while (districtKeys.hasNext()) {
                val key = districtKeys.next()
                val district = districts.getJSONObject(key).getString("name")
                list.add(district)
            }
        }

        return list.sorted() // sort alphabetically
    }

    fun getWards(province: String, district: String): List<String> {
        val list = mutableListOf<String>()
        var selectedProvince: JSONObject? = null
        var selectedDistrict: JSONObject? = null

        // find province
        val provinceKeys = data.keys()
        while (provinceKeys.hasNext()) {
            val key = provinceKeys.next()
            if (data.getJSONObject(key).getString("name") == province) {
                selectedProvince = data.getJSONObject(key)
                break
            }
        }

        // find district
        selectedProvince?.let { provinceObj ->
            val districts = provinceObj.getJSONObject("quan-huyen")
            val districtKeys = districts.keys()
            while (districtKeys.hasNext()) {
                val key = districtKeys.next()
                if (districts.getJSONObject(key).getString("name") == district) {
                    selectedDistrict = districts.getJSONObject(key)
                    break
                }
            }
        }

        // get list of districts
        selectedDistrict?.let { districtObj ->
            val wards = districtObj.getJSONObject("xa-phuong")
            val wardKeys = wards.keys()
            while (wardKeys.hasNext()) {
                val key = wardKeys.next()
                val ward = wards.getJSONObject(key).getString("name")
                list.add(ward)
            }
        }

        return list.sorted() // sort alphabetically
    }
}