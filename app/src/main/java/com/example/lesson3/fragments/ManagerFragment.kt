package com.example.lesson3.fragments

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson3.MainActivity
import com.example.lesson3.NamesOfFragment
import com.example.lesson3.R
import com.example.lesson3.data.Manager
import com.example.lesson3.databinding.FragmentFacultyBinding
import com.example.lesson3.interfaces.MainActivityCallbacks
import java.lang.Exception

const val FACULTY_TAG = "com.example.lesson3.FacultyFragment"
const val FACULTY_TITLE="Универ"

class FacultyFragment:Fragment(), MainActivity.Edit {

    interface Callback{
        fun newTitle(_title: String)
    }

    companion object{
//        fun newInstance()= FacultyFragment()
        private var INSTANCE : FacultyFragment?= null
        fun getInstance(): FacultyFragment{
            if (INSTANCE == null) INSTANCE= FacultyFragment()
            return INSTANCE ?: throw Exception("FacultyFragment не создан")
        }
    }

    private lateinit var viewModel: ManagerViewModel
    private var _binding: FragmentFacultyBinding?=null
    val binding
        get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ma = requireActivity() as MainActivityCallbacks
        ma.newTitle("Список Ресторанов")
        _binding = FragmentFacultyBinding.inflate(inflater,container,false)
        binding.rvFaculty.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ManagerViewModel::class.java)
        viewModel.managerList.observe(viewLifecycleOwner){
            binding.rvFaculty.adapter=FacultyAdapter(it)
        }
    }

    override fun append() {
        editFaculty()
    }

    override fun update() {
        editFaculty(viewModel.faculty?.name ?: "")
    }

    override fun delete() {
        deleteDialog()
    }

    private fun deleteDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление")
            .setMessage("Вы действительно хотите удалить ресторан ${viewModel.faculty?.name ?: ""}?")
            .setPositiveButton("ДА") {_, _ ->
                viewModel.deleteFaculty()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editFaculty(facultyName : String=""){
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog__string, null)
        val messageText = mDialogView.findViewById<TextView>(R.id.tvInfo)
        val inputString = mDialogView.findViewById<EditText>(R.id.etString)
        inputString.setText(facultyName)
        messageText.text="Укажите название ресторана"

        AlertDialog.Builder(requireContext())
            .setTitle("ИЗМЕНЕНИЕ ДАННЫХ")
            .setView(mDialogView)
            .setPositiveButton("ok"){_, _ ->
                if (inputString.text.isNotBlank()) {
                    if (facultyName.isBlank())
                        viewModel.appendFaculty(inputString.text.toString())
                    else
                        viewModel.updateFaculty(inputString.text.toString())
                }
            }
            .setNegativeButton("otmena", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private inner class FacultyAdapter(private val items: List<Manager>)
        : RecyclerView.Adapter<FacultyAdapter.ItemHolder> () {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): FacultyAdapter.ItemHolder {
            val view = layoutInflater.inflate(R.layout.element_faculty_list, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount(): Int= items.size

        override fun onBindViewHolder(holder: FacultyAdapter.ItemHolder, position: Int) {
            holder.bind(viewModel.managerList.value!![position])
        }

        private var lastView: View? = null

        private fun updateCurrentView(view: View){
            lastView?.findViewById<ConstraintLayout>(R.id.clFaculty)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            lastView = view
            view.findViewById<ConstraintLayout>(R.id.clFaculty).setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.mygray)
            )
        }

        private inner class ItemHolder(view: View)
            : RecyclerView.ViewHolder(view) {
                private lateinit var manager: Manager
                fun bind(manager: Manager){
                    this.manager= manager
                    val tv = itemView.findViewById<TextView>(R.id.tvFaculty)
                    tv.text= manager.name
                    if (manager == viewModel.faculty)
                        updateCurrentView(itemView)
                    tv.setOnClickListener{
                        viewModel.setFaculty(manager)
                        updateCurrentView(itemView)
                    }
                    tv.setOnLongClickListener {
                        tv.callOnClick()
                        (requireActivity() as MainActivityCallbacks).showFragment(NamesOfFragment.CLIENT)
                        true
                    }
                }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivityCallbacks).newTitle("Список ресторанов")
    }
}