package com.codersergg.taskscheduler.dto

import java.io.Serializable

abstract class AbstractOwner: Serializable {
    abstract var name: String
}