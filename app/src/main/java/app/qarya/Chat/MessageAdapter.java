package app.qarya.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import app.qarya.R;
import app.qarya.presentation.base.MyActivity;
import app.qarya.utils.ImageHelper;

import java.util.List;

import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;

import app.qarya.model.models.Message;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    OnInteractListener<Message> listener;
    private List<Message> mMessages;
    //private int[] mUsernameColors;

    public MessageAdapter(List<Message> messages, OnInteractListener<Message> listener) {
        mMessages = messages;
        this.listener = listener;
        //mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_message;
        MyActivity.log("viewType is =>", viewType+"");
        switch (viewType) {
            case Message.TYPE_MESSAGE:
                layout = R.layout.item_message;
                break;
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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.bind(message);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

              return mMessages.get(position).getCode();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;
        private ImageView photoView;
        Message message;

        public ViewHolder(View itemView) {
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

            if (item.getUpicture() != null)
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

        /*private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }*/
    }
}
