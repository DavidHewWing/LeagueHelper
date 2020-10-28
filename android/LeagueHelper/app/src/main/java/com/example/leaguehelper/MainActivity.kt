package com.example.leaguehelper


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.leaguehelper.api.APIController
import com.example.leaguehelper.api.ServiceVolley
import com.example.leaguehelper.util.DataFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    var championData: JSONObject? = null
    var summonerSpellData: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar_main.title = "LeagueHelper"
        toolbar_main.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar_main.setNavigationOnClickListener {
            val gameFragment = supportFragmentManager.findFragmentByTag("GAME")
            if (gameFragment != null){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SearchFragment(), "SEARCH")
                    .commitAllowingStateLoss()
            }
        }

        val championJSONUrl = "http://ddragon.leagueoflegends.com/cdn/10.21.1/data/en_US/champion.json"
        val summonerSpellJSONUrl = "http://ddragon.leagueoflegends.com/cdn/10.21.1/data/en_US/summoner.json"

        val service = ServiceVolley()
        val apiController = APIController(service)

        apiController.getJSON(championJSONUrl) { response ->
            championData = response?.get("data") as JSONObject
            DataFactory.initChampMap(championData)
        }
        apiController.getJSON(summonerSpellJSONUrl) {response ->
            summonerSpellData = response?.get("data") as JSONObject
            DataFactory.initSpellMap(summonerSpellData)
            fragment_container.visibility = View.VISIBLE
            progress_bar.visibility = View.GONE
        }

        val container = findViewById<FrameLayout>(R.id.fragment_container)
        if (container != null) {
            if (savedInstanceState != null) {
                return
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment(), "SEARCH")
                .commitAllowingStateLoss()
        }
    }
}