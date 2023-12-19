package com.jinkyumpark.calculator

class StringCalculator {
    fun add(text: String?): Int {
        if (text.isNullOrEmpty()) {
            return 0
        }

        if (text.isInt()) {
            return text.toInt()
        }

        val separated = text.split(Regex(":|,|//.*\n"))
        val numbers = separated.first().toInt() to separated.last().toInt()

        if (numbers.first.isNegative() || numbers.second.isNegative()) {
            throw RuntimeException("Numbers cannot be negative. ${numbers.first}, ${numbers.second}")
        }

        return numbers.first + numbers.second
    }
}

private fun Int.isNegative(): Boolean {
    return this < 0
}

private fun String.isInt(): Boolean {
    return this.all { it.isDigit() }
}
