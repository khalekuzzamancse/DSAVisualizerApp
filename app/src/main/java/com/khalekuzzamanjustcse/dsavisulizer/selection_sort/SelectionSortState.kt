package com.khalekuzzamanjustcse.dsavisulizer.selection_sort


/*
1. selectionSort(arr):
2.   n = length of arr
3.   for i from 0 to n - 1
4.     minIndex = i
5.     for j from i + 1 to n - 1
6.       if arr[j] < arr[minIndex]
7.         minIndex = j
8.     swap(arr[i], arr[minIndex])

 */
fun getSelectionSortExecutionSequence(
    arrayCellValue: List<Int>,
): List<SelectionSortState> {
    var sortedList =arrayCellValue
    val sequences = mutableListOf<SelectionSortState>()
    sequences.add(
        SelectionSortState(
            currentList = sortedList,
            executedLineNo = 1
        )
    )//1: selectionSort(arr):

    val n = sortedList.size
    sequences.add(
        SelectionSortState(
            currentList = sortedList,
            executedLineNo = 2
        )
    )// 2:  n = length(arr)
    sequences.add(
        SelectionSortState(
            currentList = sortedList,
            executedLineNo = 3,
            i = 0
        )
    ) //3:  for i from 0 to n-1
    for (i in 0 until n - 1) {
        var minIndex = i // 4: minIndex = i
        sequences.add(
            SelectionSortState(
                currentList = sortedList,
                executedLineNo = 4,
                i = i,
                minIndex = minIndex,
            )
        )

        sequences.add(
            SelectionSortState(
                currentList = sortedList,
                executedLineNo = 5,
                i = i,
                minIndex = minIndex,
                j = i + 1
            )
        ) // 5: for j from i+1 to n:
        for (j in i + 1 until n) {
            sequences.add(
                SelectionSortState(
                    currentList = sortedList,
                    executedLineNo = 6,
                    i = i,
                    minIndex = minIndex,
                    j = j
                )
            )   //6: if arr[j] < arr[minIndex]:
            if (sortedList[j] < sortedList[minIndex]) {
                minIndex = j
                sequences.add(
                    SelectionSortState(
                        currentList = sortedList,
                        executedLineNo = 7,
                        i = i,
                        minIndex = minIndex,
                        j = j
                    )
                ) //7: minIndex = j
            }

            sequences.add(
                SelectionSortState(
                    currentList = sortedList,
                    executedLineNo = 5,
                    i = i,
                    minIndex = minIndex,
                    j = j + 1
                )
            ) // 5: for j from i+1 to n:
        }
        if (minIndex != i) {
            val tempList = sortedList.toMutableList()
            val temp = sortedList[i]
            tempList[i] = tempList[minIndex]
            tempList[minIndex] = temp
            sortedList = tempList
        }

        sequences.add(
            SelectionSortState(
                currentList = sortedList,
                executedLineNo = 8,
                i = i,
                minIndex = minIndex,
                shouldSwap = true,
                sortedTill = i
            )
        ) //8: swap(arr[i], arr[minIndex])
        sequences.add(
            SelectionSortState(
                currentList = sortedList,
                executedLineNo = 3,
                i = i + 1
            )
        )//3:  for i from 0 to n-1
    }
    return sequences
}

data class SelectionSortState(
    val executedLineNo: Int,
    val currentList: List<Int> = emptyList(),
    val i: Int = GARBAGE,
    val j: Int = GARBAGE,
    val minIndex: Int = GARBAGE,
    val temp: Int = GARBAGE,
    val shouldSwap: Boolean = false,
    val sortedTill:Int= GARBAGE
) {
    companion object {
        const val GARBAGE = -1
    }
}