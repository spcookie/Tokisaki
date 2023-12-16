package io.micro.core.auth

import kotlinx.coroutines.sync.Mutex
import kotlin.random.Random

object RandomLoginCode {

    class Code(
        val value: String,
        private val isSpecial: Boolean,
        private val special: MutableList<String>
    ) {
        fun back() {
            if (isSpecial) {
                special.add(value)
            }
        }
    }

    private val special = mutableListOf(
        "1111", "3691", "8889", "7776", "6666",
        "5555", "4444", "3333", "2222", "1010",
        "8887", "1319", "9998", "7778", "6669",
        "5556", "4445", "3334", "2223", "1011",
        "1315", "5208", "8886", "1689", "7779",
        "9997", "6668", "5557", "4446", "3335"
    )

    private val mutex = Mutex()

    private val random = Random(0)

    fun hire(): Code {
        return if (mutex.tryLock()) {
            val code = if (special.isNotEmpty()) {
                Code(special.removeFirst(), true, special)
            } else {
                Code(random(), false, special)
            }
            mutex.unlock()
            code
        } else {
            Code(random(), false, special)
        }
    }

    private fun random() = random.nextInt(10000, 99999).toString()

}