package com.redmango.profilecard.ui.profilescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.drsmarineservices.nikhil.data.remote.ApiCallBackResult
import com.example.drsmarineservices.nikhil.utility.LogUtil
import com.example.drsmarineservices.nikhil.utility.Utils
import com.redmango.ProfileCardApplication
import com.redmango.profilecard.data.local.db.entitity.*
import com.redmango.profilecard.data.local.prefs.ProfilePreferences
import com.redmango.profilecard.data.remote.Networking
import com.redmango.profilecard.data.repository.ProfileRepository
import com.redmango.profilecard.data.response.ProfileDetailsResponse
import com.redmango.profilecard.data.response.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val profileCardApplication = application as ProfileCardApplication

    //private val networkService = profileCardApplication.networkService
    private val networkService = Networking.create()
    private val profileDetailsDAO = profileCardApplication.db.getProfileDAO()
    private val profileRepository = ProfileRepository(profileDetailsDAO)
    private val compositeDisposable = CompositeDisposable()
    private val profilePreferences = ProfilePreferences(profileCardApplication.preferences)

    val profileListLiveData=MutableLiveData<List<ProfileDetails>>()
    val updateAdapter=MutableLiveData<Boolean>(false)


    var rowPositionUpdateData=-1;
    var updatdMessage="";

    fun loadDetails() {

        networkService.profileDetails("10")
                .enqueue(object : retrofit2.Callback<ProfileDetailsResponse> {
                    override fun onResponse(
                            call: Call<ProfileDetailsResponse>,
                            response: Response<ProfileDetailsResponse>
                    ) {
                        if (response.raw().cacheResponse != null) {
                            LogUtil.error("Response Came From Cache")
                        } else if (response.raw().networkResponse != null) {
                            LogUtil.error("Response Came From Network")
                            if (response.isSuccessful) {
                                response.body()?.results?.let {
                                    //loadDb(it)
                                    compositeDisposable.add(
                                            profileRepository.insertProfileDetailsToDb(it).subscribe(
                                                    {
                                                        LogUtil.error("Db List " + it.size)
                                                    },
                                                    {
                                                        LogUtil.error(it.toString())
                                                    })

                                    )
                                }

                            }
                        }
                        LogUtil.error("On Response " + response.body()?.let {
                            it.results.size
                        })
                    }

                    override fun onFailure(call: Call<ProfileDetailsResponse>, t: Throwable) {
                        LogUtil.error("On Failure " + Utils.handleVolleyException(t))
                    }

                })

    }


    fun loadDetailsUsingRxJava(apiCallBackResult: ApiCallBackResult<List<ProfileDetails>, String>) {
        compositeDisposable.add(
                profileRepository.loadProfileDetailsFromServer()
                        .flatMap {
                            profileRepository.insertProfileDetailsToDb(it)
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            LogUtil.error("Result Rx Java ${it.size}")
                            profilePreferences.setIsDataSynced(true)
                            apiCallBackResult.onSuccess(it)
                        }, {
                            apiCallBackResult.onErrorResponse(Utils.handleVolleyException(it))
                            LogUtil.error("Error  Rx Java ${Utils.handleVolleyException(it)}")
                        })
        )
    }

    fun isDataAlreadySynced(): Boolean = profilePreferences.checkIsDataSynced()

    fun loadProfileDetailsFromDb(apiCallBackResult: ApiCallBackResult<List<ProfileDetails>, String>) {
        compositeDisposable.add(
                profileDetailsDAO.getProfileInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            LogUtil.error("Result From Db Rx Java ${it.size}")
                            profilePreferences.setIsDataSynced(true)
                            if (it.isEmpty()) {
                               apiCallBackResult.onErrorResponse("No Data Obtained")
                            } else {
                                apiCallBackResult.onSuccess(it)
                            }
                        }, {
                            apiCallBackResult.onErrorResponse(Utils.handleVolleyException(it))
                            LogUtil.error("Error From Db  Rx Java ${Utils.handleVolleyException(it)}")
                        })
        )
    }

    fun loadDb(results: List<Result>) {

        for (data in results) {
            val insertionId = profileDetailsDAO.insertProfileDetail(
                    ProfileDetails(
                            0,
                            data.gender,
                            Name(data.name.first, data.name.last),
                            Location(
                                    data.location.city,
                                    data.location.country,
                                    data.location.postcode.toString(),
                                    data.location.state,
                                    Street(data.location.street.name, data.location.street.number),
                                    Timezone(data.location.timezone.description, data.location.timezone.offset),
                                    Coordinates(
                                            data.location.coordinates.latitude,
                                            data.location.coordinates.longitude
                                    ),
                            ),
                            data.email,
                            Login(
                                    data.login.md5,
                                    data.login.password,
                                    data.login.salt,
                                    data.login.sha1,
                                    data.login.sha256,
                                    data.login.username,
                                    data.login.uuid
                            ),
                            Dob(data.dob.age, data.dob.date),
                            Registered(data.registered.age, data.registered.date),
                            data.phone,
                            data.cell,
                            // Id(data.id.name, data.id.value?:"null"),
                            Id(data.id.name, data.id.value ?: "null"),
                            Picture(data.picture.large, data.picture.medium, data.picture.thumbnail),
                            data.nat,
                            false,
                            ""
                    )
            )
            LogUtil.error("Insertion Id $insertionId")
        }

    }

    fun updateProfileDetails(argMessage: String, argProfileDetails: ProfileDetails, position: Int){
        compositeDisposable.add(
               profileDetailsDAO.update(true,argMessage,argProfileDetails.name.title)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .subscribe({
                          LogUtil.error("Rows Updated : $it")
                          if(it>0)
                          {
                             // profileListLiveData.value?.get(position)?.acceptDeclineMessage=argMessage
                              rowPositionUpdateData=position
                              updatdMessage=argMessage
                              updateAdapter.postValue(true)
                          }
                       },{
                           LogUtil.error("On Error Update : ${it.localizedMessage}")
                       })
        )
    }




    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}