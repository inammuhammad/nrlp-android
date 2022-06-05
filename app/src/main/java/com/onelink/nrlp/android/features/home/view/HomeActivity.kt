package com.onelink.nrlp.android.features.home.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.onelink.nrlp.android.BuildConfig
import com.onelink.nrlp.android.OneLinkApplication
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.ActivityHomeBinding
import com.onelink.nrlp.android.features.appnotification.InAppNotificationActivity
import com.onelink.nrlp.android.features.changePassword.view.ChangePasswordActivity
import com.onelink.nrlp.android.features.complaint.view.RegComplaintActivity
import com.onelink.nrlp.android.features.contactus.view.ContactUsActivity
import com.onelink.nrlp.android.features.faqs.view.FAQsActivity
import com.onelink.nrlp.android.features.home.fragments.*
import com.onelink.nrlp.android.features.home.sidemenu.*
import com.onelink.nrlp.android.features.home.viewmodel.HomeActivityViewModel
import com.onelink.nrlp.android.features.language.view.LanguageActivity
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.profile.view.ProfileActivity
import com.onelink.nrlp.android.features.receiver.view.ReceiverActivity
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.toSpanned
import java.lang.Exception
import java.util.*
import javax.inject.Inject


/**
 * Created by Umar Javed.
 */
const val SIDE_MENU_ITEM_DELAY = 200L
const val TAG_CONFIRM_LOGOUT_DIALOG = "confirm_logout_dialog"

class HomeActivity :
    BaseFragmentActivity<ActivityHomeBinding, HomeActivityViewModel>(HomeActivityViewModel::class.java),
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners, SelectCityFragment.OnSelectCityListener {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var listenerCity: SelectCityFragment.OnSelectCityListener

    override fun getLayoutRes() = R.layout.activity_home

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var sideMenuOptionsAdapter: SideMenuOptionsAdapter

    override fun initViewModel(viewModel: HomeActivityViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { toggleMenu() })
        binding.toolbar.showBorderView(true)
        binding.toolbar.setLeftButton(ContextCompat.getDrawable(this, R.drawable.ic_side_menu))
        binding.toolbar.setRightButtonVisible(true)
        binding.toolbar.setNotificationCountVisible(true)
        binding.toolbar.setRightButtonClickListener(View.OnClickListener { launchNotificationActivity() })

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
                if(it is NadraVerificationRequiredFragment)
                    binding.toolbar.setLeftButtonVisible(false)
            }
        }

        UserData.getUser()?.let {
            if (it.accountType == Constants.BENEFICIARY.toLowerCase(Locale.getDefault())) {
                addFragment(
                    BeneficiaryHomeFragment.newInstance(),
                    clearBackStack = true,
                    addToBackStack = true
                )
            } else {
                addFragment(
                    RemitterHomeFragment.newInstance(),
                    clearBackStack = true,
                    addToBackStack = true
                )
            }
        }

        initSideMenu()
        initObservers()
    }

    private fun initObservers() {

        (applicationContext as OneLinkApplication).startCounter()
        viewModel.observeLogoutResponse().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS, Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    UserData.emptyUserData()
                    launchLoginActivity()
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(this)
                }
            }
        })

        viewModel.observeInAppRating().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(this)
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                }
            }
        })
    }

    private fun initSideMenu() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            null,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.sideMenu.tvVersion.text =
            String.format(resources.getString(R.string.version, BuildConfig.VERSION_NAME))

        initSideMenuAdapter()
    }

    private fun initSideMenuAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        binding.sideMenu.rvSideMenu.layoutManager = linearLayoutManager
        sideMenuOptionsAdapter =
            SideMenuOptionsAdapter(
                SideMenuItemUtils.getSideMenuUIModelsList(
                    this,
                    ::onSideMenuItemClicked
                )
            )
        binding.sideMenu.rvSideMenu.adapter = sideMenuOptionsAdapter
    }

    fun enableSideMenuDrawer(enabled:Boolean){
        if(enabled){
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }else{
            binding.drawerLayout.closeDrawers()
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    fun hideHomeScreenTools() {
        binding.toolbar.setRightButtonVisible(false)
        binding.toolbar.setNotificationCountVisible(false)
        binding.toolbar.setLeftButtonVisible(false)
    }

    private fun onSideMenuItemClicked(sideMenuOptionsItemModel: SideMenuOptionsItemModel) {
        toggleMenu()
        Handler(Looper.getMainLooper()).postDelayed({
            when (sideMenuOptionsItemModel.key) {
                SIDE_MENU_KEY_PROFILE -> launchProfileActivity()
                SIDE_MENU_KEY_CHANGE_PASSWORD -> launchChangePassword()
                SIDE_MENU_KEY_FAQS -> launchFAQs()
                SIDE_MENU_KEY_GUIDE -> goToYouTube()
                SIDE_MENU_KEY_CONTACT_US -> launchContactUs()
                SIDE_MENU_KEY_CHANGE_LANGAUGE -> startLanguageActivity()
                SIDE_MENU_KEY_COMPLAINTS -> launchRegComplaintsActivity()
                SIDE_MENU_KEY_LOGOUT -> showLogoutConfirmationDialog()
                SIDE_MENU_KEY_RECEIVER_MANAGEMENT -> launchReceiverManagementActivity()
            }
        }, SIDE_MENU_ITEM_DELAY)
    }

    private fun launchLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    private fun launchProfileActivity() {
        startActivity(ProfileActivity.newProfileActivityIntent(this))
    }

    private fun launchChangePassword() {
        startActivity(ChangePasswordActivity.newChangePasswordIntent(this))
    }

    private fun launchContactUs() {
        startActivity(ContactUsActivity.newIntent(this))
    }

    private fun launchRegComplaintsActivity(){
        startActivity(RegComplaintActivity.newRegisteredComplaintIntent(this))
    }

    private fun startLanguageActivity() = LanguageActivity.start(this)


    private fun launchFAQs() {
        startActivity(FAQsActivity.newFaqIntent(this))
    }

    private fun launchReceiverManagementActivity() = startActivity(ReceiverActivity.createIntent(this))

    private fun goToYouTube(){
        val url = "https://www.youtube.com/playlist?list=PLFB-5JvOR9rAvAGK6YzQmxXvFiUWn48vY"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun showLogoutConfirmationDialog() {
        OneLinkAlertDialogsFragment.Builder().setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_error_dialog).setIsAlertOnly(false)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_confirmation_msg).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setPositiveButtonText(resources.getString(R.string.yes))
            .setNegativeButtonText(resources.getString(R.string.no)).setCancelable(true)
            .show(supportFragmentManager, TAG_CONFIRM_LOGOUT_DIALOG)
        try {
            if (UserData.getUser()?.registrationRating == true)
                launchInAppReview()
        } catch (e: Exception) {
        }
    }

    private fun showComingSoonDialog() {
        OneLinkAlertDialogsFragment.Builder()
            .setIsAlertOnly(true).setDrawable(R.drawable.ic_coming_soon_alert)
            .setTitle(getString(R.string.coming_soon))
            .setMessage((getString(R.string.coming_soon_msg)).toSpanned())
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false).show(supportFragmentManager, TAG)
    }

    private fun toggleMenu() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun launchInAppReview() {
        //val manager = ReviewManagerFactory.create(this)
        val manager = FakeReviewManager(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(this, reviewInfo)
                if (flow != null) {
                    flow.addOnCompleteListener { _ ->
                        Toast.makeText(this, "Review successful", Toast.LENGTH_LONG).show()
                        viewModel.inAppRatingComplete()
                    }
                }
            }
            else {}
        }
    }

    private fun launchNotificationActivity() {
        startActivity(InAppNotificationActivity.newNotificationActivityIntent(this))
    }

    fun setNotificationCount(count: String) {
        binding.toolbar.setNotificationCount(count)
    }

    override fun onResume() {
        super.onResume()
        val fragment = getCurrentFragment() as BaseFragment<*, *>?
        fragment?.refresh()
    }

    companion object {
        private const val TAG = "homeActivity"

        fun start(activity: Activity) {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
            activity.finish()
        }

        fun newHomeIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        viewModel.performLogout()
    }

    fun logoutUser(){
        viewModel.performLogout()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                if (it !is NadraVerificationRequiredFragment && it !is FatherNameVerificationFragment) {
                    onBack()
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onSelectCityListener(citiesModel: CitiesModel) {
        listenerCity.onSelectCityListener(citiesModel)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is NadraVerificationDetailsFragment){
            listenerCity = fragment
        }
    }

}
