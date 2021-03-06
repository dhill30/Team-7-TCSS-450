package edu.uw.tcss450.groupchat.ui.contacts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for individual expanded contact pages.
 *
 * @version December 2, 2020
 */
public class ContactsHolderAdapter extends FragmentStateAdapter {

    private List<Fragment> mPages;

    /**
     * Constructor to initialize data structure.
     *
     * @param fragment
     */
    public ContactsHolderAdapter(Fragment fragment) {
        super(fragment);
        mPages = new ArrayList<>();
        mPages.add(new ContactsMainFragment());
        mPages.add(new ContactsIncomingFragment());
        mPages.add(new ContactsOutgoingFragment());
        mPages.add(new ContactsSearchFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mPages.get(position);
    }

    @Override
    public int getItemCount() {
        return mPages.size();
    }

}
