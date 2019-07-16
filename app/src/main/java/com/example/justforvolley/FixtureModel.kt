package com.example.justforvolley

class FixtureModel(
    var objectType: Int,
    var month: String,
    var id: Int,
    var type: String,
    var homeTeam: TeamModel,
    var awayTeam: TeamModel,
    var date: String,
    var competitionStage: CompetitionStage,
    var venue: Venue,
    var timeStamp: String,
    var state: String?
)
class Competition(
    var id:Int,
    var name: String
)
class CompetitionStage(
    var competition:Competition
)

class TeamModel(
    var id:Int,
    var name:String,
    var shorName:String,
    var abbr: String,
    var alias:String
)

class Venue(
    var id: Int,
    var name: String
)