package app.qarya.presentation.adapters;


import tn.core.presentation.listeners.OnClickItemListener;
import app.qarya.model.models.Conversation;
import app.qarya.presentation.adapters.vh.ConversationVH;
import app.qarya.presentation.base.MyAdapter;

import java.util.List;


public class ConversationsAdapter extends MyAdapter<Conversation, ConversationVH> {


    public ConversationsAdapter(List<Conversation> itemList, OnClickItemListener<Conversation> mListener) {
        super(itemList, ConversationVH.class, mListener, false);
    }
}