package com.paragraph.ea.main.repository

import com.paragraph.ea.main.exception.DictionaryNotFoundException
import com.paragraph.ea.main.service.dto.DictionaryDto
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class LetterRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun getDictionary(nativeCode: String, learningCode: String): List<DictionaryDto> {
        val fullDictionaryDto = jdbcTemplate.query(
            Constants.SELECT_MAPPING_DICTIONARY_BETWEEN_TWO_LANGUAGE_CODE,
            MapSqlParameterSource()
                .addValue("learningCode", learningCode)
                .addValue("nativeCode", nativeCode),
        ) { resultSet: ResultSet, _: Int ->
            DictionaryDto(
                mutableListOf(resultSet.getString("letter_1")),
                mutableListOf(resultSet.getString("letter_2"))
            )
        }

        if (fullDictionaryDto.isEmpty()) {
            throw DictionaryNotFoundException("These languages are not supported")
        }
        return getStructuredDictionary(fullDictionaryDto)
    }

    private fun getStructuredDictionary(fullDictionaryDto: List<DictionaryDto>): List<DictionaryDto> {
        val dictionary = mutableListOf<DictionaryDto>()

        fullDictionaryDto.forEach { item ->
            val nativeLetterExists = dictionary.any { it.nativeLetter == item.nativeLetter }
            val learningLetterExists = dictionary.any { it.learningLetter == item.learningLetter }

            when {
                !nativeLetterExists && !learningLetterExists -> {
                    dictionary.add(DictionaryDto(item.nativeLetter, item.learningLetter))
                }
                learningLetterExists -> {
                    dictionary.filter { it.learningLetter == item.learningLetter }
                        .forEach { obj -> obj.nativeLetter.add(item.nativeLetter.first()) }
                }
                else -> {
                    dictionary.filter { it.nativeLetter == item.nativeLetter }
                        .forEach { obj -> obj.learningLetter.add(item.learningLetter.first()) }
                }
            }
        }
        return dictionary
    }

    private object Constants {
        const val SELECT_MAPPING_DICTIONARY_BETWEEN_TWO_LANGUAGE_CODE =
            "SELECT lang1.symbol AS letter_1," +
                    "lang2.symbol AS letter_2" +
                    "  FROM letter_mapping m" +
                    "    LEFT JOIN letter lang1 ON (lang1.id = m.letter_1 OR lang1.id = m.letter_2) AND lang1.language_code = :nativeCode" +
                    "    LEFT JOIN letter lang2 ON (lang2.id = m.letter_1 OR lang2.id = m.letter_2) AND lang2.language_code= :learningCode" +
                    "  WHERE lang1.id IS NOT NULL AND lang2.id IS NOT NULL"
    }
}
