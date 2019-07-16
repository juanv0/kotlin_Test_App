package com.example.justforvolley

class ResultModel(
    var objectType: Int,
    var month: String,
    var id: Int,
    var type: String,
    var homeTeam: TeamModel,
    var awayTeam: TeamModel,
    var date: String,
    var competitionStage: CompetitionStage,
    var venue: Venue,
    var state: String?,
    var timeStamp: String,
    var score: Score
)

class Score(
    var home: Int,
    var away: Int,
    var winner: String?
)
