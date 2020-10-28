package com.example.leaguehelper.api

import org.json.JSONObject

class APIController constructor(serviceInjection: ServiceInterface): ServiceInterface {
    private val service: ServiceInterface = serviceInjection

    override fun get(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        service.get(path, params, completionHandler)
    }

    override fun getJSON(path: String, completionHandler: (response: JSONObject?) -> Unit) {
        service.getJSON(path, completionHandler)
    }
}