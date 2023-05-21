package com.app.testkotlin.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(val username: String, val password: String)
