package org.tahomarobotics.scouting

import androidx.compose.runtime.MutableIntState
import com.google.gson.JsonObject
import compKey
import createScoutMatchDataFile
import nodes.*
import kotlin.math.roundToInt

class RandomData {
    fun makeUpData(team: MutableIntState, robotStartPosition: MutableIntState, match: MutableIntState){
        //Save temp data
        teamDataArray.get(compKey)?.get(match.value)?.set(robotStartPosition.intValue, createOutput(team, robotStartPosition))

        //Save permanent data
        createScoutMatchDataFile(compKey, match.value.toString(), team.intValue, createRandomOutput(team, robotStartPosition, match.value))
        match.value++
        reset()
        matchFirst.value = true
        team.intValue = (Math.random() * 10000).roundToInt()
        //Grab team from TBA for next match
//                        loadData(parseInt(match.value), team, robotStartPosition
        if(match.intValue <= 60){
            makeUpData(team, robotStartPosition, match)
        }
    }

    fun createRandomOutput(team: MutableIntState, robotStartPosition: MutableIntState, match: Int): String {
        println("saved data")
        if (notes.value.isEmpty()) {
            notes.value = "No Comments"
        }
//    notes.value = notes.value.replace(":", "")

        jsonObject = JsonObject().apply {
            addProperty("team", team.intValue.toString())
            addProperty("event_key", compKey)
            addProperty("match", match)
            addProperty("scout_name", scoutName.value)
            addProperty("notes", notes.value)
            addProperty("robotStartPosition", robotStartPosition.intValue)
            add("auto", JsonObject().apply {
                addProperty("stop", Math.random().roundToInt())
                add("algae", JsonObject().apply {
                    addProperty("ground_collection", (Math.random() * 2.0).roundToInt())
                    addProperty("removed", (Math.random() * 10.0).roundToInt())
                    addProperty("processed", (Math.random() * 10.0).roundToInt())
                })
                add("coral", JsonObject().apply {
                    addProperty("collection", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level1", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level2", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level3", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level4", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level1_missed", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level2_missed", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level3_missed", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level4_missed", (Math.random() * 10.0).roundToInt())
                })
                add("net", JsonObject().apply {
                    addProperty("scored", (Math.random() * 10.0).roundToInt())
                    addProperty("missed", (Math.random() * 10.0).roundToInt())
                })
            })
            add("tele", JsonObject().apply {
                addProperty("lost_comms", Math.random().roundToInt())
                addProperty("penalties", (Math.random() * 10.0).roundToInt())
                addProperty("played_defense", playedDefense.value)
                add("algae", JsonObject().apply {
                    addProperty("reef_collected", (Math.random() * 10.0).roundToInt())
                    addProperty("processed", (Math.random() * 10.0).roundToInt())
                })
                add("coral", JsonObject().apply {
                    addProperty("reef_level1", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level2", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level3", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level4", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level1_missed", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level2_missed", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level3_missed", (Math.random() * 10.0).roundToInt())
                    addProperty("reef_level4_missed", (Math.random() * 10.0).roundToInt())

                })
                add("net", JsonObject().apply {
                    addProperty("scored", (Math.random() * 10.0).roundToInt())
                    addProperty("missed", (Math.random() * 10.0).roundToInt())
                })
            })
//        add("endgame", JsonObject().apply {
//            addProperty("park", park.value)
//            addProperty("deep", deep.value)
//            addProperty("shallow", shallow.value)
//        })
        }
        return jsonObject.toString()
    }
}