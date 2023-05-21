package com.app.testkotlin.model

class User {
    var id = 0
    var firstName: String? = null
    var lastName: String? = null
    var image: String? = null
    var dob: String? = null
    var gender = false
    var weight = 0.0
    var height = 0.0
    var email: String? = null

    constructor(
        id: Int,
        firstName: String?,
        lastName: String?,
        image: String?,
        dob: String?,
        gender: Boolean,
        weight: Double,
        height: Double,
        email: String?
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.image = image
        this.dob = dob
        this.gender = gender
        this.weight = weight
        this.height = height
        this.email = email
    }

    constructor() {}
}