package com.example.justforvolley

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ResultFragment: Fragment() {

    var resultList : ArrayList<ResultModel> = ArrayList()
    private lateinit var resultItemAdapter: ResultAdapter
    private lateinit var resultRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.result_get_activity, container, false)
        resultItemAdapter = ResultAdapter(requireContext(), resultList)
        getResults()
        resultRecyclerView = rootView.findViewById(R.id.recycler_view_request) as RecyclerView
        resultRecyclerView.layoutManager = LinearLayoutManager(activity)
        resultRecyclerView.adapter = ResultAdapter(requireContext(), resultList)
        return rootView

    }


    companion object{
        @JvmStatic
        fun newInstance(): ResultFragment = ResultFragment()
    }


    private fun getResults() {

        val url:String = "https://storage.googleapis.com/cdn-og-test-api/test-task/results.json"
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
                    val month = year + "-" +jsonInner.getString("date").split("-")[1]
                    var jsonResult = jsonInner.getJSONObject("score")
                    var winner: String? = ""
                    if(jsonResult.has("winner")) winner = jsonResult.getString("winner")
                    val monthly = year + "-" +jsonInner.getString("date").split("-")[1]
                    val nameMonths = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
                    val date = nameMonths[jsonInner.getString("date").split("-")[1].toInt()-1] + " " + year
                    val homeTeamJson = jsonInner.getJSONObject("homeTeam") as JSONObject
                    val awayTeamJson = jsonInner.getJSONObject("awayTeam") as JSONObject
                    val competitionStageJson = jsonInner.getJSONObject("competitionStage")
                    val competitionJson = competitionStageJson.getJSONObject("competition")
                    val venueJson = jsonInner.getJSONObject("venue")
                    val scoreJson = jsonInner.getJSONObject("score")
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
                    val score = Score(
                        scoreJson.getInt("home"),
                        scoreJson.getInt("away"),
                        winner
                    )
                    resultList.add(
                        ResultModel(
                            1,
                            month,
                            jsonInner.getInt("id"),
                            jsonInner.getString("type"),
                            homeTeam,
                            awayTeam,
                            date,
                            competitionStage,
                            venue,
                            state,
                            jsonInner.getString("date"),
                            score
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
        queue.add(stringReq)

    }
    private fun setUpData(resultList: ArrayList<ResultModel>){

        resultItemAdapter = ResultAdapter(requireContext(), toGroupBy(resultList))
        resultRecyclerView.adapter = resultItemAdapter
    }
    private fun toGroupBy(resultList: ArrayList<ResultModel>): ArrayList<ResultModel>{
        val pastResult = resultList.groupBy { it.month }
        val listN = resultList.size + resultList.groupBy { it.month }.keys.size
        var newResultList: ArrayList<ResultModel> = ArrayList(listN)
        val emptyTeam = TeamModel(0,"","","","")
        val emptyVenue = Venue(0,"")
        val emptyComp = CompetitionStage(Competition(0,""))
        val emptyScore = Score(0,0,"")
        for (key in pastResult.keys){
            val item = pastResult[key] as List<ResultModel>
            newResultList.add(
                ResultModel(
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
                    "",
                    emptyScore
                )
            )

            for(value in item){
                newResultList.add(value)
            }
        }
        return newResultList
    }
}