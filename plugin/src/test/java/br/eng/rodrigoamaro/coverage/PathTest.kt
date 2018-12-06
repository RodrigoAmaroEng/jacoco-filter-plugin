package br.eng.rodrigoamaro.coverage

import org.junit.Assert.assertEquals
import org.junit.Test


class PathTest {

    @Test
    fun doesOverlap1() {
        val mainPath = "/home/ramaro/Development/Projetos/coverage/sample/src/main"
        val comparePath = "sample/src/main/java/br/eng/rodrigoamaro/sample/FooJava.java"
        assertEquals("sample/src/main", comparePath.overlapIn(mainPath))
    }

    @Test
    fun doesOverlap2() {
        val mainPath = "/app/src/main/java"
        val comparePath = "app/src/main/java/br/eng/rodrigoamaro/architectureplayground/coffee/PaymentService.kt"
        assertEquals("app/src/main/java", comparePath.overlapIn(mainPath))
    }

    @Test
    fun doesNotOverlap() {
        val mainPath = "/home/ramaro/Development/Projetos/coverage/sample/src/main"
        val comparePath = "trouble/src/main/java/br/eng/rodrigoamaro/sample/FooJava.java"
        assertEquals("", comparePath.overlapIn(mainPath))
    }
}