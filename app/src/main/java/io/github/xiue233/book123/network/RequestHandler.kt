package io.github.xiue233.book123.network

interface RequestHandler {
    fun onStart()

    fun onCompletion()

    fun onFailure(message: String)
}

object EmptyRequestHandler : RequestHandler {
    override fun onStart() {
    }

    override fun onCompletion() {
    }

    override fun onFailure(message: String) {
    }
}