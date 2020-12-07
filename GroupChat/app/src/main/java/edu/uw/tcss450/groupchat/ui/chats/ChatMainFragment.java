package edu.uw.tcss450.groupchat.ui.chats;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import edu.uw.tcss450.groupchat.R;
import edu.uw.tcss450.groupchat.databinding.FragmentChatMainBinding;
import edu.uw.tcss450.groupchat.model.UserInfoViewModel;
import edu.uw.tcss450.groupchat.model.chats.ChatNotificationsViewModel;
import edu.uw.tcss450.groupchat.model.chats.ChatRoomViewModel;

/**
 * Fragment for Home Page of the application.
 *
 * @version November 5
 */
public class ChatMainFragment extends Fragment implements View.OnClickListener {

    private ChatRoomViewModel mModel;

    private UserInfoViewModel mUserModel;

    private FragmentChatMainBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mModel = provider.get(ChatRoomViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);

        mModel.connect(mUserModel.getJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentChatMainBinding.bind(getView());

        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.listRoot;
        rv.setAdapter(new ChatRoomRecyclerViewAdapter(new ArrayList<>(), getActivity()));

        binding.swipeContainer.setOnRefreshListener(() ->
                mModel.connect(mUserModel.getJwt()));

        mModel.addRoomsObserver(getViewLifecycleOwner(), rooms -> {
            rv.setAdapter(new ChatRoomRecyclerViewAdapter(rooms, getActivity()));
            binding.swipeContainer.setRefreshing(false);
        });

        binding.buttonStartChatRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == binding.buttonStartChatRoom){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Create New Chat Room");

            final EditText chatName = new EditText(getContext());
            dialog.setView(chatName);

            dialog.setPositiveButton("Create", (dlg, i) -> {
                mModel.connectCreate(mUserModel.getJwt(), chatName.getText().toString());

                mModel.addResponseObserver(getViewLifecycleOwner(), response -> {
                    mModel.connect(mUserModel.getJwt());
                    dlg.dismiss();
                });
            });

            dialog.setNegativeButton("Cancel", (dlg, i) -> dlg.cancel());

            dialog.show();
        }
    }
}