package com.gestor.turnos.core.constants

object LaundryConstants {

    object Timing {
        const val DEFAULT_SLOT_DURATION_MINUTES = 120
        const val GRID_GRANULARITY_MINUTES = 30
        const val GRACE_PERIOD_MINUTES = 10
        const val REMINDER_BEFORE_MINUTES = 15
    }

    object Limits {
        const val MAX_BOOKINGS_PER_WEEK = 3
        const val MAX_ADVANCE_BOOKING_DAYS = 7
        const val MIN_BOOKING_DURATION_MINUTES = 60
        const val MAX_BOOKING_DURATION_MINUTES = 240
    }

    object Penalties {
        const val NO_SHOW_POINTS = 1
        const val LATE_CANCELLATION_POINTS = 1
        const val MAX_PENALTY_POINTS = 3
        const val PENALTY_RESET_DAYS = 30
    }

    object OperatingHours {
        const val OPEN_HOUR = 7
        const val CLOSE_HOUR = 23
    }

    object Network {
        const val API_TIMEOUT_SECONDS = 30L
        const val RETRY_ATTEMPTS = 3
        const val RETRY_DELAY_MS = 1000L
    }

    object Notifications {
        const val CHANNEL_ID_REMINDERS = "reminders"
        const val CHANNEL_ID_STATUS = "status_updates"
        const val CHANNEL_ID_ADMIN = "admin_alerts"
    }

    object MQTT {
        const val QOS_LEVEL = 1
        const val KEEP_ALIVE_INTERVAL = 60
        const val CONNECTION_TIMEOUT = 30
    }
}