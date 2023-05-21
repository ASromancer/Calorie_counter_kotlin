package com.app.testkotlin.model

class Account {
    var username: String? = null
    var password: String? = null
    private var role: Role? = null
    var user: User? = null

    constructor(username: String?, password: String?, role: Role?, user: User?) {
        this.username = username
        this.password = password
        this.role = role
        this.user = user
    }

    constructor() {}

    fun getRole(): Role? {
        return role
    }

    fun setRole(role: Role?) {
        this.role = role
    }
}