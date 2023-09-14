package com.khalekuzzamanjustcse.dsavisulizer.page_replacement_algorithms

data class PageReplacementState(
    val memory: List<Int>,
    val pageFault: Boolean=false,
    val loadedPageRequestNo: Int=EMPTY,
    val faultPage: Int=EMPTY,
    val replacedPage: Int=EMPTY,
    val totalFaultPages: Int=0,
    val replaceMemoryCell: Int=EMPTY,
){
    companion object{
        const val EMPTY=-111
    }
}