package com.redmango.profilecard.ui.profilescreen

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drsmarineservices.nikhil.data.remote.ApiCallBackResult
import com.example.drsmarineservices.nikhil.ui.common.CustomDialogFragment
import com.example.drsmarineservices.nikhil.utility.LogUtil
import com.redmango.profilecard.R
import com.redmango.profilecard.adapters.ProfileAdapter
import com.redmango.profilecard.data.local.db.entitity.ProfileDetails
import com.redmango.profilecard.databinding.ActivityMainBinding
import kotlin.math.pow


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var profileAdapter: ProfileAdapter;
    private lateinit var profileDetails: MutableList<ProfileDetails>
    private lateinit var progressDialog: CustomDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        manageLayoutAsperScreenSize()
        setAdapters();
        setObservers();
        checkIsDataAvilable()

    }

    private fun setAdapters() {
        profileDetails = mutableListOf()
        profileAdapter = ProfileAdapter(this, profileDetails, object : ProfileAdapter.OnItemClickListener {
            override fun onAccepted(position: Int) {
                LogUtil.error("On Accepted")
                val message = profileDetails[position].name.title + " has Accepted"
                viewModel.updateProfileDetails(message, profileDetails[position], position)
            }

            override fun onDeclined(position: Int) {
                LogUtil.error("On Declined")
                val message = profileDetails[position].name.title + " has Declined"
                viewModel.updateProfileDetails(message, profileDetails[position], position)
            }

        })

        binding.rvProfileData.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = profileAdapter
        }

    }

    private fun manageLayoutAsperScreenSize() {
        if (getScreenSize() < 6) {
            val params = binding.rvProfileData.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            binding.rvProfileData.layoutParams = params
        }
    }


    private fun setObservers() {
        viewModel.profileListLiveData.observe(this, {
            LogUtil.error("Data is Observed")
            if (it.isNotEmpty()) {
                if (profileDetails.isNotEmpty()) {
                    profileDetails.clear()
                }
                profileDetails.addAll(it)
                profileAdapter.notifyDataSetChanged()
            }
        })

        viewModel.updateAdapter.observe(this, {
            if (it) {
                LogUtil.error("Update Adapter")
                profileDetails[viewModel.rowPositionUpdateData].isAcceptedDeclined = true
                profileDetails[viewModel.rowPositionUpdateData].acceptDeclineMessage = viewModel.updatdMessage
                profileAdapter.notifyItemChanged(viewModel.rowPositionUpdateData)
                viewModel.rowPositionUpdateData = -1
                viewModel.updatdMessage = ""
            }
        })





    }

    private fun checkIsDataAvilable() {
        if (viewModel.isDataAlreadySynced()) {
            LogUtil.error("Data Already Synced")
            getUserDetailsFromDb()
        } else {
            getUserDetails()
        }
    }


    private fun getUserDetailsFromDb() {
        viewModel.loadProfileDetailsFromDb(object : ApiCallBackResult<List<ProfileDetails>, String> {
            override fun onSuccess(result: List<ProfileDetails>) {
                LogUtil.error("On Sucess Db  ${result.get(0).name}")
                viewModel.profileListLiveData.postValue(result)
            }

            override fun onFailure(error: String) {
                toast(error)
                LogUtil.error("On Failure Db $error")
            }

            override fun onErrorResponse(error: String) {
                toast(error)
                LogUtil.error("On Error Response Db  $error")
            }

        })
    }


    private fun getUserDetails() {
        viewModel.loadDetailsUsingRxJava(object : ApiCallBackResult<List<ProfileDetails>, String> {
            override fun onSuccess(result: List<ProfileDetails>) {
                LogUtil.error("On Sucess ${result.get(0).name}")
                viewModel.profileListLiveData.postValue(result)
            }

            override fun onFailure(error: String) {
                LogUtil.error("On Failure $error")
                toast(error)

            }

            override fun onErrorResponse(error: String) {
                LogUtil.error("On Error Response $error")
                toast(error)
            }

        })
    }


    private fun getScreenSize(): Double {

        val point = Point()
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getRealSize(point)
        val displayMetrics = resources.displayMetrics
        val width: Int = point.x
        val height: Int = point.y
        val wi = width.toDouble() / displayMetrics.xdpi.toDouble()
        val hi = height.toDouble() / displayMetrics.ydpi.toDouble()
        val x = wi.pow(2.0)
        val y = hi.pow(2.0)
        val screenSize = (Math.round(Math.sqrt(x + y) * 10.0) / 10.0)
        LogUtil.error("Screen Size $screenSize")
        return screenSize
    }




    fun Context.toast(argMessage: String) {
        Toast.makeText(this, argMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
    }
}