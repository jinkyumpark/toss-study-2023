package com.jinkyumpark.calculator

class StringCalculator {
    fun add(text: String?): Int {
        if (text.isNullOrEmpty()) {
            return 0
        }

        if (text.isInt()) {
            return text.toInt()
        }

        val numbers = separateNumbers(text)
        if (isNegative(numbers)) {
            throw RuntimeException("Numbers cannot be negative. ${numbers.first}, ${numbers.second}")
        }

        return add(numbers)
    }

    private fun separateNumbers(text: String): Pair<Int, Int> {
        val separated = text.split(Regex(":|,|//.*\n"))
        val firstNumber = separated.first().toInt()
        val secondNumber = separated.last().toInt()

        return firstNumber to secondNumber
    }

    private fun isNegative(numbers: Pair<Int, Int>): Boolean {
        if (numbers.first.isNegative()) {
            return true
        }

        if (numbers.second.isNegative()) {
            return true
        }

        return false
    }

    private fun add(numbers: Pair<Int, Int>): Int {
        return numbers.first + numbers.second
    }
}

private fun Int.isNegative(): Boolean {
    return this < 0
}

private fun String.isInt(): Boolean {
    return this.all { it.isDigit() }
}
