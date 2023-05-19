package com.paragraph.ea.main.common

object OpenApiConstants {

    const val MAX_SAFE_LONG_VALUE = "9007199254740991"
    const val MAX_SAFE_LONG = 9007199254740991L
    const val MAX_SAFE_INT_VALUE = "2147483647"
    const val MAX_SAFE_INT = 2147483647
    const val MAX_STRING_LENGTH_255 = 255
    const val MIN_STRING_LENGTH_1 = 1
    const val MIN_STRING_LENGTH_1_VALUE = "1"
    const val ZERO_VALUE = "0"
    const val ZERO = 0
    const val MAX_LIMIT = 100
    const val DEFAULT_MAX_LIMIT = "100"
    const val MAX_MODE_LENGTH = 10

    const val DESCRIPTION_BAD_REQUEST = "BAD_REQUEST"
    const val DESCRIPTION_NOT_FOUND = "NOT FOUND"
    const val DESCRIPTION_OK = "OK"
    const val DESCRIPTION_FORBIDDEN = "FORBIDDEN"
    const val DESCRIPTION_NO_CONTENT = "NO_CONTENT"

    const val INTEGER_TYPE = "integer"
    const val DATE_TYPE = "date"
    const val DATE_TIME_TYPE = "date-time"

    const val DATE_FORMAT_PATTERN =
        "^[0-9]{4}-((0[1-9])|(1[0-2]))-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(()|(.[0-9]{1,6})(Z|([+-](0[0-9]|1[0-2]):([0-5][0-9])))|(.[0-9]{1,6}))$"
    const val DATE_PATTERN = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$"
    const val DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    const val DATE_TIME_LOCAL_PATTERN = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}$"
    const val UUID_PATTERN = "^\\b[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-\\b[0-9a-fA-F]{12}\\b$"
    const val ALPHANUMERIC_PATTERN = "^[a-zA-Zа-яА-Я0-9]+$"
    const val LETTERS_WITH_WHITESPACE_ONLY_PATTERN = "^[a-zA-Zа-яА-Я ]+$"
    const val LETTERS_ONLY_PATTERN = "^[a-zA-Zа-яА-Я]+$"
    const val LETTERS_ONLY_ENG_PATTERN = "^[a-zA-Z]+$"
    const val ERROR_CODE_PATTERN = "^[a-z\\.-]+$"
    const val DIGITS_ONLY_PATTERN = "^[0-9]+$"
    const val ANY_SYMBOL_PATTERN = ".*"

}