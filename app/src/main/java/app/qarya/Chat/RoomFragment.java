package app.qarya.Chat;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import app.qarya.R;
import app.qarya.model.models.Conversation;
import app.qarya.model.shared.YDUserManager;
import app.qarya.model.models.Room;
import app.qarya.presentation.activities.ChatActivity;
import app.qarya.presentation.base.MyActivity;
import app.qarya.presentation.vms.VMChat;
import app.qarya.utils.AlertUtils;
import app.qarya.utils.MyConst;
import app.qarya.utils.SoundUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import tn.core.presentation.base.BaseFragment;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.NotificationsUtils;


import app.qarya.model.models.Message;

/**
 * A chat fragment containing messages view and input form.
 */
public class RoomFragment extends BaseFragment<VMChat> {



    public ChatActivity activity() {
        return (ChatActivity) getActivity();
    }


    TextView nameTo;
    View sendTo, closeSendTo;
    private RecyclerView mMessagesView;
    private EditText mInputMessageView;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;

    private Handler mTypingHandler = new Handler();
    private static final int TYPING_TIMER_LENGTH = 600;

    boolean wantLeave = false;
    boolean isABlock = false;

    public RoomFragment() {}

    public static RoomFragment newInstance(Room r) {
        Bundle args = new Bundle();
        args.putSerializable(MyConst.ITEM, r);
        RoomFragment fragment = new RoomFragment();
        fragment.setArguments(args);
        return fragment;
    }






















    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // ------------------------- Lifecycle & Fragment Events ------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------


    @Override
    public boolean onBackPressed() {
        if (!isABlock && !wantLeave){
            AlertUtils.Alert a = new AlertUtils.Alert(){
                @Override
                public void onAccept(Object o) {
                    // mViewModel.leave(); // replaced on   onDestroy
                    wantLeave = true;
                    ((ChatActivity) getActivity()).setList(new ArrayList<>());
                    ((ChatActivity) getActivity()).setInfoMessage("");
                    ((ChatActivity) getActivity()).hideInfo();
                    getActivity().onBackPressed();
                }
            };
            a.setMessage(getString(R.string.message_want_leave));
            AlertUtils.INSTANCE.alert(getActivity(), a );
        }
        return wantLeave; //super.onBackPressed();
    }


    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new MessageAdapter(mMessages, new OnInteractListener<Message>() {
            @Override
            public void onInteract(Message item, @NotNull Action action) {
                MyActivity.log(item.toString());

                if(item.getUid() != null && item.getUid() != 0)
                    if (action == Action.BAN){
                        ban(item.getUid());
                    } else if (action == Action.COMMENT){
                        tagInRoom(item.getUid(), item.getUname());
                    }
            }

            @Override
            public void onClick(Message item) {

            }
        });
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setVM();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.leave();
        mViewModel.unlisten();
        activity().refreshList();
    }

    public void setVM(){
        mViewModel = new ViewModelProvider(this).get(VMChat.class);
        mViewModel.listen();
        mViewModel.setRoom(   (Room) getArgs().getSerializable(MyConst.ITEM)  );
        if (mViewModel.room != null) {
            if (mViewModel.room.getDescription() !=null && mViewModel.room.getDescription() !="")
                ((ChatActivity) getActivity()).setInfoMessage(mViewModel.room.getDescription());
            //MyActivity.log("rooooooooooooom", mViewModel.room.getName());
            //MyActivity.log("rooooooooooooom desc", mViewModel.room.getDescription());
            ((ChatActivity) getActivity()).setTitle(mViewModel.room.getName());
            //mViewModel.callErrors.observe(this, this::onError);
            //mViewModel.loadStatus.observe(this, this::onStatusChanged);
            mViewModel.onNewMessage.observe(this, this::onNewMessage);
            mViewModel.onInfo.observe(this, this::onInfo);
            mViewModel.onJoin.observe(this, this::onJoin);
            mViewModel.onLeft.observe(this, this::onLeft);
            mViewModel.onTyping.observe(this, this::onTyping);
            mViewModel.onStopTyping.observe(this, this::onStopTyping);
            mViewModel.onBanned.observe(this, this::onBanned);
            mViewModel.join();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Choose room first.", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
        }
        SoundUtils.Companion.playChatOpening(getContext());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMessagesView = (RecyclerView) view.findViewById(R.id.messages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesView.setAdapter(mAdapter);


        sendTo = view.findViewById(R.id.sendTo);
        closeSendTo = view.findViewById(R.id.close);
        nameTo = view.findViewById(R.id.toName);
        closeSendTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTo.setVisibility(View.GONE);
                mViewModel.sendTo = null;
            }
        });

        mInputMessageView = (EditText) view.findViewById(R.id.message_input);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.typing();
                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageView sendButton = view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });
    }











































    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------- Adapter function -----------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------

    void addInfo(String message, int type) {
        mMessages.add(
                new Message(type, message)
        );
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addLog(String message) {
        addInfo(message, Message.TYPE_LOG);
    }

    private void addParticipantsLog(int usersCount) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, usersCount, usersCount));
    }

    private void addMessage(Message message) {
        mMessages.add(message);
        //mMessages.add(new Message.Builder(Message.TYPE_MESSAGE).uid(id).username(username).message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getCode() == Message.TYPE_ACTION && message.getUname().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

























    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // ----------------------------------- Helpers ----------------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------


    public void ban(int id){
        mViewModel.ban(id);
    }
    public void tagInRoom(int id, String username){
        nameTo.setText(username);
        mViewModel.sendTo = new VMChat.SendTo(id, username);
        sendTo.setVisibility(View.VISIBLE);
    }
    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        MyActivity.log("elmessage " + message);
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }

        mInputMessageView.setText("");
        int toId = 0;
        String toName = "";
        if (mViewModel.sendTo!=null){
            toId = mViewModel.sendTo.uid;
            toName = mViewModel.sendTo.username;
        }
        String mUsername = "You";
        //
        addMessage(new Message(0, message, mUsername, toName, toId));
        mViewModel.sendMessage(message, null,true);

        sendTo.setVisibility(View.GONE);
        mViewModel.sendTo = null;
    }



    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            mViewModel.stopTyping();
        }
    };




























    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // ----------------------------------- Events -----------------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------



    // for admin
    public void onBanned(SocketResponse data){
        int me = YDUserManager.auth().getId();
        MyActivity.log("me ->"+me);
        MyActivity.log("blocked id ->"+data.getUid());
        if (data.getUid().equals(me)){
            AlertUtils.Alert aa = new AlertUtils.Alert() {
                @Override
                public void onAccept(Object o) {
                    isABlock = true;
                    getActivity().onBackPressed();
                }
            };
            aa.setIsInfo(true);
            aa.setMessage(getResources().getString(R.string.message_user_banned, data.getUname()));
            AlertUtils.INSTANCE.alert(getContext(), aa);
        }
        addLog(getResources().getString(R.string.message_user_banned, data.getUname()));
    }

    // for clients
    public void onInfo(SocketResponse data){
        if (data == null) return;
        addLog(data.getMessage());

        if( data.getCode() == Message.TYPE_ALERT || data.getCode() == Message.TYPE_BLOCK) {
            AlertUtils.Alert aa = new AlertUtils.Alert() {
                @Override
                public void onAccept(Object o) {
                    if (data.getCode() == Message.TYPE_BLOCK){
                        getActivity().onBackPressed();
                    }
                }
            };
            aa.setIsInfo(true);
            aa.setMessage(data.getMessage());
            AlertUtils.INSTANCE.alert(getContext(), aa);
        }
    }
    public void onNewMessage(SocketResponse data){
        if (data == null) return;
        MyActivity.log("newwwwwwww msg");
        MyActivity.log(data.toString());

        removeTyping(data.getUname());
        addMessage(data);

        //if(!isInForegroundMode){
        Intent notificationIntent = new Intent(getContext(), ChatActivity.class);
        notificationIntent.putExtra("room", mViewModel.room.getName());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
        NotificationsUtils.Companion.notify(getContext(), 999, mViewModel.room.getName(), data.getMessage(), R.drawable.logo, SoundUtils.Companion.notificationUri(getContext()), intent);
        //}
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel.room != null)
            activity().setInfoMessage(mViewModel.room.getDescription());
        activity().refreshList();
    }

    public void onJoin(SocketResponse data){
        if (data == null) return;
        ((ChatActivity) getActivity()).setList(data.getUsers());
        String txt = getResources().getString(R.string.message_user_joined, data.getUname());
        addInfo(txt, Message.TYPE_JOIN);
        addParticipantsLog(data.getUsers().size());
    }
    public void onLeft(SocketResponse data){
        ((ChatActivity) getActivity()).setList(data.getUsers());
        String txt = getResources().getString(R.string.message_user_left, data.getUname());
        addInfo(txt, Message.TYPE_LEAVE);
        addParticipantsLog(data.getUsers().size());
        removeTyping(data.getUname());
    }
    public void onTyping(SocketResponse data){
        addMessage(data);
        //addTyping(data.getUname());
    }
    public void onStopTyping(SocketResponse data){
        removeTyping(data.getUname());
    }


























    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------- Alerts & Dialogs -----------------------------
    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------


    public void showOptions(){
        List<String> list = new ArrayList<>();
        list.add(getText(R.string.report).toString());
        if(mViewModel.room.getAdmin())
            list.add(getText(R.string.change_description).toString());

        MaterialAlertDialogBuilder builderSingle = new MaterialAlertDialogBuilder(getActivity());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, list.toArray(new String[list.size()]));

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            switch(which){
                case 0 : reportAlert(); break;
                case 1 : changeAlert(); break;
            }
            dialog.dismiss();
        });
        builderSingle.show();
    }
    public void reportAlert(){
        /*AlertUtils.Alert action = new AlertUtils.Alert() {
            public void onAccept(Object o) {
                mViewModel.report(o.toString());
            }
        };
        action.message = getContext().getText(R.string.report).toString();
        AlertUtils.report(getContext(), action);*/
    }
    public void changeAlert(){
        AlertUtils.Alert action = new AlertUtils.Alert() {
            public void onAccept(Object o) {
                mViewModel.updateRoom(o.toString());
            }
        };
        action.setMessage(getContext().getText(R.string.change_description).toString());
        AlertUtils.INSTANCE.report(getContext(), action, mViewModel.room.getDescription());
    }



}

