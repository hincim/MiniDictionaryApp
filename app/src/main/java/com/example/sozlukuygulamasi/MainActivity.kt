package com.example.sozlukuygulamasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.sqlitekullanimihazirveritabani.DatabaseCopyHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var kelimelerListe:ArrayList<Kelimeler>
    private lateinit var adapter: KelimelerAdapter
    private lateinit var vt: VeriTabaniYardimcisi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = "Sözlük Uygulaması"
        setSupportActionBar(toolbar)
        vt = VeriTabaniYardimcisi(this)
        veriTabaniKopyala()
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        kelimelerListe = Kelimelerdao().tumKelimeler(vt)
        adapter = KelimelerAdapter(this,kelimelerListe)
        rv.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        val item = menu?.findItem(R.id.action_ara)
        val searchView = item?.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        arama(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        arama(newText)
        return true
    }

    fun veriTabaniKopyala(){
        val copyHelper = DatabaseCopyHelper(this)
        try {
            copyHelper.createDataBase()
            copyHelper.openDataBase()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun arama(aramaKelime:String){
        kelimelerListe = Kelimelerdao().aramaYap(vt,aramaKelime)
        adapter = KelimelerAdapter(this,kelimelerListe)
        rv.adapter = adapter
    }

}