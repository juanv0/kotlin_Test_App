package com.example.justforvolley

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FixtureFragment: Fragment() {

    var resultList : ArrayList<FixtureModel> = ArrayList()
    private lateinit var fixtureItemAdapter: FixtureAdapter
    private lateinit var fixtureRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fixture_get_activity, container, false)
        fixtureItemAdapter = FixtureAdapter(requireContext(), resultList)
        getFixtures()
        fixtureRecyclerView = rootView.findViewById(R.id.recycler_view) as RecyclerView
        fixtureRecyclerView.layoutManager = LinearLayoutManager(activity)
        fixtureRecyclerView.adapter = FixtureAdapter(requireContext(), resultList)
        return rootView
    }

    companion object{
        @JvmStatic
        fun newInstance(): FixtureFragment = FixtureFragment()
    }

    private fun getFixtures(): ArrayList<FixtureModel> {

        val url = "https://storage.googleapis.com/cdn-og-test-api/test-task/fixtures.json"
        val stringReq= StringRequest(Request.Method.GET, url, Response.Listener<String>{
                response->
            var strResponse = response.toString()
            val jsonAr: JSONArray = JSONArray(strResponse)
            resultList = ArrayList(jsonAr.length())
            for (i in 0 until jsonAr.length()){
                try {
                    val jsonInner: JSONObject = jsonAr.getJSONObject(i)
                    var state: String? = ""
                    if (jsonInner.has("state")){
                        state = jsonInner.getString("state")
                    }
                    val year = jsonInner.getString("date").split("-")[0]
                    val monthly = year + "-" +jsonInner.getString("date").split("-")[1]
                    val nameMonths = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
                    val date = nameMonths[jsonInner.getString("date").split("-")[1].toInt()-1] + " " + year
                    val homeTeamJson = jsonInner.getJSONObject("homeTeam") as JSONObject
                    val awayTeamJson = jsonInner.getJSONObject("awayTeam") as JSONObject
                    val competitionStageJson = jsonInner.getJSONObject("competitionStage")
                    val competitionJson = competitionStageJson.getJSONObject("competition")
                    val venueJson = jsonInner.getJSONObject("venue")
                    val competition = Competition(
                        competitionJson.getInt("id"),
                        competitionJson.getString("name")
                    )
                    val competitionStage = CompetitionStage(competition)
                    val venue = Venue(
                        venueJson.getInt("id"),
                        venueJson.getString("name")
                    )
                    val homeTeam = TeamModel(
                        homeTeamJson.getInt("id"),
                        homeTeamJson.getString("name"),
                        homeTeamJson.getString("shortName"),
                        homeTeamJson.getString("abbr"),
                        homeTeamJson.getString("alias")
                    )
                    val awayTeam = TeamModel(
                        awayTeamJson.getInt("id"),
                        awayTeamJson.getString("name"),
                        awayTeamJson.getString("shortName"),
                        awayTeamJson.getString("abbr"),
                        awayTeamJson.getString("alias")
                    )

                    resultList.add(
                        FixtureModel(
                            1,
                            monthly,
                            jsonInner.getInt("id"),
                            jsonInner.getString("type"),
                            homeTeam,
                            awayTeam,
                            date,
                            competitionStage,
                            venue,
                            jsonInner.getString("date"),
                            state
                        )
                    )
                }catch (e: JSONException){
                    e.printStackTrace()
                }
            }

            setUpData(resultList)

        }, Response.ErrorListener { error ->
            if (this.context!=null) {
                var currentContext: Context = this.context!!
                Toast.makeText(currentContext, "Error: " + (error.toString()), Toast.LENGTH_SHORT).show();
            }

        })
        val queue = Volley.newRequestQueue(requireContext())
        val add = queue.add(stringReq)
        return resultList

    }
    private fun setUpData(resultList: ArrayList<FixtureModel>){

        fixtureItemAdapter = FixtureAdapter(requireContext(), toGroupBy(resultList))
        //fixtureItemAdapter = FixtureAdapter(requireContext(), resultList)
        fixtureRecyclerView.adapter = fixtureItemAdapter
        //print(toGroupBy(resultList)[0].toString())
    }
    private fun toGroupBy(resultList: ArrayList<FixtureModel>): ArrayList<FixtureModel>{
        val pastResult = resultList.groupBy { it.month }
        val listN = resultList.groupBy { it.month }.keys.size + resultList.size
        var newResultList: ArrayList<FixtureModel> = ArrayList(listN)
        val emptyTeam = TeamModel(0,"","","","")
        val emptyVenue = Venue(0,"")
        val emptyComp = CompetitionStage(Competition(0,""))
        for (key in pastResult.keys) {
            val item = pastResult[key] as List<FixtureModel>
            newResultList.add(
                FixtureModel(
                    0,
                    key,
                    0,
                    "",
                    emptyTeam,
                    emptyTeam,
                    item[0].date,
                    emptyComp,
                    emptyVenue,
                    "",
                    ""
                )
            )

                for (value in item) {
                    newResultList.add(value)
                }
            }
        return newResultList
    }
}