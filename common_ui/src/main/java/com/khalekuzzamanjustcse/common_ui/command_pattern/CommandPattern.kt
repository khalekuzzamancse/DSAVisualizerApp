package com.khalekuzzamanjustcse.common_ui.command_pattern

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface Command {

    fun execute()
    fun undo()
    fun redo()
    //redo=execute most of the times
}


/*
Implementing the undo manager,since we can use coroutine do do operation so
to avoid multiple threads can do  undo and redo at a time we used synchronization.
to avoid dead lock situation avoid nested synchronization and choose a better lock key;so
that the both synchronization block get locks at a time and use the same lock object to both block
so that the both block can get the the lock at a time.
 */



class UndoManager {
    //
    var undoAvailable by mutableStateOf(false)
        private set
    var redoAvailable by mutableStateOf(false)
    private set
    private var undoStack = emptyList<Command>()
        set(value) {
            field = value
            undoAvailable = undoStack.isNotEmpty()
        }
    private var redoStack = emptyList<Command>()
        set(value) {
            field = value
            redoAvailable = redoStack.isNotEmpty()
        }
    private val lock = Any()


    fun execute(command: Command) {
        command.execute()
        undoStack = undoStack + command
    }

    fun undo() {
        synchronized(lock) {
            undoStack.lastOrNull()?.let { toUndo ->
                undoStack = undoStack.dropLast(1)
                toUndo.undo()
                redoStack = redoStack + toUndo
            }
        }

    }

    fun redo() {
        synchronized(lock) {
            redoStack.lastOrNull()?.let { toRedo ->
                redoStack = redoStack.dropLast(1)
                toRedo.redo()
                undoStack = undoStack + toRedo
            }

        }

    }
}