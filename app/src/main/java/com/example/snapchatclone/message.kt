package com.example.snapchatclone

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


class message {
    var message: String? = null
    var senderId: String? = null



    constructor(){}

    constructor(message: String?, senderId: String?){
        this.message = message
        this.senderId = senderId
    }
}