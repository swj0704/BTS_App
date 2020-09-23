package com.example.bluetoothtemperaturesubmitter.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView

import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.bluetoothtemperaturesubmitter.API.RetrofitHelper
import com.example.bluetoothtemperaturesubmitter.DTO.Temperature
import com.example.bluetoothtemperaturesubmitter.R
import kotlinx.android.synthetic.main.activity_user_temperature_log.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class User_temperature_log : AppCompatActivity() {

    var tempData = ArrayList<Temperature>()
    private val tempAPI = RetrofitHelper().getTemperatureAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_temperature_log)
        tempAPI.getMyTemp(intent.getStringExtra("token")).enqueue(object : Callback<List<Temperature>>{
            override fun onFailure(call: Call<List<Temperature>>, t: Throwable) {
                Toast.makeText(this@User_temperature_log, "오류 발생 : $t", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<Temperature>>,
                response: Response<List<Temperature>>
            ) {
                if(response.isSuccessful){
                    if(response.code() == 200){
                        tempData = (response.body() as ArrayList<Temperature>?)!!
                    }
                }
            }

        })
        val menu = ArrayList<String>()
        menu.add("전체")
        menu.add("이상 수치만")
        menu.add("최근 내역 10개")
        val adapter = ArrayAdapter<String>(this@User_temperature_log, android.R.layout.simple_spinner_dropdown_item,menu)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinner.setSelection(0)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    when (position) {
                        0 -> {
                            selectAll()
                        }
                        1 -> {
                            selectHighTemp()
                        }
                        2 -> {
                            selectTen()
                        }
                    }
                }
            }

        }

    }
    private fun selectAll(){
        temperature_item.adapter = UserAdapter(this, tempData)
    }
    private fun selectTen(){
        var dataList = ArrayList<Temperature>()
        var i = 0
        while(i < 10){
            if(tempData.size < 10){
                dataList = tempData
                break
            }
            dataList.add(tempData[i])
            i++
        }
        temperature_item.adapter = UserAdapter(this, dataList)
    }
    private fun selectHighTemp(){
        val dataList = ArrayList<Temperature>()
        for(t in tempData){
            if(t.value >= 37.5 || t.value <= 35.5){
                dataList.add(t)
            }
        }
        temperature_item.adapter = UserAdapter(this, dataList)
    }
}