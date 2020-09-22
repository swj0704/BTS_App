package com.example.bluetoothtemperaturesubmitter.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bluetoothtemperaturesubmitter.API.RetrofitHelper
import com.example.bluetoothtemperaturesubmitter.DTO.Groups
import com.example.bluetoothtemperaturesubmitter.R
import com.example.bluetoothtemperaturesubmitter.group.GroupListAdapter
import com.example.bluetoothtemperaturesubmitter.group.Group_create
import com.example.bluetoothtemperaturesubmitter.group.Group_join
import kotlinx.android.synthetic.main.fragment_group.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)

        val token : String? = arguments!!.getString("token")
        val pk = arguments?.getInt("pk")
        // Inflate the layout for this fragment
        view.group_create.setOnClickListener {
            if(activity != null) {
                val intent = Intent(activity, Group_create::class.java)
                intent.putExtra("token",token)
                intent.putExtra("pk", pk)
                activity!!.startActivity(intent)
                activity!!.finish()
            } else {
                Log.d("ERROR", "ERROR")
            }
        }
        view.group_join.setOnClickListener {
            if (activity != null){
                val intent = Intent(activity, Group_join::class.java)
                activity!!.startActivity(intent)
                activity!!.finish()
            }else {
                Log.d("ERROR", "ERROR")
            }
        }
        if (token != null) {

            var arrayList = ArrayList<Groups>()
            var arrayList1 = ArrayList<Groups>()

            RetrofitHelper().getGroupAPI().getMyGroup(token).enqueue(object : Callback<List<Groups>>{
                override fun onResponse(call: Call<List<Groups>>, response: Response<List<Groups>>) {
                    arrayList1 = (response.body() as ArrayList<Groups>?)!!

                    view.group_listview_member.adapter = GroupListAdapter(activity!!.applicationContext,arrayList1)
                }

                override fun onFailure(call: Call<List<Groups>>, t: Throwable) {
                    Log.d("ERROR", t.toString())
                }

            })

            RetrofitHelper().getGroupAPI().getGroup(token).enqueue(object : Callback<List<Groups>>{
                override fun onResponse(call: Call<List<Groups>>, response: Response<List<Groups>>) {
                    arrayList = (response.body() as ArrayList<Groups>?)!!

                    view.group_listview.adapter = GroupListAdapter(activity!!.applicationContext,arrayList)
                }

                override fun onFailure(call: Call<List<Groups>>, t: Throwable) {
                    Log.d("ERROR", t.toString())
                }


            })
        }




        return view
    }

}