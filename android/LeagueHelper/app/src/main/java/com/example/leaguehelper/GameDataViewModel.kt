package com.example.leaguehelper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.leaguehelper.model.Summoner

open class GameDataViewModel : ViewModel() {
    val data = MutableLiveData<MutableList<Summoner>>()

    fun getData(list: MutableList<Summoner>) {
        data.value = list
    }

}