package app.qarya.presentation.fragments.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.qarya.BuildConfig
import app.qarya.R
import app.qarya.model.ModelType
import app.qarya.model.models.requests.RegisterRequest
import app.qarya.model.models.responses.AuthResponse
import app.qarya.model.shared.YDUserManager
import app.qarya.presentation.activities.MainActivity
import app.qarya.presentation.base.MyActivity
import app.qarya.presentation.delegates.OnTouchPasswordListener
import app.qarya.presentation.fragments.CategoriesFragment
import app.qarya.presentation.vms.VMAuth
import app.qarya.utils.MyConst
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_dialog_login.*
import timber.log.Timber
import tn.core.model.responses.BaseResponse
import tn.core.presentation.base.BaseDialogFragment
import tn.core.presentation.listeners.OnClickItemListener
import tn.core.presentation.listeners.OnSingleClickListener
import java.util.*

class LoginDialogFragment : BaseDialogFragment<VMAuth>() {

    private enum class FormState {
        BASE, REGISTRATION, EMAIL, FORGOTTEN_PASSWORD
    }
    private var actualFormState = FormState.BASE

    var loginDialogInterface:OnClickItemListener<AuthResponse>? = null

    var isRegistration:Boolean = false;
    

    companion object {
        fun newInstance() = LoginDialogFragment()

        fun newInstance(loginDialogInterface:OnClickItemListener<AuthResponse>?): LoginDialogFragment {
            val fragment = LoginDialogFragment()
            //val args = Bundle()
            fragment.loginDialogInterface = loginDialogInterface
            //fragment.setArguments(args)
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_login, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        prepareLoginFormNavigation()
        prepareInputBoxes()
        prepareActionButtons()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen)

        mViewModel = ViewModelProvider(this).get(VMAuth::class.java)
        mViewModel.callErrors.observe(this, Observer<List<String>> { this.onError(it) })
        mViewModel.loadStatus.observe(this, Observer<Boolean> { this.onStatusChanged(it) })
        mViewModel.getLiveData().observe(this, Observer<BaseResponse<AuthResponse>> { this.onDataReceived(it) })
        mViewModel.recover.observe(this, Observer<BaseResponse<Any>> { this.onRecoverResponse(it) })
    }

    var callbackManager:CallbackManager? = null;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callbackManager = CallbackManager.Factory.create();
        val EMAIL = "email"
        login_fb_button.setReadPermissions(Arrays.asList(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);


        // Callback registration
        login_fb_button.setFragment(this);
        login_fb_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                MyActivity.log("FACEBOOOOOOOOOOOOOK onSuccess", result?.accessToken?.userId)
                //Toast.makeText(context, result?.accessToken?.userId, Toast.LENGTH_LONG).show()
                isRegistration = false;
                mViewModel.loginFacebook(result!!.accessToken.userId, result!!.accessToken.token)
            }

            override fun onCancel() {
                MyActivity.log("FACEBOOOOOOOOOOOOOK onCancel")
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(context, error?.message, Toast.LENGTH_LONG).show()
                MyActivity.log("FACEBOOOOOOOOOOOOOK onError")
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        MyActivity.log("FACEBOOOOOOOOOOOOOK onActivityResult", requestCode.toString(), resultCode.toString(), data.toString())
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onError(errors: List<String>?) {
        super.onError(errors)
        if(!isRegistration){
            val shake = AnimationUtils.loadAnimation(context, R.anim.shake);
            login_form_registration_btn.startAnimation(shake);
        }
    }



    internal fun onRecoverResponse(model: BaseResponse<Any>?) {
        //MsgUtils.showToast(activity, MsgUtils.TOAST_TYPE_MESSAGE, getString(R.string.Check_your_email_we_sent_you_an_confirmation_email), MsgUtils.ToastLength.LONG)
        setVisibilityOfEmailForgottenForm(false)
    }

    fun onDataReceived(model: BaseResponse<AuthResponse>?) {
        if(model?.data != null){
            YDUserManager.save(model!!.data)
            if (loginDialogInterface != null) {
                loginDialogInterface!!.onClick(model.data)
            } else {
                Timber.e("Interface is null")
                //MsgUtils.showToast(activity, MsgUtils.TOAST_TYPE_INTERNAL_ERROR, null, MsgUtils.ToastLength.SHORT)
            }
            if (isRegistration){
                dismiss()
                (activity as MainActivity).setFragment(CategoriesFragment.newInstance(ModelType.POST))
            }else{
                (activity as MainActivity).refreshSoul()
                dismiss()
            }
        } else
            Toast.makeText(context, model.toString(), Toast.LENGTH_LONG).show()
    }



    private fun prepareInputBoxes() {
        // Registration form

        val registrationPassword = login_registration_password_wrapper?.getEditText()
        if (registrationPassword != null) {
            registrationPassword!!.setOnTouchListener(OnTouchPasswordListener(registrationPassword))
        }


        // Login email form
        val loginEmail = login_email_email_wrapper.getEditText()
        if (loginEmail != null) loginEmail!!.setText(YDUserManager.getUserEmailHint())
        val emailPassword = login_email_password_wrapper.getEditText()
        if (emailPassword != null) {
            emailPassword!!.setOnTouchListener(OnTouchPasswordListener(emailPassword))
            emailPassword!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == 124) {
                    invokeLoginWithEmail()
                    return@OnEditorActionListener true
                }
                false
            })
        }

        val emailForgottenPassword = login_email_forgotten_email_wrapper.getEditText()
        if (emailForgottenPassword != null)
            emailForgottenPassword!!.setText(YDUserManager.getUserEmailHint())

        // Simple accounts whisperer.
        /*val accounts = AccountManager.get(activity).accounts
        val addresses = arrayOfNulls<String>(accounts.size)
        for (i in accounts.indices) {
            addresses[i] = accounts[i].name
            Timber.e("Sets autocompleteEmails: %s", accounts[i].name)
        }

        val emails = ArrayAdapter(requireActivity().baseContext, android.R.layout.simple_dropdown_item_1line, addresses)
        login_registration_email_text_auto.setAdapter<ArrayAdapter<String>>(emails as ArrayAdapter<String>)*/

        acceptText.setOnClickListener{
            showTerms()
        }
    }

    internal fun showTerms() {
        val dialogBuilder = AlertDialog.Builder(activity)

        val wv = WebView(activity);
        wv.loadUrl(MyConst.TERMS);
        wv.setWebViewClient(object :WebViewClient(){
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }
        });

        dialogBuilder.setView(wv)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun prepareLoginFormNavigation() {
        // Registration
        login_form_registration_btn.setOnClickListener { setVisibilityOfRegistrationForm(true) }
        login_email_close_button.setOnClickListener {
            // Slow to display ripple effect
            Handler().postDelayed({ dismiss() }, 200)
        }

        login_registration_close_button.setOnClickListener {
            // Slow to display ripple effect
            Handler().postDelayed({ setVisibilityOfRegistrationForm(false) }, 200)
        }

        // Email forgotten password
        login_email_forgotten_password.setOnClickListener { setVisibilityOfEmailForgottenForm(true) }
        login_email_forgotten_back_button.setOnClickListener {
            // Slow to display ripple effect
            Handler().postDelayed({ setVisibilityOfEmailForgottenForm(false) }, 200)
        }
    }

    private fun prepareActionButtons() {

        login_email_confirm.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                invokeLoginWithEmail()
            }
        })

        login_registration_confirm.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                invokeRegisterNewUser()
            }
        })

        login_email_forgotten_confirm.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                invokeResetPassword()
            }
        })
    }

    private fun invokeRegisterNewUser() {
        hideSoftKeyboard()
        registerNewUser(
                login_registration_email_wrapper.getEditText()!!,
                login_registration_password_wrapper.getEditText()!!,
                login_registration_password_wrapper.getEditText()!!,
                login_registration_firstname_wrapper.getEditText()!!
        )
    }

    private fun registerNewUser(editTextEmail: EditText,
                                editTextPassword: EditText,
                                editTextPasswordConfirmation: EditText,
                                editTextFirstName: EditText) {
        YDUserManager.setUserEmailHint(editTextEmail.text.toString())

        isRegistration = true;
        mViewModel.register(
                RegisterRequest(
                    editTextFirstName.text.toString().trim { it <= ' ' },
                    editTextEmail.text.toString().trim { it <= ' ' },
                    editTextPassword.text.toString().trim { it <= ' ' }
                    //newUser.setAgree(if (accept.isChecked()) "1" else "0")
                )
        )
    }

    private fun invokeLoginWithEmail() {
        hideSoftKeyboard()
        logInWithEmail(login_email_email_wrapper.getEditText()!!, login_email_password_wrapper.getEditText()!!)
    }

    private fun logInWithEmail(editTextEmail: EditText, editTextPassword: EditText) {
        if (validate(editTextEmail, editTextPassword)){
            YDUserManager.setUserEmailHint(editTextEmail.text.toString())
            isRegistration = false;
            mViewModel.login(editTextEmail.text.toString().trim { it <= ' ' }, editTextPassword.text.toString().trim { it <= ' ' })
        }
    }

    fun validate(inputEmail: EditText, inputPassword: EditText): Boolean {
        var valid = true

        val email = inputEmail.getText().toString()
        val password = inputPassword.getText().toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address")
            valid = false
        } else {
            inputEmail.setError(null)
        }

        if (password.isEmpty() || password.length < 6) {
            inputPassword.setError(getString(R.string.minimum_password))
            valid = false
        } else {
            inputPassword.setError(null)
        }

        return valid
    }

    private fun invokeResetPassword() {
        val emailForgottenPasswordEmail = login_email_forgotten_email_wrapper.getEditText()
        if (emailForgottenPasswordEmail == null || emailForgottenPasswordEmail!!.getText().toString().equals("", ignoreCase = true)) {
            login_email_forgotten_email_wrapper.setErrorEnabled(true)
            login_email_forgotten_email_wrapper.setError(getString(R.string.Required_field))
        } else {
            login_email_forgotten_email_wrapper.setErrorEnabled(false)
            resetPassword(emailForgottenPasswordEmail!!)
        }
    }


    private fun resetPassword(emailOfForgottenPassword: EditText) {
        mViewModel.recover(emailOfForgottenPassword.text.toString().trim { it <= ' ' })
    }

    private fun hideSoftKeyboard() {
        if (activity != null && view != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }
    }

    private fun setVisibilityOfRegistrationForm(setVisible: Boolean) {
        if (setVisible) {
            actualFormState = FormState.REGISTRATION
            login_email_form.setVisibility(View.INVISIBLE)
            login_registration_form.setVisibility(View.VISIBLE)
        } else {
            actualFormState = FormState.EMAIL
            login_email_form.setVisibility(View.VISIBLE)
            login_registration_form.setVisibility(View.INVISIBLE)
            hideSoftKeyboard()
        }
    }

    private fun setVisibilityOfEmailForm(setVisible: Boolean) {
        actualFormState = FormState.EMAIL
        login_email_form.setVisibility(View.VISIBLE)
        hideSoftKeyboard()
    }

    private fun setVisibilityOfEmailForgottenForm(setVisible: Boolean) {
        if (setVisible) {
            actualFormState = FormState.FORGOTTEN_PASSWORD
            login_email_form.setVisibility(View.INVISIBLE)
            login_email_forgotten_form.setVisibility(View.VISIBLE)
        } else {
            actualFormState = FormState.EMAIL
            login_email_form.setVisibility(View.VISIBLE)
            login_email_forgotten_form.setVisibility(View.INVISIBLE)
        }
        hideSoftKeyboard()
    }


    override fun onStart() {
        super.onStart()
        val d = dialog
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            val window = d.window
            window!!.setLayout(width, height)
            //window.setWindowAnimations(R.style.dialogFragmentAnimation)
            d.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
                if (BuildConfig.DEBUG)
                    Timber.d("onKey: %d (Back=%d). Event:%d (Down:%d, Up:%d)", keyCode, KeyEvent.KEYCODE_BACK, event.action,
                            KeyEvent.ACTION_DOWN, KeyEvent.ACTION_UP)
                if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
                    when (actualFormState) {
                        FormState.REGISTRATION -> {
                            if (event.action == KeyEvent.ACTION_UP) {
                                setVisibilityOfRegistrationForm(false)
                            }
                            return@OnKeyListener true
                        }
                        FormState.FORGOTTEN_PASSWORD -> {
                            if (event.action == KeyEvent.ACTION_UP) {
                                setVisibilityOfEmailForgottenForm(false)
                            }
                            return@OnKeyListener true
                        }
                        FormState.EMAIL -> {
                            if (event.action == KeyEvent.ACTION_UP) {
                                setVisibilityOfEmailForm(false)
                            }
                            return@OnKeyListener true
                        }
                        else -> return@OnKeyListener false
                    }
                }
                false
            })
        }
    }
}
