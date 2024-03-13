package com.example.lesson3.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson3.data.Client
import com.example.lesson3.myConsts.TAG
import com.example.lesson3.repository.AppRepository

class ClientViewModel : ViewModel() {

    var clientList: MutableLiveData<List<Client>> = MutableLiveData()
    private var _client : Client? = null
    val client
        get()=_client

    init {
        AppRepository.getInstance().listOfClient.observeForever{
            clientList.postValue(AppRepository.getInstance().managerClients)
        }

        AppRepository.getInstance().client.observeForever{
            _client=it
            Log.d(TAG, "GroupViewModel текущая группа $it")
        }

        AppRepository.getInstance().manager.observeForever{
            clientList.postValue(AppRepository.getInstance().managerClients)
        }
    }

    fun deleteGroup(){
        if(client!=null)
            AppRepository.getInstance().deleteClient(client!!)
    }

    fun appendGroup(groupName: String){
        val client=Client()
        client.name=groupName
        client.mangerID=manager!!.id
        AppRepository.getInstance().addClient(client)
    }

    fun updateGroup(groupName: String){
        if (_client!=null){
            _client!!.name=groupName
            AppRepository.getInstance().updateClient(_client!!)
        }
    }

    fun setCurrentGroup(position: Int){
        if ((clientList.value?.size ?: 0)>position)
            clientList.value?.let{ AppRepository.getInstance().setCurrentClient(it.get(position))}
    }

    fun setCurrentGroup(client: Client){
        AppRepository.getInstance().setCurrentClient(client)
    }

    val getGroupListPosition
        get()= clientList.value?.indexOfFirst { it.id==client?.id } ?: -1

    val manager
        get()=AppRepository.getInstance().manager.value
}