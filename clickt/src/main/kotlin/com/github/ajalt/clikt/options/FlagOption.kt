package com.github.ajalt.clikt.options

import com.github.ajalt.clikt.parser.BadOptionUsage
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class FlagOption(vararg val names: String, val help: String = "", val default: Boolean = false)

class FlagOptionParser(names: Array<out String> = emptyArray()) : OptionParser {
    private val offNames: List<String> = names.mapNotNull {
        if ("/" in it) {
            val split = it.split("/", limit = 2)
            split[1].let { if (it.isBlank()) null else it }
        }
        else null
    }

    override val repeatableForHelp: Boolean get() = false

    override fun parseLongOpt(name: String, argv: Array<String>, index: Int, explicitValue: String?): ParseResult {
        if (explicitValue != null) throw BadOptionUsage("$name option does not take a value")
        return ParseResult(1, name !in offNames)
    }

    override fun parseShortOpt(name: String, argv: Array<String>, index: Int, optionIndex: Int): ParseResult =
            ParseResult(if (optionIndex == argv[index].lastIndex) 1 else 0, name !in offNames)
}

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class CountedOption(vararg val names: String, val help: String = "")