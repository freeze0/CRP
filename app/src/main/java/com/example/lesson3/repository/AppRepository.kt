package com.example.lesson3.repository

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.lesson3.API.ListAPI
import com.example.lesson3.API.ListConnection
import com.example.lesson3.API.PostId
import com.example.lesson3.API.PostResult
import com.example.lesson3.API.PostUser
import com.example.lesson3.MyApplication
import com.example.lesson3.data.Managers
import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Clients
import com.example.lesson3.data.Invoice
import com.example.lesson3.data.Invoices
import com.example.lesson3.data.Spasibo
import com.example.lesson3.database.ListDatabase
import com.example.lesson3.myConsts.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository {
    companion object{
        private var INSTANCE: AppRepository?=null

        fun getInstance(): AppRepository {
            if (INSTANCE==null){
                INSTANCE= AppRepository()
            }
            return INSTANCE?:
            throw IllegalStateException("реп не иниц")
        }
    }

    var manager: MutableLiveData<Manager> = MutableLiveData()
    var client: MutableLiveData<Client> = MutableLiveData()
    var invoice: MutableLiveData<Invoice> = MutableLiveData()


    fun getManagerPosition(manager: Manager): Int=listOfManager.value?.indexOfFirst {
        it.id==manager.id } ?:-1

    fun getManagerPosition()=getManagerPosition(manager.value?: Manager())

    fun setCurrentManager(position:Int){
        if (position<0 || (listOfManager.value?.size!! <= position))
            return setCurrentManager(listOfManager.value!![position])
    }

    fun setCurrentManager(_manager: Manager){
        manager.postValue(_manager)
    }



    fun loadData(){
        fetchManagers()
    }

    fun getClientPosition(client: Client): Int=listOfClient.value?.indexOfFirst {
        it.id==client.id } ?:-1

    fun getClientPosition()=getClientPosition(client.value?: Client())

    fun setCurrentClient(position:Int){
        if (listOfClient.value==null || position<0 ||
            (listOfClient.value?.size!! <=position))
            return setCurrentClient(listOfClient.value!![position])
    }

    fun setCurrentClient(_client: Client){
        client.postValue(_client)
    }

    val managerClients
        get()= listOfClient.value?.filter { it.mangerID == (manager.value?.id ?: 0) }?.sortedBy { it.name }?: listOf()

    fun getInvoicePosition(invoice: Invoice): Int=listOfInvoice.value?.indexOfFirst {
        it.id==invoice.id } ?:-1

    fun getInvoicePosition()=getInvoicePosition(invoice.value?: Invoice())

    fun setCurrentInvoice(position:Int){
        if (listOfInvoice.value==null || position<0 ||
            (listOfInvoice.value?.size!! <=position))
            return setCurrentInvoice(listOfInvoice.value!![position])
    }

    fun setCurrentInvoice(_invoice: Invoice){
        invoice.postValue(_invoice)
    }

    fun getClientInvoices(clientID: Int) =
        (listOfInvoice.value?.filter { it.clientID == clientID }?.sortedBy { it.date1 }?: listOf())
    fun getClientInvoicesId(clientID: Int) =
        (listOfInvoice.value?.filter { it.clientID == clientID }?.sortedBy { it.id_invoice }?: listOf())
    fun getclientInvoicesSum(clientID: Int) =
        (listOfInvoice.value?.filter { it.clientID == clientID }?.sortedBy { it.sum_total }?: listOf())
    fun getclientInvoicesDateExec(clientID: Int) =
        (listOfInvoice.value?.filter { it.clientID == clientID }?.sortedBy { it.date_exec }?: listOf())


    private val listDB by lazy {OfflineDBRepository(ListDatabase.getDatabase(MyApplication.context).listDAO())}

    private val myCoroutineScope = CoroutineScope(Dispatchers.Main)

    fun getSearch(s: String, courseID: Int) =
        (listOfInvoice.value?.filter { ((s in it.date1.toString() ) or (s in it.id_invoice.toString()) or (s in it.sum_total.toString()) or (s in it.date_exec.toString())) and (it.clientID == courseID) }?.sortedBy { it.date1 }?: listOf())

    fun onDestroy(){
        myCoroutineScope.cancel()
    }

    val listOfManager: LiveData<List<Manager>> = listDB.getManager().asLiveData()
    val listOfClient: LiveData<List<Client>> = listDB.getAllClients().asLiveData()
    val listOfInvoice: LiveData<List<Invoice>> = listDB.getAllInvoices().asLiveData()


    private var listAPI = ListConnection.getClient().create(ListAPI::class.java)

    fun fetchManagers(){
        listAPI.getManagers().enqueue(object: Callback<Managers> {
            override fun onFailure(call: Call<Managers>, t :Throwable){
                Log.d(TAG,"Ошибка получения списка факультетов", t)
            }
            override fun onResponse(
                call : Call<Managers>,
                response: Response<Managers>
            ){
                if (response.code()==200){
                    val managers = response.body()
                    val items = managers?.items?:emptyList()
                    Log.d(TAG,"Получен список менеджеров $items")
                    for (f in items){
                        println(f.id::class.java.typeName)
                        println(f.name::class.java.typeName)
                    }
                    myCoroutineScope.launch{
                        listDB.deleteAllManagers()
                        for (f in items){
                            listDB.insertManager(f)
                        }
                    }
                    fetchClients()
                }
            }
        })
    }

    fun addManager(manager: Manager){
        listAPI.insertManager(manager)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response: Response<PostResult>){
                    if (response.code()==200) fetchManagers()
                }
                override fun onFailure(call:Call<PostResult>,t: Throwable){
                    Log.d(TAG,"Ошибка добавления менеджера",t)
                }
            })
    }

    fun updateManager(manager: Manager){
        listAPI.updateManager(manager)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response: Response<PostResult>){
                    if (response.code()==200) fetchManagers()
                }
                override fun onFailure(call:Call<PostResult>,t: Throwable){
                    Log.d(TAG,"Ошибка обновления менеджера",t)
                }
            })
    }

    fun deleteManager(manager: Manager){
        listAPI.deleteManager(PostId(manager.id))
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response: Response<PostResult>){
                    if (response.code()==200) fetchManagers()
                }
                override fun onFailure(call:Call<PostResult>,t: Throwable){
                    Log.d(TAG,"Ошибка удаления менеджера",t)
                }
            })
    }

    fun fetchClients(){
        listAPI.getClients().enqueue(object: Callback<Clients> {
            override fun onFailure(call: Call<Clients>, t: Throwable) {
                Log.d(TAG, "Ошибка получения списка клиентов", t)
            }

            override fun onResponse(
                call: Call<Clients>,
                response: Response<Clients>
            ) {
                if (response.code() == 200) {
                    val clients = response.body()
                    val items = clients?.items ?: emptyList()
                    Log.d(TAG, "Получен список клиентов $items")
                    myCoroutineScope.launch {
                        listDB.deleteAllClients()
                        for (g in items) {
                            listDB.insertClient(g)
                        }
                    }
                    fetchInvoice()
                }
            }
        })
    }

    fun addClient(client: Client){
        listAPI.insertClient(client)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchClients()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка обновления клиентов", t)
                }
            })
    }

    fun updateClient(client: Client){
        listAPI.updateClients(client)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchClients()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка записи клиентов", t)
                }
            })
    }

    fun deleteClient(client: Client){
        listAPI.deleteClient(PostId(client.id))
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchClients()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка удаления клиентов", t)
                }
            })
    }

    fun fetchInvoice(){
        listAPI.getInvoices().enqueue(object : Callback<Invoices>{
            override fun onFailure(call:Call<Invoices>, t : Throwable){
                Log.d(TAG,"Ошибка получения списка накладных",t)
            }
            override fun onResponse(
                call:Call<Invoices>,
                response: Response<Invoices>
            ){
                if(response.code()==200){
                    val invoices = response.body()
                    val items = invoices?.items?: emptyList()
                    Log.d(TAG,"Получен список накладных $items")
                    myCoroutineScope.launch {
                        listDB.deleteAllInvoices()
                        for (s in items){
                            listDB.insertInvoice(s)
                        }
                    }
                }
            }
        })
    }

    fun addInvoice(invoice: Invoice){
        listAPI.insertInvoice(invoice)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchInvoice()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка записи накладных", t.fillInStackTrace())
                }
            })
    }

    fun updateInvoice(invoice: Invoice){
        listAPI.updateInvoice(invoice)
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchInvoice()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка обновления накладных", t.fillInStackTrace())
                }
            })
    }

    fun deleteInvoice(invoice: Invoice){
        listAPI.deleteInvoice(PostId(invoice.id))
            .enqueue(object : Callback<PostResult>{
                override fun onResponse(call:Call<PostResult>,response:Response<PostResult>){
                    if (response.code()==200) fetchInvoice()
                }
                override fun onFailure(call:Call<PostResult>,t:Throwable){
                    Log.d(TAG,"Ошибка удаления накладных", t.fillInStackTrace())
                }
            })
    }

    fun login(userName: String, pwd : String, dickandballs : TextView){
        listAPI.login(PostUser(userName,pwd)).enqueue(object: Callback<Spasibo> {
            override fun onFailure(call: Call<Spasibo>, t: Throwable) {
                Log.d(TAG, "Ошибка получения логина", t)
            }

            override fun onResponse(
                call: Call<Spasibo>,
                response: Response<Spasibo>
            ) {
                if (response.code() == 200) {
                    val resp = response.body()
                    dickandballs.text = resp?.items.toString()
                    dickandballs.callOnClick()
                }
            }
        })
    }

}





















