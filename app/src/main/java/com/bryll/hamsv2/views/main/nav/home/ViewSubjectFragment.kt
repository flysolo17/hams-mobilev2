package com.bryll.hamsv2.views.main.nav.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.navArgs
import com.bryll.hamsv2.R
import com.bryll.hamsv2.databinding.FragmentViewSubjectBinding
import com.bryll.hamsv2.models.Grades
import com.bryll.hamsv2.models.Schedule
import com.bryll.hamsv2.utils.UiState
import com.bryll.hamsv2.utils.getAMPM
import com.bryll.hamsv2.viewmodels.ClassesViewModel
import com.bumptech.glide.Glide

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

        generateGrades("First Grading",args.grades.firstQuarter!!)
        generateGrades("Second Grading",args.grades.secondQuarter!!)
        generateGrades("Third Grading",args.grades.thirdQuarter!!)
        generateGrades("Fourth Grading",args.grades.fourthQuarter!!)
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
}