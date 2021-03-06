package edu.uw.tcss450.groupchat.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;

import edu.uw.tcss450.groupchat.databinding.FragmentContactsHolderBinding;
import edu.uw.tcss450.groupchat.model.UserInfoViewModel;
import edu.uw.tcss450.groupchat.model.contacts.ContactNotificationsViewModel;
import edu.uw.tcss450.groupchat.model.contacts.ContactsIncomingViewModel;
import edu.uw.tcss450.groupchat.model.contacts.ContactsMainViewModel;
import edu.uw.tcss450.groupchat.model.contacts.ContactsOutgoingViewModel;
import edu.uw.tcss450.groupchat.model.contacts.ContactsSearchViewModel;

/**
 * Fragment for Contact list tab of the Contact page.
 *
 * @version November 19, 2020
 */
public class ContactsHolderFragment extends Fragment {

    private ContactsMainViewModel mContactsModel;

    private ContactsIncomingViewModel mIncomingModel;

    private ContactsOutgoingViewModel mOutgoingModel;

    private ContactsSearchViewModel mSearchModel;

    private ContactNotificationsViewModel mNotifsModel;

    private UserInfoViewModel mUserModel;

    private FragmentContactsHolderBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mContactsModel = provider.get(ContactsMainViewModel.class);
        mIncomingModel = provider.get(ContactsIncomingViewModel.class);
        mOutgoingModel = provider.get(ContactsOutgoingViewModel.class);
        mSearchModel = provider.get(ContactsSearchViewModel.class);
        mNotifsModel = provider.get(ContactNotificationsViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactsHolderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.viewPager.setAdapter(new ContactsHolderAdapter(this));

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 1:
                    tab.setText("Incoming");
                    break;
                case 2:
                    tab.setText("Outgoing");
                    break;
                case 3:
                    tab.setText("Search");
                    break;
                default:
                    tab.setText("Contacts");
                    break;
            }
            binding.viewPager.setCurrentItem(tab.getPosition(), true);
        }).attach();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mNotifsModel.setSelectedTab("contacts");
                        mNotifsModel.reset("contacts");
                        break;
                    case 1:
                        mNotifsModel.setSelectedTab("incoming");
                        mNotifsModel.reset("incoming");
                        break;
                    case 2:
                        mNotifsModel.setSelectedTab("outgoing");
                        mNotifsModel.reset("outgoing");
                        break;
                    case 3:
                        mNotifsModel.setSelectedTab("search");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mNotifsModel.addContactCountObserver("contacts", getViewLifecycleOwner(), count -> {
            BadgeDrawable badge = binding.tabLayout.getTabAt(0).getOrCreateBadge();
            badge.setMaxCharacterCount(2);

            if (count > 0) {
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                badge.clearNumber();
                binding.tabLayout.getTabAt(0).removeBadge();
            }
        });

        mNotifsModel.addContactCountObserver("incoming", getViewLifecycleOwner(), count -> {
            BadgeDrawable badge = binding.tabLayout.getTabAt(1).getOrCreateBadge();
            badge.setMaxCharacterCount(2);

            if (count > 0) {
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                badge.clearNumber();
                binding.tabLayout.getTabAt(1).removeBadge();
            }
        });

        mNotifsModel.addContactCountObserver("outgoing", getViewLifecycleOwner(), count -> {
            BadgeDrawable badge = binding.tabLayout.getTabAt(2).getOrCreateBadge();
            badge.setMaxCharacterCount(2);

            if (count > 0) {
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                badge.clearNumber();
                binding.tabLayout.getTabAt(2).removeBadge();
            }
        });

        mContactsModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            if (response.length() > 0) {
                if (response.has("code")) {
                    try {
                        Log.e("Web Service Error",
                                response.getJSONObject("data").getString("message"));
                    } catch (JSONException e) {
                        Log.e("JSON Parse Error", e.getMessage());
                    }
                } else {
                    mContactsModel.connect(mUserModel.getJwt());
                    mSearchModel.connect(mUserModel.getJwt());
                }
            } else {
                Log.d("JSON Response", "No Response");
            }
        });

        mIncomingModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            if (response.length() > 0) {
                if (response.has("code")) {
                    try {
                        Log.e("Web Service Error",
                                response.getJSONObject("data").getString("message"));
                    } catch (JSONException e) {
                        Log.e("JSON Parse Error", e.getMessage());
                    }
                } else {
                    mContactsModel.connect(mUserModel.getJwt());
                    mIncomingModel.connect(mUserModel.getJwt());
                    mSearchModel.connect(mUserModel.getJwt());
                }
            } else {
                Log.d("JSON Response", "No Response");
            }
        });

        mOutgoingModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            if (response.length() > 0) {
                if (response.has("code")) {
                    try {
                        Log.e("Web Service Error",
                                response.getJSONObject("data").getString("message"));
                    } catch (JSONException e) {
                        Log.e("JSON Parse Error", e.getMessage());
                    }
                } else {
                    mOutgoingModel.connect(mUserModel.getJwt());
                    mSearchModel.connect(mUserModel.getJwt());
                }
            } else {
                Log.d("JSON Response", "No Response");
            }
        });

        mSearchModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            if (response.length() > 0) {
                if (response.has("code")) {
                    try {
                        Log.e("Web Service Error",
                                response.getJSONObject("data").getString("message"));
                    } catch (JSONException e) {
                        Log.e("JSON Parse Error", e.getMessage());
                    }
                } else {
                    mOutgoingModel.connect(mUserModel.getJwt());
                    mSearchModel.connect(mUserModel.getJwt());
                }
            } else {
                Log.d("JSON Response", "No Response");
            }
        });
    }
}