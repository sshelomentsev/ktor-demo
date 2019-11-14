package com.sshelomentsev

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Routing) {
        route("/tasks") {
            get {
                call.respond(HttpStatusCode.OK, SimpleInMemoryDB.getTasks())
            }
            post {
                val form = call.receive<TaskForm>()
                SimpleInMemoryDB.addTask(form)
                call.respond(HttpStatusCode.Created)
            }
            route("/{taskId}") {
                get {
                    val task = call.parameters["taskId"]!!.toLong().let { SimpleInMemoryDB.getTask(it) }
                    task?.let {
                        call.respond(HttpStatusCode.OK, task)
                    } ?: run {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
                put {
                    val taskId = call.parameters["taskId"]!!.toLong()
                    val form = call.receive<TaskForm>()
                    val result = SimpleInMemoryDB.updateTask(taskId, form)
                    if (result) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
                delete {
                    val taskId = call.parameters["taskId"]!!.toLong()
                    val result = SimpleInMemoryDB.deleteTask(taskId)
                    if (result) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
    install(StatusPages) {
        exception<JsonParseException> { cause ->
            call.respond(HttpStatusCode.BadRequest)
        }
    }

}

