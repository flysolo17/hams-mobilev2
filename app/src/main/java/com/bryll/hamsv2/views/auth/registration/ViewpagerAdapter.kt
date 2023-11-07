package com.bryll.hamsv2.views.auth.registration

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bryll.hamsv2.views.auth.registration.address.StudentAddressFragment
import com.bryll.hamsv2.views.auth.registration.contacts.StudentContacts

class ViewpagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StudentInfoFragment()
            1 -> StudentAddressFragment()
            else -> StudentContacts()
        }
    }
}
