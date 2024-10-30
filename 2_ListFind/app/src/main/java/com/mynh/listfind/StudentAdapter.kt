package com.mynh.listfind

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    private var students = listOf<Student>()
    private var filteredStudents = listOf<Student>()

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val studentIdTextView: TextView = itemView.findViewById(R.id.textViewStudentId)

        fun bind(student: Student) {
            nameTextView.text = student.name
            studentIdTextView.text = student.studentId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(filteredStudents[position])
    }

    override fun getItemCount() = filteredStudents.size

    fun setData(newStudents: List<Student>) {
        students = newStudents
        filteredStudents = newStudents
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        if (query.length <= 2) {
            filteredStudents = students
        } else {
            filteredStudents = students.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.studentId.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}
