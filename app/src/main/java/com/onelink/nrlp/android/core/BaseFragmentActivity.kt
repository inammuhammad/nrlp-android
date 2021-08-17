@file:Suppress("KDocUnresolvedReference")

package com.onelink.nrlp.android.core

import android.view.KeyEvent
import android.view.MotionEvent
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.onelink.nrlp.android.R
import java.util.*

/**
 * Created by Umar Javed.
 */

/**
 * This class holds the abstract level implementation of Activities holding Fragments.
 * Every Activity incorporating Fragment(s) will be extending this class.
 *
 * @param DB the data binding of respective class(s).
 * @param VM the view model of respective class(s).
 * @property viewModelClass the View Model of respective class(s).
 */

abstract class BaseFragmentActivity<DB : ViewDataBinding, VM : BaseViewModel>(viewModelClass: Class<VM>) :
    BaseActivity<DB, VM>(viewModelClass), BaseFragment.FragmentNavigationHelper {

    private var currFragment: Fragment? = null

    private val fragments = Stack<Fragment>()


    override fun addFragment(fragment: Fragment, clearBackStack: Boolean, addToBackStack: Boolean) {
        addFragment(fragment, R.id.fragmentContainer, clearBackStack, addToBackStack)
    }

    override fun replaceFragment(
        fragment: Fragment,
        clearBackStack: Boolean,
        addToBackStack: Boolean
    ) {
        addFragment(fragment, R.id.fragmentContainer, clearBackStack, addToBackStack)
    }

    /**
     * Adds the fragment to the referenced layout
     *
     * @param fragment Fragment to be added
     * @param resourceId Resource Id where fragment to be added
     * @param clearBackStack true/false telling if should clear fragment back stack or not
     * @param addToBackStack true/false telling if should add fragment to back stack or not
     */
    override fun addFragment(
        fragment: Fragment, layoutId: Int, clearBackStack: Boolean, addToBackStack: Boolean
    ) {
        if (clearBackStack) {
            clearFragmentBackStack()
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(layoutId, fragment)
        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commit()

        setCurrentFragment(fragment)
        if (addToBackStack) fragments.push(fragment)

        onFragmentBackStackChanged()
    }

    /**
     * Replaces the fragment in the referenced layout
     *
     * @param fragment Fragment to be replaced
     * @param resourceId Resource Id where fragment to be replaced
     * @param clearBackStack true/false telling if should clear fragment back stack or not
     * @param addToBackStack true/false telling if should add fragment to back stack or not
     */
    override fun replaceFragment(
        fragment: Fragment, layoutId: Int, clearBackStack: Boolean, addToBackStack: Boolean
    ) {
        if (clearBackStack) {
            clearFragmentBackStack()
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(layoutId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()

        setCurrentFragment(fragment)
        fragments.push(fragment)

        onFragmentBackStackChanged()
    }

    /**
     * Override this method if need to perform any action on fragment transactions
     */
    open fun onFragmentBackStackChanged() {

    }

    /**
     * Sets the current current fragment in local stack
     */
    private fun setCurrentFragment(currentFragment: Fragment) {
        this.currFragment = currentFragment
    }

    /**
     * Clears the fragment back stack
     */
    private fun clearFragmentBackStack() {
        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount - 1) {
            fm.popBackStack()
        }

        if (!fragments.isEmpty()) {
            currFragment = null
            fragments.clear()
        }
    }

    /**
     * Overridden method
     * Gets invoked when device's back button is pressed
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun getCurrentFragment(): Fragment? {
        return currFragment
    }

    /**
     *Hides keyboard when clicked anywhere outside EditText
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    /**
     *Pops fragment from back stack. If only one fragment then finishes activity
     */
    override fun onBack() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
            return
        }
        supportFragmentManager.popBackStack()
        fragments.pop()
        currFragment = when {
            fragments.isEmpty() -> null
            fragments.peek() is BaseFragment<*, *> -> fragments.peek() as BaseFragment<*, *>
            else -> null
        }

        onFragmentBackStackChanged()
    }
}
