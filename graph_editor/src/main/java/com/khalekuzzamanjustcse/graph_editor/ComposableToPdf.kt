package com.khalekuzzamanjustcse.graph_editor

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Objects


@Preview
@Composable
private fun ComposableToPdfPreview() {
    val context = LocalContext.current
    ComposableToPdf(content = {
        Column(modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawCircle(Color.Red, radius = 30f, center = Offset(100f, 100f))
            }
        ) {

        }
    }) {
        writePdfDocumentToAppSpecificStorage(it, context)
    }
}

@Composable
fun ComposableToPdf(
    content: @Composable () -> Unit = {},
    onPdfCreated: (PdfDocument) -> Unit = {}
) {
    var bitMapCreated by remember {
        mutableStateOf(false)
    }
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    if (bitMapCreated) {
        bitmap?.let {
            val document = bitmapToPdf(it)
            onPdfCreated(document)
        }
    }
    val snapShot = captureBitmap {
        content()
    }
    LaunchedEffect(Unit) {
        bitmap = snapShot.invoke()
        bitMapCreated = true
    }


}

@Composable
private fun bitmapToPdf(bitmap: Bitmap): PdfDocument {
    val document = PdfDocument()
    val page = document
        .startPage(
            PdfDocument
                .PageInfo
                .Builder(bitmap.width, bitmap.height, 1)
                .create()
        )
    val canvas = page.canvas
    canvas.drawBitmap(bitmap, 0f, 0f, null)
    document.finishPage(page)
    return document
}

@Composable
private fun captureBitmap(
    content: @Composable () -> Unit,
): () -> Bitmap {

    val context = LocalContext.current
    val composeView = remember { ComposeView(context) }
    fun captureBitmap(): Bitmap {
        return composeView.drawToBitmap()
    }
    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content.invoke()
                }
            }
        }
    )
    return ::captureBitmap
}

fun writePdf(context: Context, document: PdfDocument,onSuccess: () -> Unit={}) {
    val outputStream: OutputStream
    try {
        val contentResolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues()
        val dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")
        val now = LocalDateTime.now()
        val dateSuffix = "_" + dtf.format(now)

        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Graph$dateSuffix.pdf")
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_DOCUMENTS + File.separator + "GraphFolder"
        )
        val pdfUri =
            contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        outputStream = pdfUri?.let { contentResolver.openOutputStream(it) }!!
        Objects.requireNonNull(outputStream)
        try {
            document.writeTo(outputStream)
            document.close()
            onSuccess()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun writePdfDocumentToAppSpecificStorage(document: PdfDocument, context: Context) {
    try {
        val fos: FileOutputStream = context.openFileOutput("yyy.pdf", Context.MODE_PRIVATE)
        document.writeTo(fos)
        fos.close()
        document.close()
    } catch (_: Exception) {
    }
}
