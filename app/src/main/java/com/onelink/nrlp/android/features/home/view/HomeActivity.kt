package com.onelink.nrlp.android.features.home.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onelink.nrlp.android.BuildConfig
import com.onelink.nrlp.android.OneLinkApplication
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.ActivityHomeBinding
import com.onelink.nrlp.android.features.changePassword.view.ChangePasswordActivity
import com.onelink.nrlp.android.features.contactus.view.ContactUsActivity
import com.onelink.nrlp.android.features.faqs.view.FAQsActivity
import com.onelink.nrlp.android.features.home.fragments.BeneficiaryHomeFragment
import com.onelink.nrlp.android.features.home.fragments.RemitterHomeFragment
import com.onelink.nrlp.android.features.home.sidemenu.*
import com.onelink.nrlp.android.features.home.viewmodel.HomeActivityViewModel
import com.onelink.nrlp.android.features.language.view.LanguageActivity
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.profile.view.ProfileActivity
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.toSpanned
import java.util.*
import javax.inject.Inject

/**
 * Created by Umar Javed.
 */
const val SIDE_MENU_ITEM_DELAY = 200L
const val TAG_CONFIRM_LOGOUT_DIALOG = "confirm_logout_dialog"

class HomeActivity :
    BaseFragmentActivity<ActivityHomeBinding, HomeActivityViewModel>(HomeActivityViewModel::class.java),
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.activity_home

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var sideMenuOptionsAdapter: SideMenuOptionsAdapter

    override fun initViewModel(viewModel: HomeActivityViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { toggleMenu() })
        binding.toolbar.showBorderView(true)
        binding.toolbar.setLeftButton(ContextCompat.getDrawable(this, R.drawable.ic_side_menu))

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {

                binding.toolbar.setTitle(it.getTitle())
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

    private fun onSideMenuItemClicked(sideMenuOptionsItemModel: SideMenuOptionsItemModel) {
        toggleMenu()
        Handler(Looper.getMainLooper()).postDelayed({
            when (sideMenuOptionsItemModel.key) {
                SIDE_MENU_KEY_PROFILE -> launchProfileActivity()
                SIDE_MENU_KEY_CHANGE_PASSWORD -> launchChangePassword()
                SIDE_MENU_KEY_FAQS -> launchFAQs()
                SIDE_MENU_KEY_CONTACT_US -> launchContactUs()
                SIDE_MENU_KEY_CHANGE_LANGAUGE -> startLanguageActivity()
                SIDE_MENU_KEY_LOGOUT -> showLogoutConfirmationDialog()
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

    private fun startLanguageActivity() = LanguageActivity.start(this)


    private fun launchFAQs() {
        startActivity(FAQsActivity.newFaqIntent(this))
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

}
