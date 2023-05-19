package com.paragraph.ea.main.service.dto

class DictionaryDto(

    var nativeLetter: MutableList<String>,
    var learningLetter: MutableList<String>
) {
    var isUsed: Boolean = false
}
