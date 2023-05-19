package com.paragraph.ea.main.repository

import com.paragraph.ea.main.common.AbstractTest
import com.paragraph.ea.main.exception.DictionaryNotFoundException
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = ZONKY, type = POSTGRES, refresh = AFTER_EACH_TEST_METHOD)
internal class LetterRepositoryTest(
    @Autowired
    private val letterRepository: LetterRepository
) : AbstractTest() {

    @Nested
    inner class GetDictionary {
        @Test
        fun `when native and learning letters are exist expect DictionaryDto`() {
            val ruGeDictionary = letterRepository.getDictionary("ru", "ge")
            val geEnDictionary = letterRepository.getDictionary("ge", "en")
            val enGeDictionary = letterRepository.getDictionary("en", "ge")

            assertTrue(ruGeDictionary.isNotEmpty())
            assertTrue(geEnDictionary.isNotEmpty())
            assertTrue(enGeDictionary.isNotEmpty())
            assertTrue(ruGeDictionary.size == 28)
            assertTrue(geEnDictionary.size == 27)
            assertTrue(enGeDictionary.size == 27)
        }

        @Test
        fun `when native and learning letters are not exist expect empty list`() {
            val exceptionResponse = assertThrows(DictionaryNotFoundException::class.java) {
                letterRepository.getDictionary("TeEsT1", "TEsST2")
            }

            assertEquals(exceptionResponse.localizedMessage, "These languages are not supported")
        }
    }
}
