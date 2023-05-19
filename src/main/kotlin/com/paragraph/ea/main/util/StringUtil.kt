package com.paragraph.ea.main.util

import mu.KotlinLogging

object StringUtil {
    private val log = KotlinLogging.logger { }

    fun validateParagraph(paragraph: String): String {
        log.debug("Starting validate paragraph. Paragraph size: {}", paragraph.length)
        val result = paragraph.trim()
        val indexes = result.indexesOfBeginning().apply { add(0) }
        log.debug("Found {} indexes to change it to uppercase", indexes.size)

        return buildString {
            for ((index, char) in result.withIndex()) {
                append(if (index in indexes) char.uppercaseChar() else char)
            }
        }
    }

    fun String.replaceMultiple(nativeLetters: List<String>, learningLetters: List<String>): String {
        return nativeLetters.fold(this) { text, native ->
            text.replace(native, learningLetters.random().lowercase(), true)
        }
    }

    private fun String.indexesOfBeginning(): MutableList<Int> {
        val pattern = """[!?.]\s\p{Ll}""".toRegex()
        return pattern.findAll(this).map { it.range.last }.toMutableList()
    }

    fun indexOfNearestEndpoint(subString: String, text: String, globalIndex: Int): Int {
        val punctuationMarks = setOf('!', '?', '.')
        return subString.withIndex().indexOfFirst {
            it.value in punctuationMarks
                    && text.getOrNull(globalIndex + it.index + 1)?.isWhitespace() == true
                    && text[globalIndex + it.index + 2].isLetter()
        }
    }
}