package org.hstefans.strap.app.controllers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javafx.collections.ObservableList
import org.hstefans.strap.app.main.Task
import tornadofx.Controller
import java.io.File


class TaskController : Controller() {

    private val taskJsonPath = "src/JSON/Tasks.json"


    fun filterTasksForUser(username: String): List<Task>? {
        println(getAllTasks())
        return getAllTasks()?.filter { task -> task.assignee == username }
    }


    //Return all users from Json, used for login authentication
    private fun getAllTasks(): List<Task>? {
        val mapper = jacksonObjectMapper()
        //Configure mapper
        mapper.registerKotlinModule()
        mapper.findAndRegisterModules()
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);


        val jsonString: String = File(taskJsonPath).readText(Charsets.UTF_8)

        return mapper.readValue(jsonString) as ObservableList<Task>


    }


    //Write to Json
    public fun writeToDataStore(task: Task): String {

        val gson = Gson()

        val jsonUser: String = gson.toJson(task)
        File(
            taskJsonPath
        ).writeText(jsonUser)

        val gsonPretty = GsonBuilder().setPrettyPrinting().create()

        //Clean up the structure of the JSON before writing(improves readability for humans)
        val jsonPretty: String = gsonPretty.toJson(task)
        File(taskJsonPath).writeText(jsonPretty)

        //Return some sort of response
        return "Success"
    }
}
