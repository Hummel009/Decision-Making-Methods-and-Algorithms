package com.github.hummel.dmma.lab8.grammar

class GrammarGenerator(drawnElements: MutableList<Element>) {
	val grammar: Grammar = Grammar()
	private val rulesVocabulary = arrayListOf<Rule>()
	private var elementNumber = 1

	init {
		rulesVocabulary.add(LeftRule.NONE)
		rulesVocabulary.add(UpRule.NONE)
		grammar.startElementType = connectElementToGrammar(drawnElements)
	}

	private fun connectElementToGrammar(elements: MutableList<Element>): ElementType {
		if (elements.size == 1) {
			return elements[0].elementType
		}
		var resultRule: RuleSearchResult? = null

		for (candidate in elements) {
			resultRule = searchRule(candidate, elements)
			if (resultRule != null) {
				break
			}
		}

		resultRule ?: throw InvalidElementException()

		val result = ElementType("S" + elementNumber++.toString())
		grammar.addElementType(result)
		grammar.addRule(resultRule.getRule(result))

		return result
	}

	private fun searchRule(candidate: Element, elements: MutableList<Element>): RuleSearchResult? {
		rulesVocabulary.forEach {
			if (isFirstInRule(it, candidate, elements)) {
				elements.remove(candidate)
				val firstElementType = candidate.elementType
				val secondElementType = connectElementToGrammar(elements)
				return@searchRule RuleSearchResult(it, firstElementType, secondElementType)
			}
			if (isSecondInRule(it, candidate, elements)) {
				elements.remove(candidate)
				val firstElementType = connectElementToGrammar(elements)
				val secondElementType = candidate.elementType
				return@searchRule RuleSearchResult(it, firstElementType, secondElementType)
			}
		}

		return null
	}

	private fun isFirstInRule(rule: Rule, candidate: Element, elements: List<Element>): Boolean =
		elements.none { isDifferentElementInRule(rule, candidate, it) }

	private fun isSecondInRule(rule: Rule, candidate: Element, elements: List<Element>): Boolean =
		elements.none { isDifferentElementInRule(rule, it, candidate) }

	private fun isDifferentElementInRule(rule: Rule, candidate: Element, element: Element): Boolean {
		return !candidate.startPosition.isSame(element.startPosition) && !candidate.endPosition.isSame(element.endPosition) && !rule.isRulePositionPare(
			candidate, element
		)
	}
}

class RuleSearchResult(
	private val rule: Rule, private val firstElementType: ElementType, private val secondElementType: ElementType
) {
	fun getRule(startElementType: ElementType): Rule =
		rule.getInstance(startElementType, firstElementType, secondElementType)
}