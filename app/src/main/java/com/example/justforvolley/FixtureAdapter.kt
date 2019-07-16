package com.example.justforvolley

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.SimpleDateFormat

class FixtureAdapter(private val context: Context, arrayList: ArrayList<FixtureModel>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        internal var fixtureList = ArrayList<FixtureModel>()
        private val listItemLayoutInflater: LayoutInflater
        init{
                listItemLayoutInflater = LayoutInflater.from(context)
                this.fixtureList=arrayList
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewGroup: Int): RecyclerView.ViewHolder {

                if(viewGroup==1) {
                        return ListItemViewHolder(listItemLayoutInflater.inflate(R.layout.item_layout, parent, false))
                }else{
                        return HeaderItemViewHolder(listItemLayoutInflater.inflate(R.layout.header, parent, false))
                }
        }

        override fun onBindViewHolder(itemViewHolder: RecyclerView.ViewHolder, position: Int) {
                if (itemViewHolder is ListItemViewHolder) {
                        val pieces = fixtureList[position].timeStamp.split("-")
                        val day = pieces[2].substring(0,2)
                        val month = fixtureList[position].date.substring(0,3)
                        var postponed = ""
                        val state = fixtureList[position].state
                        if(state=="postponed") {
                                itemViewHolder.toAlert.setTextColor(Color.parseColor("#ec321e"))
                                postponed = " POSTPONED"

                        }
                        val tiemStamp = month + " " + day + ", " + pieces[0] + " at " + pieces[2].substring(3,8) + postponed
                        itemViewHolder.league.setText(fixtureList[position].competitionStage.competition.name)
                        itemViewHolder.awayTeam.setText(fixtureList[position].awayTeam.name.format("utf-8"))
                        itemViewHolder.homeTeam.setText(fixtureList[position].homeTeam.name)
                        itemViewHolder.date.setText(day + " Of "+ month)
                        itemViewHolder.diaHora.setText(fixtureList[position].venue.name+" |")
                        itemViewHolder.toAlert.setText(tiemStamp)
                }else if (itemViewHolder is HeaderItemViewHolder){

                        itemViewHolder.header.setText(fixtureList[position].date)
                }

        }
        override fun getItemViewType(position: Int) =
                if (fixtureList[position].objectType==0) {
                        0
                } else {
                        1
                }
        override fun getItemCount(): Int {
                return fixtureList.size
        }
        inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

                var league = itemView.findViewById(R.id.lbl_league) as TextView
                var homeTeam = itemView.findViewById(R.id.lbl_home) as TextView
                var awayTeam = itemView.findViewById(R.id.lbl_away) as TextView
                var date = itemView.findViewById(R.id.lbl_date) as TextView
                var diaHora = itemView.findViewById(R.id.day_hour) as TextView
                var toAlert = itemView.findViewById(R.id.to_alert) as TextView
        }
        inner class HeaderItemViewHolder(headerView: View) : RecyclerView.ViewHolder(headerView){

                var header = headerView.findViewById(R.id.header) as TextView
        }
}