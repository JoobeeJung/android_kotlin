package com.kbstar.androidkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.kbstar.androidkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //ViewBinding은 tool이다. code generator 이다
    //view 선언과 view 획득을 위한 findViewById 코드를 자동으로 만들어준다.
    //layout xml을보고 ..
    //activity_main.xml ==> ActivityMainBinding 클래스를 자동으로 만들고 그곳에 넣는다
    //item.xml ==> ItemBinding

//    lateinit var nameView: EditText
//    lateinit var phoneView: EditText
//    lateinit var saveButton: Button
    var user:User ?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //Binding 객체.. 획득.. 최소한의 준비
//        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        nameView = findViewById(R.id.nameView);
//        phoneView = findViewById(R.id.editTextPhone);
//        saveButton = findViewById(R.id.saveButton);

        binding.saveButton.setOnClickListener {

            val name:String = binding.nameView.text.toString();
            val phone:String = binding.editTextPhone.text.toString();
            user = User(name,phone,User.USER)

            Toast.makeText(this@MainActivity, "save ok", Toast.LENGTH_SHORT).show()

        }
//
//        saveButton.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view:View)
//            {
//                val name:String = nameView.text.toString();
//                val phone:String = phoneView.text.toString();
//                user = User(name,phone,User.USER)
//
//                Toast.makeText(this@MainActivity, "save ok", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    open override fun onCreateOptionsMenu(menu: Menu) : Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    open override fun onOptionsItemSelected(item: MenuItem) : Boolean
    {
        if(item.itemId == R.id.menu_print){
            user?.run{
                //not null
                Log.d("kkang","name: ${user!!.name}, phone: ${user!!.phone}, role: ${user!!.role}");

            } ?: run {
                //null
                Log.d("kkang","user not saved...");

            }

        }
        return super.onOptionsItemSelected(item);
    }
}


