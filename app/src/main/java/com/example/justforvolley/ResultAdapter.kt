package com.example.justforvolley

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlin.math.abs

class ResultAdapter(private val context: Context, arrayList: ArrayList<ResultModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    internal var resultList = ArrayList<ResultModel>()
    private val listItemLayoutInflater: LayoutInflater
    init{
        listItemLayoutInflater = LayoutInflater.from(context)
        this.resultList=arrayList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1) {
            return ResultListItemViewHolder(listItemLayoutInflater.inflate(R.layout.item_result_layout, parent, false))
        } else {
            return ResultHeaderItemViewHolder(listItemLayoutInflater.inflate(R.layout.header,parent,false))
        }
    }

    override fun getItemViewType(position: Int) =
        if (resultList[position].objectType==0) {
            0
        } else {
            1
        }
    override fun onBindViewHolder(itemViewHolder: RecyclerView.ViewHolder, position: Int) {

        if (itemViewHolder is ResultListItemViewHolder) {
            val pieces = resultList[position].timeStamp.split("-")
            val day = pieces[2].substring(0,2)
            val month = resultList[position].date.substring(0,3)
            val tiemStamp = month + " " + day + ", " + pieces[0] + " at " + pieces[2].substring(3,8)

            val homeScore= resultList[position].score.home.toULong()
            val awayScore= resultList[position].score.away.toULong()
            when{
                homeScore < awayScore -> itemViewHolder.away.setTextColor(Color.parseColor("#112FA0"))
                homeScore > awayScore -> itemViewHolder.home.setTextColor(Color.parseColor("#112FA0"))
                else -> ""
            }

            itemViewHolder.league.setText(resultList[position].competitionStage.competition.name)
            itemViewHolder.homeTeam.setText(resultList[position].homeTeam.name)
            itemViewHolder.awayTeam.setText(resultList[position].awayTeam.name)
            itemViewHolder.date.setText(resultList[position].venue.name+" | "+tiemStamp)
            itemViewHolder.home.setText(homeScore.toString())
            itemViewHolder.away.setText(awayScore.toString())
        } else if (itemViewHolder is ResultHeaderItemViewHolder){
            itemViewHolder.header.setText(resultList[position].date)
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    inner class ResultListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var league = itemView.findViewById(R.id.lbl_league_r) as TextView
        var homeTeam = itemView.findViewById(R.id.lbl_homeTeam_r) as TextView
        var awayTeam = itemView.findViewById(R.id.lbl_awayTeam_r) as TextView
        var date = itemView.findViewById(R.id.lbl_date_r) as TextView
        var home = itemView.findViewById(R.id.lbl_home_r) as TextView
        var away = itemView.findViewById(R.id.lbl_away_r) as TextView

    }
    inner class ResultHeaderItemViewHolder(headerView: View): RecyclerView.ViewHolder(headerView){
        var header = headerView.findViewById(R.id.header) as TextView
    }
}