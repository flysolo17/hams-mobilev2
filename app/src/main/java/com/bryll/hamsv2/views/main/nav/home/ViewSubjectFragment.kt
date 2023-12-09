package com.bryll.hamsv2.views.main.nav.home

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels

import androidx.navigation.fragment.navArgs
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentViewSubjectBinding
import com.bryll.hamsv2.models.Classes

import com.bryll.hamsv2.models.Schedule
import com.bryll.hamsv2.models.Subjects
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.getAMPM
import com.bryll.hamsv2.viewmodels.ClassesViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ViewSubjectFragment : Fragment() {
    private lateinit var  binding : FragmentViewSubjectBinding
    private val args : ViewSubjectFragmentArgs by navArgs()
    private val classesViewModel by activityViewModels<ClassesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewSubjectBinding.inflate(inflater,container,false)
        binding.imageBackground.setImageResource(args.image)
        binding.textSubjectTitle.text = args.subject.name
        binding.textTeachername.text = args.users.name
        Glide.with(binding.root.context).load(args.users.profile).into(binding.imageProfile)

        generateGrades("First Grading",args.grades.firstQuarter ?: 0.00)
        generateGrades("Second Grading",args.grades.secondQuarter ?: 0.00)
        generateGrades("Third Grading",args.grades.thirdQuarter ?: 0.00)
        generateGrades("Fourth Grading",args.grades.fourthQuarter ?: 0.00)
      return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        classesViewModel.currentClass.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.ERROR -> {

                }
                is UiState.LOADING -> {


                }
                is UiState.SUCCESS -> {
                    state.data?.let {
                        it.curriculum.map {curr->
                            if (args.subject.id == curr.subjectID) {
                                binding.schedCount.text = curr.schedules.size.toString()
                                curr.schedules.map {sched->
                                    generateSched(sched = sched)
                                }
                            }
                        }
                    }
                }
            }
        }
        binding.buttonDownload.setOnClickListener {
            generateAndDownloadPDF(view.context,binding.layoutGrades,args.subject)
        }
    }
    private fun generateSched(sched : Schedule) {
        val view = LayoutInflater.from(binding.root.context).inflate(R.layout.list_schedule,null)
        view.findViewById<TextView>(R.id.textDay).text = sched.day
        view.findViewById<TextView>(R.id.textTime).text = "${sched.startTime} - ${sched.endTime} ${getAMPM(sched.endTime)}"
        binding.layoutSched.addView(view)
    }

    private fun generateGrades(grading : String,grades: Double) {
        val view = LayoutInflater.from(binding.root.context).inflate(R.layout.list_grades2,null)
        view.findViewById<TextView>(R.id.textGrading).text = grading
        view.findViewById<TextView>(R.id.textGrade).text = grades.toString()
        binding.layoutGrades.addView(view)
    }




    private fun generateAndDownloadPDF(context: Context  ,view: View,subject: Subjects) {
        // Define the PDF file name and path
        val pdfFileName = "${subject.name}.pdf"
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val pdfFile = File(directory, pdfFileName)
        try {
            val pdfWriter = PdfWriter(pdfFile)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            val bitmap = viewToBitmap(view)


            val image = ImageDataFactory.create(bitmapToByteArray(bitmap))
            val data = com.itextpdf.layout.element.Image(image)
            document.add(data)
            document.close()
            Toast.makeText(context,"Saved to ${pdfFile.absolutePath}",Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun sharePdfFile(context: Context, pdfFile: File) {
        val uri = Uri.fromFile(pdfFile)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        try {
            context.startActivity(Intent.createChooser(intent, "Share PDF using"))
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(
                binding.root,
                "No app found to handle the PDF file",
                Snackbar.LENGTH_SHORT
            ).show()
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
}