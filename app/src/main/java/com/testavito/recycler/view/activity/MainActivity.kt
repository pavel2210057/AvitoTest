package com.testavito.recycler.view.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testavito.recycler.R
import com.testavito.recycler.view.recycler.adapter.RecyclerAdapter
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private companion object Extras {
        const val RecyclerAdapterDataExtra = "com.testavito.recycler.RECYCLER_ADAPTER_DATA"
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onResume() {
        super.onResume()

        this.recyclerView.adapter = intent?.extras?.getSerializable(RecyclerAdapterDataExtra)
                as? RecyclerAdapter ?: this.recyclerView.adapter
        (this.recyclerView.layoutManager as GridLayoutManager).spanCount =
            when(resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> 4
                Configuration.ORIENTATION_PORTRAIT -> 2
                else -> 0
            }
    }

    private fun init() {
        val recyclerViewAdapter = RecyclerAdapter()

        this.recyclerView = findViewById<RecyclerView>(R.id.recyclerView)?.apply {
            adapter = recyclerViewAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 1)
        } ?: throw RuntimeException("RecyclerView doesn't exist")

        timer(period = 5000) {
            runOnUiThread(recyclerViewAdapter::addItem)
        }
    }

    override fun onStop() {
        super.onStop()
        intent.putExtras(Bundle().apply {
            putSerializable(RecyclerAdapterDataExtra, recyclerView.adapter as RecyclerAdapter)
        })
    }
}