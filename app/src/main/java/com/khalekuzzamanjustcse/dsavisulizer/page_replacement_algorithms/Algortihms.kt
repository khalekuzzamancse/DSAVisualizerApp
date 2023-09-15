package com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms

enum class ReplacementType {
    LRU, MRU, OPTIMAL,FIFO
}

class PageReplacementAlgorithms(
    private val requestPages: List<Int>,
    private val memoryCapacity: Int
) {

    private fun leastRecentlyUsedIndex(
        memoryPages: List<Int>,
        usedPages: List<Int>
    ): Int {
        var leastUsedIndex = Int.MAX_VALUE
        var leastRecentUsed = -1
        for (target in memoryPages) {
            val index = usedPages.lastIndexOf(target)
            if (index < leastUsedIndex) {
                leastUsedIndex = index
                leastRecentUsed = target
            }
        }
        return memoryPages.indexOf(leastRecentUsed)
    }

    private fun optimalPageReplacementIndex(
        memoryPages: List<Int>,
        futurePages: List<Int>
    ): Int {
        println(futurePages)
        if (futurePages.isEmpty())
            return 0
        var maxIndex = -1
        var laterUse = -1
        for (target in memoryPages) {
            val index = futurePages.indexOf(target)
            if (index == -1) {
                return memoryPages.indexOf(target)
            }

            if (index > maxIndex) {
                maxIndex = index
                laterUse = target
            }
        }
        return memoryPages.indexOf(laterUse)
    }

    private fun mostRecentlyUsedIndex(
        memoryPages: List<Int>,
        usedPages: List<Int>
    ): Int {
        println(usedPages)
        var maxIndex = -1
        var mostRecentUsed = -1
        for (target in memoryPages) {
            val index = usedPages.lastIndexOf(target)
            if (index > maxIndex) {
                maxIndex = index
                mostRecentUsed = target
            }
        }
        return memoryPages.indexOf(mostRecentUsed)
    }

    private fun getFIFOStates(): List<PageReplacementState> {
        val empty = PageReplacementState.EMPTY
        val memory = MutableList(memoryCapacity) { empty }
        var i = 0
        var pageFaults = 0
        val states = mutableListOf<PageReplacementState>()
        requestPages.forEachIndexed { index, page ->
            if (page !in memory) {
                pageFaults++
                val replacedPage = memory[i]
                memory[i] = page
                states.add(
                    PageReplacementState(
                        memory = memory.map { it },
                        pageFault = true,
                        replaceMemoryCell = i,
                        totalFaultPages = pageFaults,
                        replacedPage = replacedPage,
                        loadedPageRequestNo = index,
                        faultPage = page
                    )

                )
                i = (i + 1) % memoryCapacity // Circular increment
            } else {
                states.add(
                    PageReplacementState(
                        memory = memory.map { it },
                        pageFault = false,
                        replaceMemoryCell = i,
                        totalFaultPages = pageFaults
                    )
                )
            }
        }
        return states
    }

    fun getState(type: ReplacementType): List<PageReplacementState> {
        if(type ==ReplacementType.FIFO)
            return getFIFOStates()
        val empty = PageReplacementState.EMPTY
        val memory = MutableList(memoryCapacity) { empty }
        var pageFaults = 0
        val states = mutableListOf<PageReplacementState>()
        requestPages.forEachIndexed { index, page ->
            if (page !in memory) {
                pageFaults++
                val replacementIndex: Int
                if (index < memoryCapacity)
                    replacementIndex = index
                else {
                    replacementIndex = when (type) {
                        ReplacementType.LRU -> leastRecentlyUsedIndex(
                            memoryPages = memory,
                            usedPages = requestPages.subList(0, index)
                        )

                        ReplacementType.MRU -> mostRecentlyUsedIndex(
                            memoryPages = memory,
                            usedPages = requestPages.subList(0, index)
                        )

                        ReplacementType.OPTIMAL -> optimalPageReplacementIndex(
                            memoryPages = memory,
                            futurePages = if (index < requestPages.lastIndex)
                                requestPages.subList(index + 1, requestPages.lastIndex)
                            else emptyList()
                        )
                        else -> {0}
                    }

                }
                val replacedPage = memory[replacementIndex]
                memory[replacementIndex] = page
                states.add(
                    PageReplacementState(
                        memory = memory.map { it },
                        pageFault = true,
                        replaceMemoryCell = replacementIndex,
                        totalFaultPages = pageFaults,
                        replacedPage = replacedPage,
                        loadedPageRequestNo = index,
                        faultPage = page
                    )

                )

            } else {
                states.add(
                    PageReplacementState(
                        memory = memory.map { it },
                        pageFault = false,
                        totalFaultPages = pageFaults
                    )
                )
            }
        }
        return states
    }

}


fun main() {
    val pageRequests = listOf(7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1)
    val algorithm = PageReplacementAlgorithms(pageRequests, 4)
    algorithm.getState(ReplacementType.OPTIMAL).forEach {
        println(it)
    }

}


/*
MRU=Most recent used
takes the current memory cell element
takes the request no from 0 to current at which we have a miss
we have given the memory current pages as list of integer
we have given the previous page from 0 to current requestNo-1 as the used list
find the which element have recently used then return the index of the
recycle uses page index in the memory cell


 */