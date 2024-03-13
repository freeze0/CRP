package com.example.lesson3.fragments

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson3.MainActivity
import com.example.lesson3.NamesOfFragment
import com.example.lesson3.R
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import com.example.lesson3.databinding.FragmentStudentBinding
import com.example.lesson3.interfaces.MainActivityCallbacks
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class InvoiceFragment : Fragment(){

    companion object {
        private lateinit var client: Client
        fun newInstance(client: Client): InvoiceFragment{
            this.client = client
            return InvoiceFragment()
        }
    }

    private lateinit var viewModel: InvoiceViewModel
    private lateinit var _binding : FragmentStudentBinding

    val binding
        get()= _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentStudentBinding.inflate(inflater, container, false)
        binding.rvStudent.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(this).get(InvoiceViewModel::class.java)
        viewModel.set_Group(client)
        viewModel.invoiceList.observe(viewLifecycleOwner){
            binding.rvStudent.adapter=StudentAdapter(it)
        }
        //### !!!!
        if (MainActivity.AuthStatus.userType == 1){
            binding.fabNewStudent.visibility = VISIBLE
        } else {
            binding.fabNewStudent.visibility = INVISIBLE
        }
        binding.fabNewStudent.setOnClickListener{
            editStudent(Invoice().apply { clientID = viewModel.client!!.id })
        }
        binding.btnSearch.setOnClickListener {
            viewModel.search(binding.etSearch.text.toString())
        }
    }

    private fun deleteDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Вы действительно хотите удалить эту накладную ${viewModel.student?.id_invoice?: ""}?")
            .setPositiveButton("да"){_,_ ->
                viewModel.deleteStudent()
            }
            .setNegativeButton("нет", null)
            .setCancelable(true)
            .create()
            .show()
    }
    private fun infoDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Полная информация")
            .setMessage("дата: ${viewModel.student?.date1 ?: ""} " +
                    "\n\nномер: ${viewModel.student?.id_invoice ?: ""}" +
                    "\n\nсумма: ${viewModel.student?.sum_total ?: ""} руб." +
                    "\n\nдата исполнения: ${viewModel.student?.date_exec ?: ""}" +
                    "\n\nгрузоотправитель: ${viewModel.student?.handed ?: ""}" +
                    "\n\nгрузополучатель: ${viewModel.student?.accepted ?: ""}" +
                    "\n\nдополнительная информация: ${viewModel.student?.add_info ?: ""}" +
                    "\n\nдокумент-основание: ${viewModel.student?.basis_doc ?: ""}")
            .setNegativeButton("скрыть", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editStudent(invoice: Invoice? = null){
        (requireActivity() as MainActivityCallbacks).showFragment(NamesOfFragment.INVOICE, invoice)
        (requireActivity() as MainActivityCallbacks).newTitle("Категория блюд ${viewModel.client!!.name}")
    }

    private inner class StudentAdapter(private val items: List<Invoice>)
        : RecyclerView.Adapter<StudentAdapter.ItemHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): StudentAdapter.ItemHolder {
            val view = layoutInflater.inflate(R.layout.element_student_list, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount(): Int= items.size

        override fun onBindViewHolder(holder: StudentAdapter.ItemHolder, position: Int) {
            holder.bind(viewModel.invoiceList.value!![position])
        }

        private var lastView: View? = null

        private fun updateCurrentView(view: View){
            lastView?.findViewById<ConstraintLayout>(R.id.clStudent)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.white))
            view.findViewById<ConstraintLayout>(R.id.clStudent).setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.mygray))
            lastView= view
        }

        private inner class ItemHolder(view: View)
            : RecyclerView.ViewHolder(view) {

                private lateinit var invoice: Invoice

                fun bind(invoice: Invoice){
                    this.invoice= invoice
                    if (invoice==viewModel.student)
                        updateCurrentView(itemView)
                    val tvDate1 = itemView.findViewById<TextView>(R.id.tvDate1)
                    tvDate1.text="дата: " + invoice.date1
                    val tvId = itemView.findViewById<TextView>(R.id.tvId)
                    tvId.text= "номер накладной: " +invoice.id_invoice.toString()
                    val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
                    tvPrice.text= "сумма: " + invoice.sum_total.toString() + "руб."
                    val tvDateEx = itemView.findViewById<TextView>(R.id.tvDateEx)
                    tvDateEx.text= "дата исполнения: " + invoice.date_exec.toString()

                    tvDate1.setOnLongClickListener {
                        tvDate1.callOnClick()
                        viewModel.update_info(1)
                        true
                    }
                    tvId.setOnLongClickListener{
                        tvId.callOnClick()
                        viewModel.update_info(2)
                        true
                    }
                    tvPrice.setOnLongClickListener{
                        tvPrice.callOnClick()
                        viewModel.update_info(3)
                        true
                    }
                    tvDateEx.setOnLongClickListener{
                        tvDateEx.callOnClick()
                        viewModel.update_info(4)
                        true
                    }


                    val cl = itemView.findViewById<ConstraintLayout>(R.id.clStudent)
                    cl.setOnClickListener {
                        viewModel.setCurrentStudent(invoice)
                        updateCurrentView(itemView)
                    }
                    itemView.findViewById<ImageButton>(R.id.ibEditStudent).setOnClickListener{
                        editStudent(invoice)
                    }
                    itemView.findViewById<ImageButton>(R.id.ibDeleteStudent).setOnClickListener{
                        deleteDialog()
                    }
                    itemView.findViewById<ImageButton>(R.id.ibInfo).setOnClickListener {
                        infoDialog()
                    }
                    if (MainActivity.AuthStatus.userType != 1){
                        itemView.findViewById<ImageButton>(R.id.ibEditStudent).isVisible = false
                        itemView.findViewById<ImageButton>(R.id.ibDeleteStudent).isVisible = false
                    }

//                    itemView.findViewById<ImageButton>(R.id.ibInfo).setOnClickListener {
////                        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
////                            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${invoice.phone}"))
////                            startActivity(intent)
////                        }
////                        else {
////                            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE), 2)
////                        }
//                    }

                    val llb = itemView.findViewById<LinearLayout>(R.id.llStudentButtons)
                    llb.visibility=View.INVISIBLE
                    llb?.layoutParams=llb?.layoutParams.apply { this?.width=1 }
                    val ib=itemView.findViewById<ImageButton>(R.id.ibInfo)
                    ib.visibility=View.INVISIBLE
                    cl.setOnLongClickListener{
                        cl.callOnClick()
                        llb.visibility=View.VISIBLE
                        ib.visibility=View.VISIBLE
                        MainScope().
                        launch{
                            val lp= llb?.layoutParams
                            lp?.width= 1
                            val ip=ib.layoutParams
                            ip.width=1
                            while(lp?.width!!<350){
                                lp?.width=lp?.width!!+35
                                llb?.layoutParams=lp
                                ip.width=ip.width+10
//                                if (ib.visibility==View.VISIBLE)
//                                    ib.layoutParams=ip
                                delay(50)
                            }
                        }
                        true
                    }
                }
        }
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}