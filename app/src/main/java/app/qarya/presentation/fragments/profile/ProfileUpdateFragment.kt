package app.qarya.presentation.fragments.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.profile_update_fragment.*

import app.qarya.R
import app.qarya.model.models.Category
import app.qarya.model.models.City
import app.qarya.model.shared.YDUserManager
import app.qarya.model.models.User
import app.qarya.model.models.requests.UpdateEmailRequest
import app.qarya.model.models.requests.UpdatePasswordRequest
import app.qarya.model.models.requests.UpdateProfileRequest
import app.qarya.model.models.responses.AuthResponse
import app.qarya.presentation.activities.MainActivity
import app.qarya.presentation.base.MyFragment
import app.qarya.presentation.vms.VMProfileUpdate
import java.util.ArrayList

class ProfileUpdateFragment : MyFragment<VMProfileUpdate>() {

    var user: User? = null
    var cities: List<City>? = null
    var categories: List<Category>? = null

    companion object {
        fun newInstance() = ProfileUpdateFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_update_fragment, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(VMProfileUpdate::class.java)
        mViewModel.callErrors.observe(
            this,
            Observer<List<String>> { response -> onError(response) })
        mViewModel.loadStatus.observe(
            this,
            Observer<Boolean> { response -> onStatusChanged(response) })
        mViewModel.getLiveData()
            .observe(this, Observer<User> { response -> onDataReceived(response) })
        mViewModel.cities.observe(
            this,
            Observer { data: List<City>? -> fillLocationsSpinner(data) })
        mViewModel.categories.observe(this, Observer { fillCategoriesSpinner(it) })
        val auth: AuthResponse = YDUserManager.auth()
        if (auth != null && auth.id != null)
            mViewModel.user(auth.id)
        mViewModel.getCities()
        mViewModel.categoriesOfUsers()
    }

    fun fillLocationsSpinner(data: List<City>?) {
        cities = data
        val list: MutableList<String> = ArrayList()
        if (data != null) for (location in data) {
            list.add(location.name)
        }
        locationSpinner!!.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, list)
    }

    fun fillCategoriesSpinner(data: List<Category>?) {
        categories = data
        val list: MutableList<String> = ArrayList()
        if (data != null) for (category in data) {
            list.add(category.name)
        }
        categorySpinner!!.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, list)
    }

    fun onDataReceived(data: User) {
        if (user == null) { // first request, user is null
            user = data
            YDUserManager.saveFromUser(data)
            name_wrapper.editText?.setText(data.name)
            email_wrapper.editText?.setText(data.email)
            summary_wrapper.editText?.setText(data.summary)
            address_wrapper.editText?.setText(data.address)
            phone_wrapper.editText?.setText(data.phone)

            // UPDATE
            confirmBtn.setOnClickListener {
                var c = 1
                if (categorySpinner!!.selectedItemPosition >= 0 && categorySpinner!!.selectedItemPosition < categories!!.size)
                    c = categories!![categorySpinner!!.selectedItemPosition].id
                var l = 1
                if (locationSpinner!!.selectedItemPosition >= 0 && locationSpinner!!.selectedItemPosition < cities!!.size)
                    l = cities!![locationSpinner!!.selectedItemPosition].id
                val p = UpdateProfileRequest(
                    c, l,
                    name_wrapper.editText?.text.toString(),
                    phone_wrapper.editText?.text.toString(),
                    summary_wrapper.editText?.text.toString(),
                    address_wrapper.editText?.text.toString()
                )
                mViewModel.update(p)
            }
            emailBtn.setOnClickListener {
                val p = UpdateEmailRequest(
                    email_wrapper.editText?.text.toString(),
                    email_password_wrapper.editText?.text.toString()
                )
                mViewModel.updateEmail(p)
            }
            passwordBtn.setOnClickListener {
                val p = UpdatePasswordRequest(
                    password_current_wrapper.editText?.text.toString(),
                    password_wrapper.editText?.text.toString(),
                    password_confirm_wrapper.editText?.text.toString()
                )
                mViewModel.updatePassword(p)
            }
        } else {
            YDUserManager.saveFromUser(data)
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
            (activity as MainActivity).resetHeader()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
    }


    var current = 0
    fun initTabs() {
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        var tabIcons = intArrayOf(R.drawable.ic_quill, R.drawable.ic_quill, R.drawable.ic_quill)
        var tabTitles =
            intArrayOf(R.string.update_profile, R.string.update_email, R.string.update_password)

        for (i in tabTitles.indices) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]).setIcon(tabIcons[i]))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                current = tabLayout.selectedTabPosition
                onTabSelected()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}

        })
    }

    fun onTabSelected() {
        if (current == 0) {
            form_email.visibility = View.GONE
            form_password.visibility = View.GONE
            form_profile.visibility = View.VISIBLE
        } else if (current == 1) {
            form_profile.visibility = View.GONE
            form_password.visibility = View.GONE
            form_email.visibility = View.VISIBLE
        } else {
            form_profile.visibility = View.GONE
            form_email.visibility = View.GONE
            form_password.visibility = View.VISIBLE
        }
    }


/*
    fun forStore(){

        workTimeStart.keyListener = null
        workTimeEnd.keyListener = null
        workTimeStart.setOnClickListener {
            val timePickerDialog = TimePickerDialog(activity, { timePicker, hourOfDay, minutes -> workTimeStart.setText("$hourOfDay:$minutes") }, 8, 0, true)
            timePickerDialog.show()
        }
        workTimeEnd.setOnClickListener {
            val timePickerDialog = TimePickerDialog(activity, { timePicker, hourOfDay, minutes -> workTimeEnd.setText("$hourOfDay:$minutes") }, 18, 0, true)
            timePickerDialog.show()
        }
    }*/
}
