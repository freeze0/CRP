package com.example.lesson3.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import com.example.lesson3.repository.AppRepository
import java.util.Date

class InvoiceViewModel : ViewModel() {
    var invoiceList: MutableLiveData<List<Invoice>> = MutableLiveData()

    private var _invoice: Invoice?= null
    val student
        get()= _invoice

    var client: Client? = null
    fun update_info(typeSort: Int){
        if (typeSort == 1)
            AppRepository.getInstance().listOfInvoice.observeForever{
                invoiceList.postValue(AppRepository.getInstance().getClientInvoices(this.client!!.id))
            }
        if (typeSort == 2)
            AppRepository.getInstance().listOfInvoice.observeForever{
                invoiceList.postValue(AppRepository.getInstance().getClientInvoicesId(client!!.id))
            }
        if (typeSort == 3)
            AppRepository.getInstance().listOfInvoice.observeForever{
                invoiceList.postValue(AppRepository.getInstance().getclientInvoicesSum(client!!.id))
            }
        if (typeSort == 4)
            AppRepository.getInstance().listOfInvoice.observeForever{
                invoiceList.postValue(AppRepository.getInstance().getclientInvoicesDateExec(client!!.id))
            }
        AppRepository.getInstance().invoice.observeForever{
            _invoice=it
        }
    }
    fun search(s: String){
        AppRepository.getInstance().listOfInvoice.observeForever{
            invoiceList.postValue(AppRepository.getInstance().getSearch(s, client!!.id))
        }
    }
    fun set_Group(client: Client) {
        this.client = client
        AppRepository.getInstance().listOfInvoice.observeForever {
            invoiceList.postValue(AppRepository.getInstance().getClientInvoices(client.id))
        }
        AppRepository.getInstance().invoice.observeForever{
            _invoice=it
        }
    }

    fun deleteStudent() {
        if(student!=null)
            AppRepository.getInstance().deleteInvoice(student!!)
    }
    fun update_info(){
        AppRepository.getInstance().fetchInvoice()
    }

    fun appendStudent(date1: Date, id_invoice: String, sum_total: Int, date_exec: Date, handed: String, accepted: String, add_info: String, basis_doc: String){
        val invoice = Invoice()
        invoice.date1 = date1
        invoice.sum_total = sum_total
        invoice.id_invoice = id_invoice
        invoice.date_exec = date_exec
        invoice.handed = handed
        invoice.clientID = client!!.id
        invoice.accepted = accepted
        invoice.add_info = add_info
        invoice.basis_doc = basis_doc
        AppRepository.getInstance().addInvoice(invoice)
    }

    fun updateStudent(date1: Date, id_invoice: String, sum_total: Int, date_exec: Date, handed: String, accepted: String, add_info: String, basis_doc: String){
        if (_invoice!=null){
            _invoice!!.date1 = date1
            _invoice!!.id_invoice = id_invoice
            _invoice!!.sum_total = sum_total
            _invoice!!.date_exec = date_exec
            _invoice!!.handed = handed
            _invoice!!.accepted = accepted
            _invoice!!.add_info = add_info
            _invoice!!.basis_doc = basis_doc
            AppRepository.getInstance().updateInvoice(_invoice!!)
        }
    }

    fun setCurrentStudent(invoice: Invoice){
        AppRepository.getInstance().setCurrentInvoice(invoice)
    }

}