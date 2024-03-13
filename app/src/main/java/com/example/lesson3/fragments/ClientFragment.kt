package com.example.lesson3.fragments

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lesson3.MainActivity
import com.example.lesson3.R
import com.example.lesson3.data.Client
import com.example.lesson3.databinding.FragmentGroupBinding
import com.example.lesson3.interfaces.MainActivityCallbacks
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ClientFragment : Fragment(), MainActivity.Edit {

    companion object {
        private var INSTANCE : ClientFragment?= null
        fun getInstance(): ClientFragment {
            if (INSTANCE == null) INSTANCE= ClientFragment()
            return INSTANCE ?: throw Exception("GroupFragment ne sozdan")
        }
        fun newInstance() : ClientFragment{
            INSTANCE= ClientFragment()
            return INSTANCE!!
        }
    }

    private lateinit var viewModel: ClientViewModel
    private var tabPosition: Int=0
    private lateinit var _binding: FragmentGroupBinding
    private val binding get()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ClientViewModel::class.java)
        val ma= (requireActivity() as MainActivityCallbacks)
        ma.newTitle("Клиент \"${viewModel.manager?.name}\"")

        viewModel.clientList.observe(viewLifecycleOwner
        ) {
            createUI(it)
        }
    }

    private inner class GroupPageAdapter(fa: FragmentActivity, private val cours: List<Client>?): FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return (cours?.size ?: 0)
        }

        override fun createFragment(position: Int): Fragment{
            return InvoiceFragment.newInstance((cours!![position]))
        }
    }

    private fun createUI(clientList: List<Client>){
        binding.tlGroup.clearOnTabSelectedListeners()
        binding.tlGroup.removeAllTabs()

        for (i in 0 until (clientList.size)){
            binding.tlGroup.addTab(binding.tlGroup.newTab().apply {
                text = clientList.get(i).name
            })
        }

        val adapter = GroupPageAdapter(requireActivity(), viewModel.clientList.value)
        binding.vpGroup.adapter = adapter
        TabLayoutMediator(binding.tlGroup, binding.vpGroup, true, true){
                tab,pos ->
            tab.text=clientList.get(pos).name
        }.attach()
        tabPosition=0
        if(viewModel.client!=null)
            tabPosition = if(viewModel.getGroupListPosition>=0)
                viewModel.getGroupListPosition
        else
            0
        viewModel.setCurrentGroup(tabPosition)
        binding.tlGroup.selectTab(binding.tlGroup.getTabAt(tabPosition), true)
        binding.tlGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition=tab?.position!!
                viewModel.setCurrentGroup(clientList[tabPosition])
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun append(){
        editGroup()
    }

    override fun update(){
        editGroup(viewModel.client?.name ?: "")
    }

    override fun delete(){
        deleteDialog()
    }

    private fun deleteDialog(){
        if (viewModel.client==null) return
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Вы действительно хотите удалить клиента ${viewModel.client?.name ?: ""}?")
            .setPositiveButton("ДА"){_,_ ->
                viewModel.deleteGroup()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    private fun editGroup(groupName: String=""){
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog__string, null)
        val messageText = mDialogView.findViewById<TextView>(R.id.tvInfo)
        val inputString = mDialogView.findViewById<EditText>(R.id.etString)
        inputString.setText(groupName)
        messageText.text="Укажите наименование клиента"

        AlertDialog.Builder(requireContext())
            .setTitle("ИЗМЕНЕНИЕ ДАННЫХ")
            .setView(mDialogView)
            .setPositiveButton("подтверждаю"){_,_ ->
                if (inputString.text.isNotBlank()){
                    if (groupName.isBlank())
                        viewModel.appendGroup(inputString.text.toString())
                    else
                        viewModel.updateGroup(inputString.text.toString())
                }
            }
            .setNegativeButton("отмена",null)
            .setCancelable(true)
            .create()
            .show()
    }



//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}