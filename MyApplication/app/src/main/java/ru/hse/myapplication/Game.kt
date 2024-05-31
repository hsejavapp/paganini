//package ru.hse.myapplication
//
//import ru.hse.myapplication.words.getWord
//import ru.hse.myapplication.words.validate
//
//class Game constructor() {
//    private var rightWord = getWord()
//    private var status = Status.PROCESSING
//    private var attemptsCnt = 0
//
//    fun guess(guessedWord: StringBuffer): List<Int>? {
//        assert(guessedWord.length == 5)
//        if (!validate(guessedWord))
//            return null
//        val results: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0)
//        for (i in 0 until 5) {
//            if (guessedWord[i] in rightWord.toList())
//                results[i]++
//            if (guessedWord[i] == rightWord[i])
//                results[i]++
//        }
//        attemptsCnt++
//        return results
//    }
//
//    fun getAttemptsCount(): Int {
//        return attemptsCnt
//    }
//
//    fun sendStatus(newStatus: Status) {
//        status = newStatus
//        // тут хорошо бы собирать статистику (отправлять данные на сервер)
//    }
//}
//
//enum class Status {
//    PROCESSING,
//    DEFEAT,
//    VICTORY,
//}
