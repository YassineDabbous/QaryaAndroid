package app.qarya.Chat.Private;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import app.qarya.R;
import app.qarya.model.models.Message;
import app.qarya.model.shared.YDUserManager;
import app.qarya.utils.ImageHelper;
import app.qarya.utils.MyMediaPlayer;

import java.util.List;

import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.TextUtils;

/**
 * Created by X on 5/23/2018.
 */

public class ChatAdapter extends Adapter {
    private List<Message> mMessageList;
    OnInteractListener<Message> listener;
    int me = 0;
    public ChatAdapter(List<Message> messageList, OnInteractListener<Message> listener) {
        mMessageList = messageList;
        me = YDUserManager.auth().getId();
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);
        if (message.getCode().equals(Message.TYPE_MESSAGE) && message.getUid().equals(me)) {
            // If the current user is the sender of the message
            return Message.TYPE_MY_MESSAGE;
        }
        return message.getCode();
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        int layout = -1;
        switch (viewType) {
            case Message.TYPE_MESSAGE:
                layout = R.layout.item_message_received;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
                return new ReceivedMessageHolder(view);
            case Message.TYPE_MY_MESSAGE:
                layout = R.layout.item_message_sent;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
                return new SentMessageHolder(view);
            case Message.TYPE_LOG:
            case Message.TYPE_JOIN:
            case Message.TYPE_LEAVE:
                layout = R.layout.item_log;
                break;
            case Message.TYPE_ACTION:
                layout = R.layout.item_action;
                break;
        }

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new LogsViewHolder(v);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof SentMessageHolder)
            ((SentMessageHolder) holder).release();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof SentMessageHolder)
            ((SentMessageHolder) holder).play();
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);

        if (holder instanceof ReceivedMessageHolder)
            ((ReceivedMessageHolder) holder).bind(message);
        else if (holder instanceof SentMessageHolder)
            ((SentMessageHolder) holder).bind(message);
        else if (holder instanceof LogsViewHolder)
            ((LogsViewHolder) holder).bind(message);
/*
        switch (holder.getItemViewType()) {
            case Message.TYPE_MY_MESSAGE:
                ((SentMessageHolder) holder).bind(message);
                break;
            case Message.TYPE_MESSAGE:
                ((ReceivedMessageHolder) holder).bind(message);
            default:
                ((LogsViewHolder) holder).bind(message);
        }*/
    }





    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageTextView, timeText;
        ImageView messageImageView;
        MyMediaPlayer messageVideoView;
        Message message;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeText = itemView.findViewById(R.id.text_message_time);
            messageImageView = itemView.findViewById(R.id.messageImageView);
            messageVideoView = itemView.findViewById(R.id.messagePlayer);
        }

        void play(){
            if(message!=null && message.getFileType() != null && message.getFileType() >= 1)
                messageVideoView.initializePlayer(message.getFileUrl());
        }
        void release(){
            messageVideoView.releasePlayer();
        }

        void bind(Message message) {

            //avoid showin images in wrong places
            setIsRecyclable(false);

            this.message = message;
            timeText.setText(message.getTimeAgo());

            if(message.getFileType() == null){
                TextUtils.htmlToView(messageTextView, TextUtils.replaceUrlsWithAnchorTag(message.getMessage()));
                messageTextView.setVisibility(View.VISIBLE);
            } else {
                if(message.getFileType() == 0){
                    messageImageView.setVisibility(View.VISIBLE);
                    ImageHelper.load(messageImageView, message.getFileUrl());
                } else if(message.getFileType() >= 1){
                    messageVideoView.initializePlayer(message.getFileUrl());
                    messageVideoView.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private class ReceivedMessageHolder extends SentMessageHolder {
        ImageView userImageView;
        TextView nameTextView;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            userImageView = itemView.findViewById(R.id.userImageView);
        }

        @Override
        void bind(Message message) {
            super.bind(message);
            nameTextView.setText(message.getUname());
            ImageHelper.load(userImageView, message.getUpicture(), 80,80);
        }
    }


    public class LogsViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;
        private ImageView photoView;
        Message message;

        public LogsViewHolder(View itemView) {
            super(itemView);

            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            photoView = itemView.findViewById(R.id.user_photo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //listener.onInteract(message, Action.BAN);
                    listener.onInteract(message, Action.COMMENT);
                }
            });
        }
        public void bind(Message item) {
            this.message = item;

            if (item.getUpicture() != null && photoView!=null)
                ImageHelper.loadCircle(photoView, item.getUpicture());

            if (null != mUsernameView){
                mUsernameView.setText(item.getUname());
                //mUsernameView.setTextColor(getUsernameColor(username));
            }

            if (null != mMessageView){
                mMessageView.setText(item.getMessage());

                if(item.getCode() == Message.TYPE_JOIN){
                    mMessageView.setBackgroundColor(mMessageView.getResources().getColor(R.color.green));
                    mMessageView.setTextColor(mMessageView.getResources().getColor(R.color.white));
                } else if(item.getCode() == Message.TYPE_LEAVE){
                    mMessageView.setBackgroundColor(mMessageView.getResources().getColor(R.color.material_red));
                    mMessageView.setTextColor(mMessageView.getResources().getColor(R.color.white));
                }
            }


            if (item.getToID() != null && item.getToID() > 0){
                itemView.findViewById(R.id.toUser).setVisibility(View.VISIBLE);
                ((TextView) itemView.findViewById(R.id.toUsername)).setText(item.getToName());
            }


        }
    }
}