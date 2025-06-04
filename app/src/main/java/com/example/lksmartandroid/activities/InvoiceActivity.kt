package com.example.lksmartandroid.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract.Directory
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lksmartandroid.R
import com.example.lksmartandroid.adapters.InvoiceAdapter
import com.example.lksmartandroid.databinding.ActivityInvoiceBinding
import com.example.lksmartandroid.services.ClientService
import java.net.URI
import androidx.core.graphics.createBitmap
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InvoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInvoiceBinding
    private lateinit var invoiceAdapter: InvoiceAdapter

    private lateinit var savedFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAdapter()
        with(binding) {
            ibBack.setOnClickListener { finish() }
            btnDone.setOnClickListener { finish() }
            btnSave.setOnClickListener { savePDF() }
            btnShare.setOnClickListener { sharePDF() }
        }
    }

    private fun savePDF() {
        try {
            with(binding) {
                val bitmap = createBitmap(layoutInvoice.width, layoutInvoice.height)
                val canvas = Canvas()
                layoutInvoice.draw(canvas)

                val docDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                if (!docDir.exists()) docDir.mkdirs()

                val pdfFile = File(
                    docDir,
                    "Transaction-${
                        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    }"
                )
                val outputSteam = FileOutputStream(pdfFile)

                val pdfDoc = PdfDocument()
                val pageInfo = PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
                val page = pdfDoc.startPage(pageInfo)

                page.canvas.drawBitmap(bitmap, 0f, 0f, null)
                pdfDoc.apply {
                    finishPage(page)
                    writeTo(outputSteam)
                    close()
                    outputSteam.close()
                }
                savedFile = pdfFile
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun sharePDF() {
        try {
            val uri: Uri = FileProvider.getUriForFile(this, this.packageName+".fileprovider", savedFile)
            startActivity(Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            })
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun setupAdapter() = binding.rvInvoice.apply {
        invoiceAdapter = InvoiceAdapter()
        adapter = invoiceAdapter
        invoiceAdapter.cart = ClientService.cart
        invoiceAdapter.notifyDataSetChanged()
    }
}