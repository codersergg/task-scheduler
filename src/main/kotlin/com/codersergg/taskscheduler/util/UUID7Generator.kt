package com.codersergg.taskscheduler.util

import java.util.*

fun generateUUID7(): UUID {
    val uuid = UUID.randomUUID()
    val mostSignificantBits = uuid.mostSignificantBits
    val leastSignificantBits = uuid.leastSignificantBits
    val version = (mostSignificantBits and 0x00000000000000000000000000000000fL.inv()) or 0x00000000000000000000000000000000L
    return UUID(version, leastSignificantBits)
}