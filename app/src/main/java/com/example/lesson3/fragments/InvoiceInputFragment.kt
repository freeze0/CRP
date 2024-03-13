package com.example.lesson3.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lesson3.data.Invoice
import com.example.lesson3.databinding.FragmentStudentInput2Binding
import com.example.lesson3.repository.AppRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat

private const val ARG_PARAM1 = "student_param"

class InvoiceInputFragment : Fragment() {
    private lateinit var invoice: Invoice
    private lateinit var _binding : FragmentStudentInput2Binding
    var flag_validation = true
    var flag = false
    val binding
        get()=_binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val param1 = it.getString(ARG_PARAM1)
            if (param1==null)
                invoice=Invoice()
            else{
                val paramType= object : TypeToken<Invoice>(){}.type
                invoice = Gson().fromJson<Invoice>(param1, paramType)
            }
        }
    }

    fun validation (et1: String, et2: String, et3: String, et4: String, et5: String, et6: String, et7: String, et8: String){
        flag_validation = true
        if (et1.isBlank()) {
            binding.etDate1.requestFocus()
            binding.etDate1.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et2.isBlank() or !(et2.toString().toInt() > 1)) {
            binding.etSum.requestFocus()
            binding.etSum.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et3.isBlank()) {
            binding.etIDInv.requestFocus()
            binding.etIDInv.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et4.isBlank()) {
            binding.etDateExec.requestFocus()
            binding.etDateExec.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et5.isBlank()) {
            binding.etHanded.requestFocus()
            binding.etHanded.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et6.isBlank()) {
            binding.etAccepted.requestFocus()
            binding.etAccepted.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et7.isBlank()) {
            binding.etInfo.requestFocus()
            binding.etInfo.setError("Введите корректные данные")
            flag_validation = false
        }
        if (et8.isBlank()) {
            binding.etBasisDoc.requestFocus()
            binding.etBasisDoc.setError("Введите корректные данные")
            flag_validation = false
        }
    }
    @SuppressLint("SimpleDateFormat")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        flag = !(invoice.add_info.toString().isBlank())
        _binding = FragmentStudentInput2Binding.inflate(inflater, container, false)
        binding.btSave.text = if (flag) "Изменить" else "Добавить"
        binding.etDate1.setText(invoice.date1.toString())
        binding.etSum.setText(invoice.sum_total.toString())
        binding.etIDInv.setText(invoice.id_invoice)
        binding.etDateExec.setText(invoice.date_exec.toString())
        binding.etHanded.setText(invoice.handed)
        binding.etAccepted.setText(invoice.accepted)
        binding.etInfo.setText(invoice.add_info)
        binding.etBasisDoc.setText(invoice.basis_doc)


        binding.btCancel.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btSave.setOnClickListener {
            validation(
                binding.etDate1.text.toString(),
                binding.etSum.text.toString(),
                binding.etIDInv.text.toString(),
                binding.etDateExec.text.toString(),
                binding.etHanded.text.toString(),
                binding.etAccepted.text.toString(),
                binding.etInfo.text.toString(),
                binding.etBasisDoc.text.toString()
            )
            if (flag_validation) {
                invoice.date1.time = SimpleDateFormat("dd.MM.yyyy").parse(binding.etDate1.text.toString())?.time ?: invoice.date1.time
                // invoice.date1 = binding.etDate1.date
                invoice.sum_total = binding.etSum.text.toString().toInt()
                invoice.id_invoice = binding.etIDInv.text.toString()
                // invoice.date_exec = binding.etDateExec.text
                invoice.date_exec.time = SimpleDateFormat("dd.MM.yyyy").parse(binding.etDateExec.text.toString())?.time ?: invoice.date_exec.time
                invoice.handed = binding.etHanded.text.toString()
               // invoice.clientID = client!!.id
                invoice.accepted = binding.etAccepted.text.toString()
                invoice.add_info = binding.etInfo.text.toString()
                invoice.basis_doc = binding.etBasisDoc.text.toString()
                if (flag)
                    AppRepository.getInstance().updateInvoice(invoice)
                else
                    AppRepository.getInstance().addInvoice(invoice)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance(invoice: Invoice) =
            InvoiceInputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, Gson().toJson(invoice))
                }
            }
    }
}