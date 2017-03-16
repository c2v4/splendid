package com.c2v4.splendid.core.util

import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.core.model.Resource.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class CostUtilityTest {

    @Test
    fun canBuyTest() {
        assertThat(canBuy(mapOf(RED to 2), mapOf(RED to 3))).isTrue()
        assertThat(canBuy(mapOf(RED to 1, BLUE to 2), mapOf(RED to 3, BLUE to 2))).isTrue()
        assertThat(canBuy(mapOf(RED to 1, BLUE to 2), mapOf(RED to 3, GOLD to 2))).isTrue()
        assertThat(canBuy(mapOf(RED to 1, BLUE to 2), mapOf(RED to 3, BLUE to 1))).isFalse()
        assertThat(canBuy(mapOf(RED to 1, BLUE to 2),
                mapOf(RED to 1, BLUE to 1, GOLD to 1))).isTrue()
    }

    @Test
    fun isTakenCorrectTest() {
        assertThat(isTakenCorrect(mapOf(RED to 1, BLUE to 1, BLACK to 1))).isTrue()
        assertThat(isTakenCorrect(mapOf(RED to 1, BLUE to 1, BLACK to 1, GREEN to 1))).isFalse()
        assertThat(isTakenCorrect(mapOf(RED to 2, BLUE to 1, BLACK to 1))).isFalse()
        assertThat(isTakenCorrect(mapOf(RED to 1, BLUE to 1, GOLD to 1))).isFalse()
        assertThat(isTakenCorrect(mapOf(RED to 2))).isTrue()
        assertThat(isTakenCorrect(mapOf(RED to 1))).isFalse()
        assertThat(isTakenCorrect(mapOf(GOLD to 2))).isFalse()
    }

    @Test
    fun isDrawCorrectTest() {
        assertThat(isDrawCorrect(mapOf(RED to 1, BLUE to 1, BLACK to 1),
                mapOf<Resource, Int>(),
                mapOf<Resource, Int>())).isTrue()
        assertThat(isDrawCorrect(mapOf(BLUE to 1, BLACK to 1),
                mapOf<Resource, Int>(),
                mapOf<Resource, Int>())).isFalse()
        assertThat(isDrawCorrect(mapOf(RED to 1, BLUE to 1, BLACK to 1),
                mapOf(RED to 1),
                mapOf(RED to 8))).isTrue()
        assertThat(isDrawCorrect(mapOf(RED to 1, BLUE to 1, BLACK to 1),
                mapOf(BLUE to 1, BLACK to 1),
                mapOf(RED to 9))).isTrue()
        assertThat(isDrawCorrect(mapOf(RED to 1, BLUE to 1, BLACK to 1),
                mapOf(),
                mapOf(BLACK to 9))).isFalse()
        assertThat(isDrawCorrect(mapOf(RED to 1, BLUE to 1, BLACK to 1),
                mapOf(GREEN to 1),
                mapOf(BLACK to 8))).isFalse()
    }

    @Test
    fun mapMergeTest() {
        assertThat(mapOf(RED to 2, BLUE to 4).merge(mapOf(RED to 1,
                BLACK to 2))).containsAllEntriesOf(mapOf(RED to 3, BLUE to 4, BLACK to 2))
    }

    @Test
    fun mapSubtractPositiveTest() {
        assertThat(mapOf(RED to 2).subtractPositive(mapOf(RED to 1))).containsAllEntriesOf(mapOf(RED to 1))
        assertThat(mapOf(RED to 2,
                BLUE to 2).subtractPositive(mapOf(RED to 1))).containsAllEntriesOf(mapOf(RED to 1,
                BLUE to 2))
        assertThat(mapOf(RED to 1,
                BLUE to 2).subtractPositive(mapOf(BLUE to 2))).containsAllEntriesOf(mapOf(RED to 1))
        assertThat(mapOf(BLUE to 2).subtractPositive(mapOf(BLUE to 2))).isEmpty()
        assertThatThrownBy { mapOf(BLUE to 2).subtractPositive(mapOf(RED to 1)) }
        assertThatThrownBy { mapOf(BLUE to 2).subtractPositive(mapOf(BLUE to 3)) }
    }

    @Test
    fun haveValuesTest() {
        assertThat(mapOf(RED to 1, BLUE to 2).haveValues(mapOf(RED to 1))).isTrue()
        assertThat(mapOf(RED to 1, BLUE to 2).haveValues(mapOf(RED to 1,BLUE to 2))).isTrue()
        assertThat(mapOf(RED to 1, BLUE to 2).haveValues(mapOf(RED to 1,BLUE to 3))).isFalse()
        assertThat(mapOf(RED to 1, BLUE to 2).haveValues(mapOf(RED to 1,GREEN to 2))).isFalse()
        assertThat(mapOf<Resource,Int>().haveValues(mapOf())).isTrue()
        assertThat(mapOf<Resource,Int>().haveValues(mapOf(RED to 1))).isFalse()
    }
}
