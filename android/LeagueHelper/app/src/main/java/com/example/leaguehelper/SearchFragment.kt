package com.example.leaguehelper

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.leaguehelper.api.APIController
import com.example.leaguehelper.api.ServiceVolley
import com.example.leaguehelper.model.Summoner
import com.example.leaguehelper.util.DataFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import org.json.JSONObject

class SearchFragment : Fragment() {

    private lateinit var service: ServiceVolley
    private lateinit var apiController: APIController
    private lateinit var gameDataViewModel: GameDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init api services
        service = ServiceVolley()
        apiController = APIController(service)

        // init view model
        gameDataViewModel = activity?.run {
            ViewModelProviders.of(this).get(GameDataViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        // setting up Listeners
        setUpRootSearchListener()
        setUpSearchButtonListener()
    }

    private fun setUpRootSearchListener() {
        root_search.setOnClickListener { it ->
            val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
        }
    }

    private fun setUpSearchButtonListener() {
        search_summoner_button.setOnClickListener { it ->
            var uri = "/lol/summoner/v4/summoners/by-name/"
            val summonerName = summoner_edit_text.text.toString().replace(" ", "%20")
            val params = JSONObject()
            var apiError = false
            var currSummoner: Summoner? = null

            uri += summonerName

            apiController.get(uri,  params) { response ->
                if (response == null) {
                    apiError = true
                } else {
                    val name = response.get("name") as String
                    val id = response.get("id") as String
                    val accountId = response.get("accountId") as String
                    val puuid = response.get("puuid") as String
                    currSummoner = Summoner(name, puuid, id, accountId)
                    getCurrentGameInformation(apiError, currSummoner)
                }
            }
            val activity = activity!!.findViewById(android.R.id.content) as View
            if (apiError) {
                Snackbar.make(activity, "Summoner not found!", Snackbar.LENGTH_LONG).show()
            }
        }

    }

    private fun getCurrentGameInformation(apiError: Boolean, summoner: Summoner?) {
        val activity = activity!!.findViewById(android.R.id.content) as View
        if (apiError || summoner == null) {
            Snackbar.make(activity, "Summoner not found!", Snackbar.LENGTH_LONG).show()
        } else {
            var uri = "/lol/spectator/v4/active-games/by-summoner/"
            val cosmicInsightId = 8347
            val params = JSONObject()
            uri += summoner.id
            var error = false

            apiController.get(uri, params) { response ->
                val team100 = mutableListOf<Summoner>()
                val team200 = mutableListOf<Summoner>()
                var currTeam = -1
                if (response == null) {
                    error = true
                    Snackbar.make(activity, "Summoner not in game!", Snackbar.LENGTH_LONG).show()
                } else {
                    val participants = response.getJSONArray("participants")
                    for (i in 0 until participants.length()) {
                        var hasInsight = false
                        val summ = participants.getJSONObject(i)
                        val perks = summ.get("perks") as JSONObject
                        val perkIds = perks.getJSONArray("perkIds")

                        for (j in 0 until perkIds.length()) {
                            val perk =  perkIds.getInt(j)
                            if (perk == cosmicInsightId) {
                                hasInsight = true
                                break
                            }
                        }


                        val teamId = summ.get("teamId") as Int
                        val spell1Id = summ.get("spell1Id") as Int
                        val spell2Id = summ.get("spell2Id") as Int
                        val champId = summ.get("championId") as Int
                        val name = summ.get("summonerName") as String
                        val id = summ.get("summonerId") as String

                        val champion = DataFactory.championMap[champId.toString()]
                        val spell1 = DataFactory.spellMap[spell1Id.toString()]
                        val spell2 = DataFactory.spellMap[spell2Id.toString()]

                        val newSummoner = Summoner(name = name, id = id)
                        newSummoner.setChampion(champion)
                        newSummoner.setSummonerSpell(spell1, spell2)
                        newSummoner.hasCosmicInsight = hasInsight
                        Log.d("wowowowow", newSummoner.getChampion().name)
                        Log.d("wowowowow", newSummoner.hasCosmicInsight.toString())

                        if (teamId == 100) {
                            team100.add(newSummoner)
                        } else {
                            team200.add(newSummoner)
                        }

                        if (summoner.id == id) {
                            currTeam = teamId
                        }
                    }
                }
                if (!error) {
                    if (currTeam == 100) {
                        gameDataViewModel.data.value = team200
                    } else {
                        gameDataViewModel.data.value = team100
                    }
                    fragmentManager!!.beginTransaction()
                        .replace(R.id.fragment_container, GameFragment(), "GAME")
                        .commitAllowingStateLoss()
                }
            }
        }

    }

}
