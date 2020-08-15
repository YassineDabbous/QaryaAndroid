package app.qarya.presentation.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import app.qarya.BuildConfig;
import app.qarya.Chat.Private.ChatFragment;
import app.qarya.Chat.RoomFragment;
import app.qarya.R;
import app.qarya.domain.usecase.UCMessages;
import app.qarya.model.ModelType;
import app.qarya.model.models.Category;
import app.qarya.model.models.City;
import app.qarya.model.models.Comment;
import app.qarya.model.models.Commun;
import app.qarya.model.models.Conversation;
import app.qarya.model.models.Message;
import app.qarya.model.models.Model;
import app.qarya.model.models.Notification;
import app.qarya.model.models.Post;
import app.qarya.model.models.Relation;
import app.qarya.model.models.Room;
import app.qarya.model.models.User;
import app.qarya.model.models.responses.ConfigsResponse;
import app.qarya.model.shared.YDUserManager;
import app.qarya.presentation.fragments.PostsFragment;
import app.qarya.presentation.fragments.posts.PostFragment;
import app.qarya.presentation.fragments.posts.ProductFragment;
import app.qarya.presentation.fragments.posts.NoteFragment;
import app.qarya.presentation.fragments.posts.StoryFragment;
import app.qarya.presentation.fragments.profile.UserProfileFragment;
import app.qarya.presentation.fragments.dialogs.GenericListFragment;
import app.qarya.presentation.fragments.dialogs.LoginDialogFragment;
import app.qarya.utils.AlertUtils;
import app.qarya.utils.Themes;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import tn.core.domain.Failure;
import tn.core.domain.base.Closure;
import tn.core.presentation.base.BaseActivity2;
import tn.core.presentation.base.BaseDialogFragment;
import tn.core.presentation.base.BaseFragment;
import tn.core.presentation.listeners.Action;
import tn.core.presentation.listeners.OnInteractListener;
import tn.core.util.Const;
import tn.core.util.LocaleManager;
import tn.core.util.PrefManager;


public class MyActivity extends BaseActivity2 {

    public static String ACTION = "ACTION";
    public static String DATA = "DATA";
    View pd;


    void toggleSwipe(boolean b){

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        MyActivity.log("attachBaseContext");
    }

    public static void logFb(String... logs){
        if(BuildConfig.DEBUG)
            for (String log: logs) {
                Log.wtf(Const.TAG, log);
            }
    }
    public static void logPlayer(String... logs){
        if(BuildConfig.DEBUG)
            for (String log: logs) {
                Log.wtf("PLAYER", log);
            }
    }
    public static void log(String... logs){
        if(BuildConfig.DEBUG)
            for (String log: logs) {
                Log.wtf(Const.TAG, log);
            }
    }
    public static void logHome(String... logs){
        if(false && BuildConfig.DEBUG)
            for (String log: logs) {
                Log.wtf(Const.TAG, log);
            }
    }



    public boolean isInForeground() {
        return isInForegroundMode;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isInForegroundMode = true;
    }

    @Override
    protected void onPause() {
        isInForegroundMode = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        isInForegroundMode = true;
        super.onResume();
        setupToolbar();
        init();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(Themes.Companion.getSavedTheme(this));
        prefManager = new PrefManager(getApplicationContext());
        super.onCreate(savedInstanceState);
    }


    public void setBackground(){
        final ImageView bg = findViewById(R.id.backgroundImage);
        if(Themes.Companion.isDarkTheme(this)) {
            ConfigsResponse configs =  YDUserManager.configs();
            String bglink = null;
            if (configs!=null){
                bglink = configs.getBackground();
                log(bglink);
                if (bg!=null && bglink!=null && !bglink.isEmpty())
                    log("setting background to:", bglink);
                Picasso.get().load(bglink).into(bg, new Callback() {
                    @Override
                    public void onSuccess() {
                        bg.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        bg.setVisibility(View.GONE);
                    }
                });
            }
        } else {
            bg.setVisibility(View.GONE);
        }
    }

    public void toggleTheme(){
        setTheme(Themes.Companion.toggleTheme(this));
        recreate();
    }
    /*public void turnLight(boolean o){
        Themes.Companion.saveTheme(o ?  Themes.Companion.getDAY() : Themes.Companion.getDEFAULT(), this);
        recreate();
    }*/

    @Override
    public void showPD(){
        if(pd==null) pd = findViewById(R.id.llProgressBar);
        pd.setVisibility(View.VISIBLE);
    }
    @Override
    public void dismissPD(){
        if(pd==null) pd = findViewById(R.id.llProgressBar);
        pd.setVisibility(View.GONE);
    }




    public void showLogin(){
        DialogFragment loginDialogFragment = LoginDialogFragment.Companion.newInstance(null);
        loginDialogFragment.show(getSupportFragmentManager(), LoginDialogFragment.class.getSimpleName());
    }

    public void setFirstFragment(BaseFragment fragment){
        currentFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        ft.replace(R.id.content_main, fragment, fragment.getClass().getSimpleName()+tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        tag++;
        //setupToolbar();
    }
    public void setFragment(BaseFragment fragment){
        if(currentFragment instanceof DialogFragment) ((DialogFragment) currentFragment).dismiss();
        currentFragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        String eltag = fragment.getTag();
        if(eltag==null)eltag = fragment.getClass().getSimpleName()+tag;
        else MyActivity.log(fragment.getClass().getSimpleName()+" has a tag ₧ƒ◄↕v╞");
        ft.replace(R.id.content_main, fragment, eltag);
        ft.addToBackStack(fragment.getClass().getSimpleName()+tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        tag++;
        //setupToolbar();
    }


    public void setFragment(BaseDialogFragment fragment){
        currentFragment = fragment;
        String eltag = fragment.getTag();
        if(eltag==null)eltag = fragment.getClass().getSimpleName()+tag;
        else MyActivity.log(fragment.getClass().getSimpleName()+" has a tag ₧ƒ◄↕v╞");
        fragment.show(getSupportFragmentManager(), eltag);
        tag++;
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            boolean canBack = currentFragment!=null ? currentFragment.onBackPressed() : false;
            if (canBack){
                if (currentFragment instanceof DialogFragment){
                    ((DialogFragment) currentFragment).dismiss();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
                setupToolbar();
                String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            }
        }
        else{
            super.onBackPressed();
            finish();
        }
    }

    public void setupToolbar() {
        log("");
    }



    public void onItemSelected(Commun item) {
        if (item instanceof Post) onItemSelected((Post) item);
        else if (item instanceof User) onItemSelected((User) item);
        else if (item instanceof Relation) onItemSelected((Relation) item);
        else if (item instanceof Comment) onItemSelected((Comment) item);
        else log("onItemSelected => WHAT IS THIS !!!!!!!!!!!!!!!");
    }

    public void onItemSelected(Relation item) {
        //setFragment(PostsFragment.newInstance(new User(item.uid, item.uname, item.upicture)));
        onItemSelected(new User(item.uid, item.uname, item.upicture));
    }
    public void onItemSelected(User item) {
        setFragment(UserProfileFragment.Companion.newInstance(item.getId()));
    }
    public void onItemSelected(Category item) {
        setFragment(PostsFragment.newInstance(item));
        /*if (item.parentOf == ModelType.POST)
            setFragment(PostsFragment.newInstance(item));
        else
            setFragment(ProductsFragment.newInstance(item));*/
    }
    public void onItemSelected(City item) {
        setFragment(PostsFragment.newInstance(item));
    }

    public void onItemSelected(Post item) {
        goTo(item.getType(), item.getId());
    }

    /*public void onItemSelected(FBRoom item) {
        setFragment(RoomMessagesFragment.newInstance(item));
    }*/

    public void onItemSelected(Room item) {
        setFragment(RoomFragment.newInstance(item));
    }

    public void onItemSelected(Conversation item) {
        //setFragment(ChatFragment.newInstance(item.getId(), 0));
        setFragment(ChatFragment.newInstance(item));
    }
    public void onItemSelected(Message item) {}
    public void onItemSelected(Comment item) {}




    public void comment(Model item) {
        setFragment(NoteFragment.newInstance(item.getId()));
    }

    public void passToComments(Post item){
        /*MyActivity.log("comment post from fragment");
        CommentsActivity.title = item.getTitle();
        CommentsActivity.type = "1";
        CommentsActivity.item_id = item.getId()+"";
        startActivity(new Intent(getActivity(), CommentsActivity.class));*/
    }


    void shareExternal(String sharedText) {
        MyActivity.log("Share post from fragment");
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_via)));
    }

    public void share(Model item) {
        String sharedText = ((Post)item).getTitle()+" \n => "+((Post) item).getWebLink();
        AlertUtils.INSTANCE.popupList(this, R.array.sharer_options, new AlertUtils.Alert() {
            @Override
            public void onAccept(@NotNull Object o) {
                int which = (int) o;
                switch (which){
                    case 0: {
                        openSharer(sharedText);
                        break;
                    }
                    case 1: {
                        shareExternal(sharedText);
                        break;
                    }
                    default: ;
                }
            }
        });
    }

    public void openSharer(String sharedText){
        setFragment(new GenericListFragment(new OnInteractListener<Object>() {
            @Override
            public void onInteract(Object item, @NotNull Action action) { onClick(item); }
            @Override
            public void onClick(Object o) {
                // share to private msgs
                new UCMessages().pushMessage(0, (int) o, sharedText, 0, new Closure<List<Message>>() {
                    @Override
                    public void onSuccess(List<Message> response) {
                        Toast.makeText(MyActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Failure failure) {
                        Toast.makeText(MyActivity.this, failure.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
    }







/*
    public void onItemSelected(Notification notification) {
        if(notification.getItemType().equals(1)){
            setFragment(TopicFragment.newInstance(notification.getItemId()));
        }else if(notification.getItemType().equals(3)){
            if(notification.getParentType() !=null && notification.getParentType()!=0)
                switch (notification.getParentType()){
                    case 1: // post
                        setFragment(TopicFragment.newInstance(notification.getParentId()));
                        break;
                }
            else{
                switch (notification.getItemType()){
                    case 4: //work
                        //setFragment(WorksShowFragment.newInstance(notification.getItemId()));
                }
            }
        }else if(notification.getItemType().equals(4)){
            //setFragment(WorksShowFragment.newInstance(notification.getItemId()));
        }
        else{
            MyActivity.log("Can't Handle Notification Type :/ "+getClass().getName());
        }
    }*/

    void goTo(int type, int id){
        MyActivity.log("Handle Notification: go to "+type+" with id "+id);

        switch (type){
            case ModelType.STORY: {
                setFragment(StoryFragment.Companion.newInstance(id));
            } break;
            case ModelType.PRODUCT: {
                setFragment(ProductFragment.Companion.newInstance(id));
            } break;
            case ModelType.NOTE: {
                setFragment(NoteFragment.newInstance(id));
            } break;
            case ModelType.POST: {
                setFragment(PostFragment.newInstance(id));
            } break;
            default: log("onItemSelected Post => Unknown Type !!!!!!!!!!!!!!!");
        }
    }
    public void onItemSelected(Notification notification) {
        switch (notification.getItemType()){
            case ModelType.COMMENT: {
                if(notification.getParentType()!=null && notification.getParentType()!=0)
                    goTo(notification.getParentType(), notification.getParentId());
            }
            default: goTo(notification.getItemType(), notification.getItemId());
        }
    }



}