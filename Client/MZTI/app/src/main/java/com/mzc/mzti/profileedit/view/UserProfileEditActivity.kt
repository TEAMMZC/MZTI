package com.mzc.mzti.profileedit.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.clearFocus
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.databinding.ActivityUserProfileEditBinding
import com.mzc.mzti.model.data.mbti.getProfileImgResId
import com.mzc.mzti.profileedit.viewmodel.UserProfileEditViewModel

private const val TAG: String = "UserProfileEditActivity"
const val KEY_USER_INFO_DATA: String = "user_info_data"

class UserProfileEditActivity : BaseActivity() {

    private val model: UserProfileEditViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[UserProfileEditViewModel::class.java]
    }
    private val binding: ActivityUserProfileEditBinding by lazy {
        ActivityUserProfileEditBinding.inflate(layoutInflater)
    }

    private val galleryIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    val selectedImgUri = result.data?.data
                    if (selectedImgUri != null) {
                        DLog.d(TAG, "selectedImgUri=$selectedImgUri")
                        model.setUserProfileImg(selectedImgUri)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setObserver()
        init()
    }

    private fun setObserver() {
        model.progressFlag.observe(this, Observer { flag ->
            if (flag) {
                showProgress()
            } else {
                dismissProgress()
            }
        })

        model.exceptionData.observe(this, Observer { exception ->
            exception.message?.let { msg ->
                model.setProgressFlag(false)
                showErrorMsg()
            }
        })

        model.apiFailMsg.observe(this, Observer { msg ->
            model.setProgressFlag(false)
            showToastMsg(msg)
        })

        model.userNickname.observe(this, Observer { userNickname ->
            binding.tvUserProfileEditNicknameWarning.visibility =
                if (userNickname.length in 1 until 9) View.GONE else View.VISIBLE
        })

        model.userMBTI.observe(this, Observer { userMBTI ->
            binding.tvUserProfileEditMbti.text = userMBTI.name
        })

        model.userProfileImg.observe(this, Observer { userProfileImg ->

        })
    }

    private fun init() {
        val id = MztiSession.userId
        val nickname = MztiSession.userNickname
        val mbti = MztiSession.userMbti

        binding.apply {
            // ID
            tvUserProfileEditId.text = id
            // 닉네임
            etUserProfileEditNickname.setText(nickname)
            // MBTI
            tvUserProfileEditMbti.text = mbti.name
            val defaultProfileImgRes = getProfileImgResId(mbti)
            // 프로필 사진
            Glide.with(this@UserProfileEditActivity)
                .load(MztiSession.userProfileImg)
                .placeholder(defaultProfileImgRes)
                .fallback(defaultProfileImgRes)
                .error(defaultProfileImgRes)
                .into(ivUserProfileEditUserProfile)

            // 완료 버튼
            tvUserProfileEditConfirm.setOnClickListener {

            }
            clUserProfileEditMbtiCard.setOnClickListener {
                val galleryIntent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                }
                galleryIntentForResult.launch(Intent.createChooser(galleryIntent, "프로필 사진 선택"))
            }

            etUserProfileEditNickname.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s?.toString() ?: ""
                    model.setUserNickname(input)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            etUserProfileEditNickname.setOnEditorActionListener { textView: TextView, actionId: Int, event: KeyEvent ->
                if (textView is EditText) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        textView.clearFocus(pIsHideKeyboard = true)
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
        }
    }

}