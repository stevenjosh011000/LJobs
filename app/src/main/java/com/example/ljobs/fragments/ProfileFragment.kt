package com.example.ljobs.fragments


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ljobs.Edu.EduDao
import com.example.ljobs.Edu.EduEntity
import com.example.ljobs.Edu.LanguageDao
import com.example.ljobs.Edu.LanguageEntity
import com.example.ljobs.EduItemAdapter
import com.example.ljobs.R
import com.example.ljobs.Session.LoginPref
import com.example.ljobs.User.UserDao
import com.example.ljobs.User.UserEntity
import com.example.ljobs.UserApp
import com.example.ljobs.ViewPdfActivity
import com.example.ljobs.databinding.DialogEducationInfoBinding
import com.example.ljobs.databinding.DialogLanguageBinding
import com.example.ljobs.databinding.DialogProfileUpdateBinding
import com.example.ljobs.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


class ProfileFragment : Fragment() {

    //region Ini Variables
    lateinit var session : LoginPref
    lateinit var permissionLauncher : ActivityResultLauncher<Array<String>>
    lateinit var userDao : UserDao
    lateinit var eduDao : EduDao
    lateinit var languageDao : LanguageDao
    lateinit var resume: String
    lateinit var resumeName: String
    lateinit var binding : FragmentProfileBinding
    var id: Int? = null
    var email: String? = null
    var imageBytes : String? = null
    var fileName : String? = null
    var isReadPermissionGranted = false
    var isWritePermissionGranted = false
    var isManagePermissionGranted = false
    var resumeUpdated = false
    //endregion


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = LoginPref(activity?.applicationContext!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //region Ini Variables
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false)
        val typeFace: Typeface = Typeface.createFromAsset(activity?.applicationContext?.assets,"goodTimesRg.otf")
        var user:HashMap<String,String> = session.getUserDetails()
        val pass: String = user.get(LoginPref.KEY_PASS).toString()
        email = user.get(LoginPref.KEY_EMAIL).toString()
        resume = user.get(LoginPref.RESUME).toString()
        resumeName = user.get(LoginPref.RESUME_NAME).toString()
        eduDao = (activity?.applicationContext as UserApp).db.eduDao()
        userDao = (activity?.applicationContext as UserApp).db.userDao()
        languageDao = (activity?.applicationContext as UserApp).db.languageDao()
        id = user.get(LoginPref.KEY_ID)?.toInt()!!
        binding.tvProfileName.typeface = typeFace
        //endregion

        //region Get Personal Data
        lifecycleScope.launch{
            userDao?.fetchUserByEmail(user.get(LoginPref.KEY_EMAIL).toString())?.collect{
                if(it!=null){
                    var name : String = it.name?.subSequence(0,2).toString()
                    binding.tvProfilePic.text = name.uppercase()
                    binding.tvProfileName.text = it.name
                    binding.tvProfileEmail.text = it.email
                    binding.tvProfilePhone.text = "+60"+it.phoneNum
                }
            }
        }
        lifecycleScope.launch{
            eduDao.fetchAllEduByEmail(email!!).collect(){
                var list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, eduDao, container?.context!!)
            }
        }

        lifecycleScope.launch{
            languageDao.fetchAllLanByEmail(email!!).collect(){
                var allLanguages : String = ""
                var count = 0
                if(it!=null) {
                    if(it.english == 1){
                        allLanguages += "English "
                        count++
                    }
                    if(it.chinese == 1){
                        allLanguages += ",Chinese "
                        count++
                    }
                    if(it.bahasaMelayu == 1){
                        allLanguages += ",Bahasa Melayu "
                        count++
                    }
                    if(it.tamil == 1){
                        allLanguages += ",Tamil "
                        count++
                    }
                    if(it.cantonese == 1){
                        allLanguages += ",Cantonese "
                        count++
                    }
                    if(it.hakka == 1){
                        allLanguages += ",Hakka "
                        count++
                    }
                    if(it.hokkien == 1){
                        allLanguages += ",Hokkien "
                        count++
                    }
                    if(it.hindi == 1){
                        allLanguages += ",Hindi "
                        count++
                    }
                    if(count == 0){
                        binding.languages.text = "Please add all the languages you know."
                    }else {
                        binding.languages.text = allLanguages
                        binding.languages.setTextColor(Color.BLACK)
                    }
                }
            }
        }


        //endregion

        //region Checking Permissions
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->

            isReadPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted
            isWritePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermissionGranted
            isManagePermissionGranted = permissions[Manifest.permission.MANAGE_EXTERNAL_STORAGE] ?: isManagePermissionGranted
        }
        //endregion

        //region Checking Resume & Update Resume Listener & View Resume Listener
        if(!resume.isEmpty() && !resumeName.isEmpty()){
            binding.resume.visibility = View.VISIBLE
            binding.resumeTv.visibility = View.GONE
            if(resumeName.length > 15) {
                binding.resumeNameTv.text = resumeName.substring(0, 15) + "..."
            }
            binding.resumeNameTv.visibility = View.VISIBLE
        }

        binding.resume.setOnClickListener {
            val intent = Intent(context?.applicationContext,ViewPdfActivity::class.java)
            startActivity(intent)
        }

        binding.resumeAdd.setOnClickListener {

            if(isReadPermissionGranted && isWritePermissionGranted && isManagePermissionGranted){
                startFileChooser()
            }else{
                requestPermission(container?.context!!)
            }
            lifecycleScope.launch{

                userDao.fetchUserById(id!!).collect {
                    if (it != null) {
                        if(it.resumeStatus.toString() == "1") {
                            binding.resumeNameTv.setText(it.resumeName.toString())
                            binding.resume.visibility = View.VISIBLE
                            binding.resumeTv.visibility = View.GONE
                            if(resumeName.length > 15) {
                                binding.resumeNameTv.text = resumeName.substring(0, 15) + "..."
                            }
                            binding.resumeNameTv.visibility = View.VISIBLE

                        }
                    }
                }

            }
        }
        //endregion

        //region updates languages
        binding.languagesAdd.setOnClickListener {
            val languageDialog= Dialog(container?.context!!, R.style.Theme_Dialog)
            languageDialog.setCancelable(false)

            val bindinglangueges = DialogLanguageBinding.inflate(layoutInflater)
            languageDialog.setContentView(bindinglangueges.root)

            lifecycleScope.launch{
                languageDao.fetchAllLanByEmail(email!!).collect(){

                    if(it!=null) {
                        if(it.english == 1){
                            bindinglangueges.english.isChecked = true
                        }
                        if(it.chinese == 1){
                            bindinglangueges.chinese.isChecked = true
                        }
                        if(it.bahasaMelayu == 1){
                            bindinglangueges.bahasaMelayu.isChecked = true
                        }
                        if(it.tamil == 1){
                            bindinglangueges.tamil.isChecked = true
                        }
                        if(it.cantonese == 1){
                            bindinglangueges.cantonese.isChecked = true
                        }
                        if(it.hakka == 1){
                            bindinglangueges.hakka.isChecked = true
                        }
                        if(it.hokkien == 1){
                            bindinglangueges.hokkien .isChecked = true
                        }
                        if(it.hindi == 1){
                            bindinglangueges.hindi.isChecked = true
                        }

                    }
                }
            }


            bindinglangueges.tvUpdate.setOnClickListener {

                var english : Int = 0
                if(bindinglangueges.english.isChecked){
                    english = 1
                }

                var chinese : Int = 0
                if(bindinglangueges.chinese.isChecked){
                    chinese = 1
                }

                var bahasaMelayu : Int = 0
                if(bindinglangueges.bahasaMelayu.isChecked){
                    bahasaMelayu = 1
                }

                var tamil : Int = 0
                if(bindinglangueges.tamil.isChecked){
                    tamil = 1
                }

                var cantonese : Int = 0
                if(bindinglangueges.cantonese.isChecked){
                    cantonese = 1
                }

                var hakka : Int = 0
                if(bindinglangueges.hakka.isChecked){
                    hakka = 1
                }

                var hokkien : Int = 0
                if(bindinglangueges.hokkien.isChecked){
                    hokkien = 1
                }

                var hindi : Int = 0
                if(bindinglangueges.hindi.isChecked){
                    hindi = 1
                }


                lifecycleScope.launch{
                    languageDao.update(english=english, bahasaMelayu = bahasaMelayu, chinese = chinese, cantonese = cantonese, hakka = hakka, hokkien = hokkien, hindi = hindi, tamil = tamil, email = email!!)

                    Toast.makeText(
                        container?.context!!,
                        "Updated Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    languageDialog.dismiss()
                }
            }

            bindinglangueges.tvCancel.setOnClickListener {
                languageDialog.dismiss()
            }

            languageDialog.show()
        }
        //endregion

        //region add educational info
        binding.educationAdd.setOnClickListener{
            val eduDialog= Dialog(container?.context!!, R.style.Theme_Dialog)
            eduDialog.setCancelable(false)

            val bindingEduLvl = DialogEducationInfoBinding.inflate(layoutInflater)
            eduDialog.setContentView(bindingEduLvl.root)

            ArrayAdapter.createFromResource(container?.context!!,R.array.eduLvl,android.R.layout.simple_spinner_item).also{
                    adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                bindingEduLvl.eduLvlSpinner.adapter = adapter
            }

            ArrayAdapter.createFromResource(container?.context!!,R.array.eduYear,android.R.layout.simple_spinner_item).also{
                    adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                bindingEduLvl.eduYearSpinner.adapter = adapter
            }

            bindingEduLvl.tvUpdate.setOnClickListener {
                lifecycleScope.launch{
                    eduDao.insert(EduEntity(email = email, eduLvl = bindingEduLvl.eduLvlSpinner.selectedItem.toString(), eduCert = bindingEduLvl.certName.text.toString(), eduSchool = bindingEduLvl.schoolName.text.toString(), eduEndYear = bindingEduLvl.eduYearSpinner.selectedItem.toString()))
                    Toast.makeText(
                        container?.context!!,
                        "Inserted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    eduDialog.dismiss()
                }
            }

            bindingEduLvl.tvCancel.setOnClickListener {
                eduDialog.dismiss()
            }

            eduDialog.show()

        }
        //endregion

        //region Update Profile
        binding.ivEdit.setOnClickListener {
            updateProfileDialog(id!!,email!!,pass,userDao!!,container?.context!!)
        }
        //endregion

        // Inflate the layout for this fragment
        return binding.root
    }

    //region Choose PDF
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

                val data = result!!.data
                val sUri: Uri = data?.data!!
                val path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )

//                imageBytes = Base64.decode(convertToBase64(File(path,name)), Base64.DEFAULT)
//              val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            if(data.toString().contains("downloads")){
            fileName = DocumentFile.fromSingleUri(context?.applicationContext!!,Uri.parse(sUri.toString()))?.name.toString()
            imageBytes = convertToBase64(File(path,fileName))
                lifecycleScope.launch{
                    userDao?.update(resume = imageBytes.toString(), resumeName = fileName.toString(), resumeStatus = "1" ,id = id!!)
                    session.updateResumeSession(resume = imageBytes.toString(), resumeName = fileName.toString())
                    resume = imageBytes.toString()
                    resumeName = fileName.toString()
                    resumeUpdated = true
                    Toast.makeText(activity?.applicationContext,"Resume Updated.", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(activity?.applicationContext,"Please select PDF from downloads folder", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun startFileChooser(){
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("application/pdf")
        startForResult.launch(Intent.createChooser(intent,"Choose PDF"))
    }


    //endregion

    //region Dialog for updating profile
    private fun updateProfileDialog(id:Int, email:String, currentPass:String, userDao: UserDao, context: Context){

        val updateDialog= Dialog(context, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        val binding = DialogProfileUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch{
            userDao.fetchUserByEmail(email).collect{
                if(it !=null){
                    binding.nameUp.setText(it.name.toString())
                    binding.phoneUp.setText(it.phoneNum.toString())
                }
            }
        }
        binding.editPassword.setOnClickListener {
            if(binding.editPassword.isChecked){
                binding.oldPassContainer.visibility = View.VISIBLE
                binding.newPasswordContainer.visibility = View.VISIBLE
                binding.confirmPassContainer.visibility = View.VISIBLE
            }else{
                binding.oldPassContainer.visibility = View.GONE
                binding.newPasswordContainer.visibility = View.GONE
                binding.confirmPassContainer.visibility = View.GONE
            }
        }

        binding.tvUpdate.setOnClickListener {
            val name = binding.nameUp.text.toString()
            val phone = binding.phoneUp.text.toString()
            val oldPassword = binding.oldPass.text.toString()
            val password = binding.newPassword.text.toString()
            val confirmPass = binding.confirmPass.text.toString()

            binding.nameContainerUp.helperText = nameValidation(name)
            binding.phoneContainerUp.helperText = phoneValidation(phone)
            binding.newPasswordContainer.helperText = passwordValidation(password,oldPassword)
            binding.confirmPassContainer.helperText = confirmPasswordValidation(confirmPass,password)

            if(binding.editPassword.isChecked){
                if( nameValidation(name)==""&&
                    phoneValidation(phone)==""&&
                    passwordValidation(password, oldPassword)==""&&
                    confirmPasswordValidation(confirmPass,password)==""){

                    if(currentPass != md5(oldPassword)){
                        binding.oldPassContainer.helperText = "Old password does not match"
                    }else{
                        lifecycleScope.launch{
                            userDao.update(UserEntity(id=id,password= md5(password),email=email,name= name, phoneNum = phone))
                            session.createLoginSession(id.toString(),email,md5(password),resume,resumeName)
                            Toast.makeText(activity?.applicationContext,"Record Updated.", Toast.LENGTH_LONG).show()
                            updateDialog.dismiss()
                        }
                    }
                }
            }else{
                if(nameValidation(name)==""&&
                    phoneValidation(phone)==""){
                    lifecycleScope.launch{
                        userDao.update(UserEntity(id=id,password= currentPass,email=email,name= name, phoneNum = phone))
                        Toast.makeText(activity?.applicationContext,"Record Updated.", Toast.LENGTH_LONG).show()
                        updateDialog.dismiss()
                    }
                }
            }
        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()

    }
    //endregion

    //region All Validations
    private fun confirmPasswordValidation(confirmPass:String,newPassword:String):String{
        if(confirmPass.isEmpty()){
            return "Confirm password is required"
        }

        if(newPassword != confirmPass){
            return "Confirm password does not match the password"
        }

        return ""
    }

    private fun passwordValidation(newPassword:String,currentPass: String):String{

        if(newPassword.isEmpty()){
            return "Password is required"
        }

        if(newPassword.length<6){
            return "Minimum 6 character password"
        }

        if(!newPassword.matches(".*[A-Z].*".toRegex())){
            return "Must contain 1 upper-case character"
        }

        if(!newPassword.matches(".*[a-z].*".toRegex())){
            return "Must contain 1 lower-case character"
        }

        if(!newPassword.matches(".*[@#\$%^&+=].*".toRegex())){
            return "Must contain 1 special character"

        }

        if(newPassword == currentPass){
            return "New Password cannot be the same as current password"
        }

        return ""
    }

    private fun phoneValidation(phone: String) : String{

        if(phone.isEmpty()){
//            binding.phoneContainerUp.helperText = "Phone number is required"
            return "Phone number is required"
        }

        if(phone.length < 9){
//            binding.phoneContainerUp.helperText = "Phone Number : 9-10 digits"
            return "Phone Number : 9-10 digits"
        }

        if(phone.length > 10){
//            binding.phoneContainerUp.helperText = "Phone Number : 9-10 digits"
            return "Phone Number : 9-10 digits"
        }

        return ""
    }

    private fun nameValidation(name: String) : String{
        if(name.isEmpty()){
            return "Name is required"
        }

        if(name.length<2) {
            return "Minimum 3 character"
        }

        return ""
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun requestPermission(context: Context){

        isReadPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWritePermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                startActivity(intent)
            }else{
                isManagePermissionGranted = true
            }
        }

        val permissionRequest : MutableList<String> = ArrayList()
        if(!isManagePermissionGranted){
            permissionRequest.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
        if(!isWritePermissionGranted){
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if(!isReadPermissionGranted){
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


        if(permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    fun convertToBase64(attachment: File): String {
        return Base64.encodeToString(attachment.readBytes(), Base64.NO_WRAP)
    }
    //endregion

    private fun setupListOfDataIntoRecyclerView(eduList:ArrayList<EduEntity>, eduDao: EduDao, context: Context){
        if(eduList.isNotEmpty()){
            val itemAdapter = EduItemAdapter(eduList,
                {   updateId ->
                    updateEduDialog(updateId,eduDao,context)
                },
                {
                        deleteId ->
                    deleteEduDialog(deleteId,eduDao,context)
                }

            )
            binding.rvEduList.layoutManager = LinearLayoutManager(context)
            binding.rvEduList.adapter = itemAdapter
            binding.rvEduList.visibility = View.VISIBLE
            binding.eduInfo.visibility = View.GONE

        }else{
            binding.rvEduList.visibility = View.GONE
            binding.eduInfo.visibility = View.VISIBLE
        }
    }

    private fun updateEduDialog(id:Int, employeeDao: EduDao, context: Context){
        val updateDialog= Dialog(context, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val bindingEdu = DialogEducationInfoBinding.inflate(layoutInflater)
        updateDialog.setContentView(bindingEdu.root)
        var eduNewLvl : String = ""
        var eduNewEndYear : String = ""

        ArrayAdapter.createFromResource(context,R.array.eduLvl,android.R.layout.simple_spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bindingEdu.eduLvlSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(context,R.array.eduYear,android.R.layout.simple_spinner_item).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bindingEdu.eduYearSpinner.adapter = adapter
        }

        bindingEdu.eduLvlSpinner.visibility = View.GONE
        bindingEdu.eduYearSpinner.visibility = View.GONE
        bindingEdu.editOldEndYear.visibility = View.VISIBLE
        bindingEdu.editOldEduLvl.visibility = View.VISIBLE

        lifecycleScope.launch{
            employeeDao.fetchEduById(id).collect{
                if(it != null){
                    bindingEdu.certName.setText(it.eduCert.toString())
                    bindingEdu.schoolName.setText(it.eduSchool.toString())
                    bindingEdu.eduOldLvl.setText("Education Level : " + it.eduLvl.toString())
                    bindingEdu.eduOldEndYear.setText("End Year : " + it.eduEndYear.toString())
                    eduNewLvl = it.eduLvl.toString()
                    eduNewEndYear = it.eduEndYear.toString()
                }
            }
        }

        bindingEdu.editOldEndYear.setOnClickListener {
            bindingEdu.eduYearSpinner.visibility = View.VISIBLE
            if(!bindingEdu.editOldEndYear.isChecked){
                bindingEdu.eduYearSpinner.visibility = View.GONE
            }
        }

        bindingEdu.editOldEduLvl.setOnClickListener {
            bindingEdu.eduLvlSpinner.visibility = View.VISIBLE
            if(!bindingEdu.editOldEduLvl.isChecked){
                bindingEdu.eduLvlSpinner.visibility = View.GONE
            }
        }

        bindingEdu.tvUpdate.setOnClickListener {
            val certName = bindingEdu.certName.text.toString()
            val schoolName = bindingEdu.schoolName.text.toString()

            if(bindingEdu.editOldEduLvl.isChecked){
                eduNewLvl = bindingEdu.eduLvlSpinner.selectedItem.toString()
            }else{

            }
            if(bindingEdu.editOldEndYear.isChecked){
                eduNewEndYear = bindingEdu.eduYearSpinner.selectedItem.toString()
            }

            if(certName.isNotEmpty() && schoolName.isNotEmpty()){
                lifecycleScope.launch{
                    employeeDao.update(EduEntity(id, email = email, eduCert = certName, eduSchool = schoolName, eduLvl = eduNewLvl, eduEndYear = eduNewEndYear))
                    Toast.makeText(context,"Record Updated.",Toast.LENGTH_LONG).show()
                    updateDialog.dismiss()
                }
            }else{
                Toast.makeText(context,"Certificate or School Name cannot be blank.",Toast.LENGTH_LONG).show()
            }
        }

        bindingEdu.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()

    }

    private fun deleteEduDialog(id:Int, employeeDao: EduDao, context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Record")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes"){
                dialogInterface, _ ->
            lifecycleScope.launch{
                employeeDao.delete(EduEntity(id))
                Toast.makeText(context,"Record deleted successful",Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface,_->
            dialogInterface.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        builder.show()
    }



}