package example

import io.micronaut.runtime.Micronaut

fun main(args: Array<String>) {
    Micronaut.build().args(*args).packages("example").start()
}

