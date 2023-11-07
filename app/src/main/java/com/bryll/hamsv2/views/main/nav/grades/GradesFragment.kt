package com.bryll.hamsv2.views.main.nav.grades

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryll.hamsv2.MainActivity

import com.bryll.hamsv2.databinding.FragmentGradesBinding
import com.bryll.hamsv2.utils.LoadingDialog
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.viewmodels.EnrollmentViewModel
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class GradesFragment : Fragment(), OnGradeClick {
    private lateinit var binding : FragmentGradesBinding
    private val enrollmentViewModel : EnrollmentViewModel by activityViewModels()
    private lateinit var loadingDialog : LoadingDialog




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGradesBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enrollmentViewModel.enrollmentList.observe(viewLifecycleOwner) {state->
            when(state) {
                is UiState.ERROR -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(view.context,state.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting all enrollments")
                }
                is UiState.SUCCESS -> {
                    Log.d("grade","test")
                    Log.d("grade",state.data.toString())
                    loadingDialog.closeDialog()
                    binding.recyclerviewGrades.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = GradesAdapter(view.context,state.data,this@GradesFragment)
                    }
                }
            }
        }
    }



    override fun downloadGrade(context: Context, view: View) {
        generateAndDownloadPDF(context,view)
    }

    private fun generateAndDownloadPDF(context: Context  ,view: View) {
        // Define the PDF file name and path
        val pdfFileName = "sample.pdf"
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val pdfFile = File(directory, pdfFileName)

        try {
            val pdfWriter = PdfWriter(pdfFile)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            val bitmap = viewToBitmap(view)

            // Create an iText Image object from the bitmap
            val image = ImageDataFactory.create(bitmapToByteArray(bitmap))
            val data = Image(image)
            document.add(data)


            document.close()
            Toast.makeText(context,"success",Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

//    private fun requestMediaStorePermission(context: Context, pdfFile: File, fileName: String) {
//        try {
//            val values = ContentValues().apply {
//                put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
//                put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
//            }
//
//            val contentResolver = context.contentResolver
//            val collection = MediaStore.Files.getContentUri("external")
//
//            // Insert the PDF file into MediaStore
//            val uri = contentResolver.insert(collection, values)
//
//            if (uri != null) {
//                contentResolver.openOutputStream(uri)?.use { outputStream ->
//                    pdfFile.inputStream().use { inputStream ->
//                        inputStream.copyTo(outputStream)
//                    }
//                    Log.d("MediaStore", "PDF saved to MediaStore: $uri")
//                }
//            } else {
//                Log.e("MediaStore", "Failed to insert PDF into MediaStore")
//            }
//        } catch (e: Exception) {
//            Log.e("MediaStore", "Error: ${e.message}")
//        }
//    }
//


}