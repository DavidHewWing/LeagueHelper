package com.example.leaguehelper.api

import org.json.JSONObject

interface ServiceInterface {
    fun get(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun getJSON(path: String, completionHandler: (response: JSONObject?) -> Unit)
}