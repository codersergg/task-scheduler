package com.codersergg.taskscheduler.dto

import java.io.Serializable

abstract class AbstractProvider: Serializable {
    abstract var name: String
}