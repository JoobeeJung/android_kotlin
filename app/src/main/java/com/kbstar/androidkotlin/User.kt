package com.kbstar.androidkotlin

open class User(val name: String, val phone: String, val role: String) {
    companion object {
        var ADMIN = "ADMIN"
        var USER = "USER"
    }
}