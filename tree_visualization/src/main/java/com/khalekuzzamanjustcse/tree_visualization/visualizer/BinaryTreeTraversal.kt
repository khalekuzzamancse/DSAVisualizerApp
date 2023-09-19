package com.khalekuzzamanjustcse.tree_visualization.visualizer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.tree_visualization.BinaryTreeChildType
import com.khalekuzzamanjustcse.tree_visualization.ChildPickerData
import com.khalekuzzamanjustcse.tree_visualization.Tree
import com.khalekuzzamanjustcse.tree_visualization.TreeTraversalIntermediateDS
import com.khalekuzzamanjustcse.tree_visualization.TreeTraversalState
import com.khalekuzzamanjustcse.tree_visualization.bsfSequence
import com.khalekuzzamanjustcse.tree_visualization.dfsSequence
import com.khalekuzzamanjustcse.tree_visualization.inorderTraversal
import com.khalekuzzamanjustcse.tree_visualization.laying_node.Node
import com.khalekuzzamanjustcse.tree_visualization.postorderTraversal
import com.khalekuzzamanjustcse.tree_visualization.preorderTraversal
import com.khalekuzzamanjustcse.tree_visualization.tree_input.TreeInput
import com.khalekuzzamanjustcse.tree_visualization.ui.common.PopupWithRadioButtons


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TreeVisualizerPreview() {
    val size: Dp = 45.dp
    val sizePx = size.value * LocalDensity.current.density
    var openDialog by remember {
        mutableStateOf(false)
    }
    val tree by remember {
        mutableStateOf(Tree(Node(value = 1, sizePx = sizePx)))
    }

    var selectedChild by
    remember {
        mutableStateOf(BinaryTreeChildType.LEFT)
    }

//lambdas
    val onChildSelect: () -> BinaryTreeChildType = {
        selectedChild
    }

    //variables states
    //
    var popped by remember {
        mutableStateOf(false)
    }
    var newlyAdded by remember { mutableStateOf(emptyList<Int>()) }
    var type by remember { mutableStateOf(TreeTraversalIntermediateDS.Queue) }
    var processingNode by remember { mutableStateOf("") }

    //
    var traversalIterator by
    remember {
        mutableStateOf(bsfSequence(tree.getRoot(), onChildSelect).iterator())
    }

    val onNodeProcess: (TreeTraversalState) -> Unit = { currentState ->

        val tempList = newlyAdded.toMutableList()
        currentState.newlyAddedNodes.forEach {
            if (it != null)
                tempList.add(it.value)
        }
        newlyAdded=tempList
        if(currentState.processingNode!=null){
            popped=true
            processingNode="${currentState.processingNode.value}"
        }



        val node = currentState.processingNode
        if (node != null)
            node.color.value = Color.Blue
    }
    var availableChild by remember {
        mutableStateOf(listOf(""))
    }
    var dialogText by remember {
        mutableStateOf("")
    }
    var childSelectionMode by remember { mutableStateOf(false) }


    val jumpNextStep: () -> Unit = {
        if (traversalIterator.hasNext()) {
            val state = traversalIterator.next()
            if (state is TreeTraversalState) {

                onNodeProcess(state)

            } else if (state is ChildPickerData) {
                state.parent.color.value = Color.Blue
                dialogText = "Processing :${state.parent.value}\n it has two children " +
                        "choose which is enqueued 1st"
                availableChild = state.child.map { "${it.value}" }
                openDialog = true
            }
        }
    }

//
    var selectedTraversal by remember {
        mutableStateOf(TreeTraversalType.BFS)
    }
    var treeInputModeOn by remember { mutableStateOf(true) }
    var topAppbarTitle by remember { mutableStateOf("BFS ") }

    val onTraversalTypeChanged: (String) -> Unit = {
        topAppbarTitle = it
        tree.resetTreeColor()
        selectedTraversal = it
        traversalIterator = when (it) {
            TreeTraversalType.INORDER -> inorderTraversal(tree.getRoot()).iterator()
            TreeTraversalType.PREORDER -> preorderTraversal(tree.getRoot()).iterator()
            TreeTraversalType.POSTORDER -> postorderTraversal(tree.getRoot()).iterator()
            TreeTraversalType.BFS -> bsfSequence(tree.getRoot(), onChildSelect).iterator()
            TreeTraversalType.DFS -> dfsSequence(tree.getRoot()).iterator()
            else -> inorderTraversal(tree.getRoot()).iterator()
        }

    }

    Scaffold(
        topBar = {
            TraversalScreenTopAppbar(
                title = topAppbarTitle,
                onNextClick = jumpNextStep,
                onTraversalSelected = onTraversalTypeChanged,
                onChildSelectModeChange = { childSelectionMode = !childSelectionMode },
                childSelectionModeOn = childSelectionMode,
                isOnInputMode = treeInputModeOn,
                onInputComplete = { treeInputModeOn = false },

                )
        }
    ) { padding ->

        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
        ) {
            if (treeInputModeOn) {
                TreeInput(tree, size)
            } else {
                NextProcessingNodes(
                   processing = processingNode,
                    newlyAdded =newlyAdded,
                    removed = popped,
                    onRemovedFinished = {popped=false},
                    onAddedFinished = { newlyAdded=emptyList()  }
                )
                TreeVisualizer(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    root = tree.getRoot(),
                    size = size)
                PopupWithRadioButtons(
                    text = dialogText,
                    isOpen = openDialog && childSelectionMode,
                    options = availableChild,
                    onOptionSelected = {
                        selectedChild = if (it == availableChild.first())
                            BinaryTreeChildType.LEFT
                        else
                            BinaryTreeChildType.RIGHT
                        openDialog = false
                        jumpNextStep()
                    }
                )

            }
        }
    }


}

