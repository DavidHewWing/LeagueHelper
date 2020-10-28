package com.example.leaguehelper.util

import android.util.Log
import com.example.leaguehelper.model.Champion
import com.example.leaguehelper.model.SummonerSpell
import org.json.JSONArray
import org.json.JSONObject

object DataFactory {
    val championMap = HashMap<String, Champion>()
    val spellMap = HashMap<String, SummonerSpell>()

    fun initChampMap(champions: JSONObject?): Boolean {
        if (champions == null) {
            return false
        }

        val championsIterator = champions.keys()
        while(championsIterator.hasNext()) {
            val champName = championsIterator.next()
            val champObject = champions.get(champName) as JSONObject
            val champImageObject = champObject.get("image") as JSONObject
            val champImageURI = champImageObject.get("full") as String
            val champKey = champObject.get("key") as String

            val newChampion = Champion(champKey, champImageURI, champName)
            championMap[champKey] = newChampion
            Log.d("beepboop", newChampion.toString())
        }
        return true
    }

    fun initSpellMap(spells: JSONObject?): Boolean {
        if (spells == null) {
            return false
        }

        val spellsIterator = spells.keys()
        while(spellsIterator.hasNext()) {
            val spellParentName = spellsIterator.next()
            val spellObject = spells.get(spellParentName) as JSONObject
            val spellImageObject = spellObject.get("image") as JSONObject
            val spellName = spellObject.get("name") as String
            val spellId = spellObject.get("key") as String
            var cooldown = (spellObject.get("cooldown") as JSONArray).getInt(0)
            val spellImageURI = spellImageObject.get("full") as String

            if (spellName == "Teleport") cooldown = 420
            val newSpell = SummonerSpell(spellId, spellImageURI, spellName, cooldown)
            spellMap[spellId] = newSpell
            Log.d("bapbop", newSpell.toString())
        }
        return true
    }

}