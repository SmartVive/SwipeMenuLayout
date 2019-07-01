package com.example.swipemenulayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    fun initView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        recyclerView.adapter = MainAdapter()
    }

    class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>(){
        val list:MutableList<String> = arrayListOf()

        init {
            for (i in 0 until 100){
                list.add("$i")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return ViewHolder(inflate)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = "Item：${list[position]}"

            holder.tvDelete.setOnClickListener {
                list.removeAt(holder.layoutPosition)
                notifyItemRemoved(holder.layoutPosition)
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
           val textView: TextView = itemView.findViewById(R.id.textView)
            val tvDelete : TextView = itemView.findViewById(R.id.tvDelete)
        }

    }
}
