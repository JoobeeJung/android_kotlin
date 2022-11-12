package com.kbstar.test4_provider

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.kbstar.test4_provider.databinding.ActivityMainBinding
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    //퍼미션 설정 요청자..퍼미션좀 설정해줘 요청하는 요청자(권한 설정 거부하면 이용못하니까)
    lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    //주소록 목록화면 요청자..
    lateinit var contactsLauncher: ActivityResultLauncher<Intent>

    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions() //요청 처리자..
        ){
            //요청후.. 콜백.. 사후처리.. 람다함수
            //퍼미션 부여 다이얼로그가 닫힌 후에 호출..
            for(entry in it.entries)
            {
                if(entry.key == "android.permission.READ_CONTACTS" && entry.value)
                {   // 퍼미션이 유저에 의해 부여된거라면..
                    //주소록의 목록 activity를 intent로 실행시킨다.
                    val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                    contactsLauncher.launch(intent) //이상 없으면 주소록 목록화면이 뜨게 된다.
                }else
                {
                    //퍼미션이 유저에 의해 거부된거라면..
                    Toast.makeText(this,"request permission for contacts", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //2.. 퍼미션 부여 다일얼로그
        contactsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()//요청 처리자.. intent 발생자..
        ){
            //주소록 목록화면에서 되돌아왔을 때 콜백.. 사후처리
            if(it.resultCode == RESULT_OK)
            {
                //유저가 하나를 선택해서 돌아온 경우
                //목록에서 되돌아오면 유저가 선택한 홍길동의 식별자만..
                //식별자를 조건으로 구체적으로 원하는 데이터를 주소록 provider에게 다시 요청해야..
                val cursor = contentResolver.query(
                    it!!.data!!.data!!,//여기에 주소록 provider 지칭하는 url.. 홍길동 식별자 ..
                    arrayOf(//획득하고자 하는 데이터..
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    ),
                    null, null, null
                )

                if(cursor!!.moveToFirst()){
                    //넘어온 데이터가 있다면..
                    //화면에 출력..
                    binding.contactResultView.text = "name : ${cursor?.getString(0)} , phone: ${cursor?.getString(1)}"
                }
            }
        }
        //1...유저가 화면 버튼 눌렀을 때
        binding.contactButton.setOnClickListener{
            //4...
            //현재 주소록 퍼미션이 부여된건지 판단..
            if(ContextCompat.checkSelfPermission(
                    this, "android.permission.READ_CONTACTS"
            ) != PackageManager.PERMISSION_GRANTED){
                //퍼미션이 거부상태라고 하면.. 퍼미션 부여해달라고.. 유저에게 요청..
                permissionLauncher.launch(arrayOf("android.permission.READ_CONTACTS"))
            }
            else
            {
                //퍼미션이 부여 상태
                val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                contactsLauncher.launch(intent)
            }
        }

        // gallery app ~~~~~~~~~~~~~~~~~~
        val galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            //gallery에서 되돌아 왔을 때 사후처리
            try {
                //BitmapFactory 작업 옵션..
                val option = BitmapFactory.Options()
                option.inSampleSize = 10 // 10분의 1로 줄여서 이미지 로딩..

                //gallery app의 provider가 이미지를 읽을 수 있는 InputStream 제공한다..
                val inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream,null, option)
                inputStream!!.close()
                //이미지 다 읽음

                bitmap?.let {
                    //읽은 이미지가 있다면.. 화면출력..
                    binding.userImageView.setImageBitmap(bitmap)
                } ?: let{
                    //읽은게 없다면..
                    Log.d("kkang","no image")
                }
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        binding.galleryButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }

        //camera app 연동 사후 처리..
        val cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            //camera app이 저장한 파일을 읽어서.. 화면 출력
            val option = BitmapFactory.Options()
            option.inSampleSize = 10

            //파일에서 이미지 로딩..
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap.let{
                binding.userImageView.setImageBitmap(bitmap)
            }
        }

        binding.cameraButton.setOnClickListener{
            //file 준비
            val tempStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            //외장메모리공간.. 앱별 저장소.. 사진파일 저장소..
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${tempStamp}_",
                ".jpg",
                storageDir
            )
            filePath = file.absolutePath
            //camera app에게 공유하기위한 파일 정보..
            val photoURI = FileProvider.getUriForFile(
                this,
                "com.kbstar.test4_provider",
                file
            )
            //camera app 실행..
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraLauncher.launch(intent)
        }

    }

}
