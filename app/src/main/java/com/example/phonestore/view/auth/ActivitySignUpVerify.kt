package com.example.phonestore.view.auth

import android.R.attr.phoneNumber
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import com.example.phonestore.R
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivitySignUpVerifyBinding
import com.example.phonestore.extendsion.*
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import java.util.concurrent.TimeUnit


class ActivitySignUpVerify: BaseActivity() {
    companion object{
        fun intentFor(context: Context, number: String): Intent =
            Intent(context, ActivitySignUpVerify::class.java).putExtra("phone", number)
    }
    private lateinit var binding: ActivitySignUpVerifyBinding
    private lateinit var auth: FirebaseAuth
    private var resendToken: ForceResendingToken? = null
    private var storedVerificationId: String? = null
    private var number: String? = ""
    private var flag = false
    private val timer = object: CountDownTimer(61000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            flag = false
            val seconds  = millisUntilFinished/1000
            binding.tvTimer.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
        }

        override fun onFinish() {
            binding.tvSendAgain.unEnabled()
            binding.tvSendAgain.setBackgroundColor(ContextCompat.getColor(this@ActivitySignUpVerify, com.example.phonestore.R.color.white))
            binding.tvSendAgain.setTextColor(ContextCompat.getColor(this@ActivitySignUpVerify, com.example.phonestore.R.color.red))
        }
    }
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            binding.edtOtp.setText(credential.smsCode)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.


            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.


            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }
    override fun setBinding() {

        binding = ActivitySignUpVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        number = intent.getStringExtra("phone")
        auth = FirebaseAuth.getInstance()
        auth.useAppLanguage()
        //auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
        //auth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(formatPhone(number), "123456");
        AppEvent.notifyShowPopUp()



    }
    override fun setUI() {
        binding.btnVerify.setOnClickListener {
            verifyVerificationCode(binding.edtOtp.text.toString().trim())
            //startActivity(ActivitySignUp.intentFor(this, number))
        }
        binding.edtOtp.addTextChangedListener {
            binding.tvOTPFail.gone()
        }
        binding.tvSendAgain.setOnClickListener {
            binding.tvOTPFail.gone()
            flag = true
            if(resendToken!=null) {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(formatPhone(number)!!) // Phone number to verify
                    .setTimeout(60, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this) // Activity (for callback binding)
                    .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                    .setForceResendingToken(resendToken!!) // ForceResendingToken from callbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
                AppEvent.notifyShowPopUp()
            }else{
                sendVerificationCode(formatPhone(number))
            }
            binding.tvSendAgain.enabled()
            binding.tvSendAgain.setBackgroundColor(ContextCompat.getColor(this@ActivitySignUpVerify, com.example.phonestore.R.color.white))
            binding.tvSendAgain.setTextColor(ContextCompat.getColor(this@ActivitySignUpVerify, com.example.phonestore.R.color.dray))
        }
    }

    override fun setToolBar() {
        binding.toolbarSignUpVerify.toolbar.title = "Xác nhận số điện thoại"
        setSupportActionBar(binding.toolbarSignUpVerify.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    private fun sendVerificationCode(phoneNumber: String?) {
        flag = true
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!)       // Phone number to verify
            .setTimeout(60, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    startActivity(ActivitySignUp.intentFor(this, number))

                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        binding.tvOTPFail.text = getString(R.string.otp_fail)
                        binding.tvOTPFail.visible()
                    }
                    // Update UI
                }
            }
    }
    private fun verifyVerificationCode(code: String?) {
        //creating the credential
        if(storedVerificationId!=null) {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code!!)
            signInWithPhoneAuthCredential(credential)
        }else  {
            binding.tvOTPFail.text = getString(R.string.otp_fail)
            binding.tvOTPFail.visible()
        }

    }
    private fun formatPhone(number: String?): String?{
        return if(number?.contains("+84") == false ){
            "+84$number"
        }else if(number?.contains("+84") == true) {
            if(!number.contains("+840"))
            number.replace("+84", "+840")
            else number
        }else number
    }

    override fun onResume() {
        super.onResume()
        if(flag) {
            binding.groupTimer.visible()
            timer.start()
            AppEvent.notifyClosePopUp()
        } else sendVerificationCode(formatPhone(number))
    }


}