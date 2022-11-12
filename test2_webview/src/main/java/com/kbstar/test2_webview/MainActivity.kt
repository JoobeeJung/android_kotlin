package com.kbstar.test2_webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import com.kbstar.test2_webview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //js에게 공개하기 위한 클래스..
    class JavascriptTest {
        //이 클래스의 객체가 js에 공개되었다고 하더라도.. 아래의 어노테이션이 추가된 함수만 호출 가능
        @get:JavascriptInterface
        val webData: String
            get() { //이 get함수를 js가 call
                //진동울리는 코드, 카메라 열기 카드 등
                val sb = StringBuffer()
                sb.append("[")
                for(i in 0..9)
                {
                    sb.append("[$i,") //x축 좌표
                    sb.append("${(0..100).random()}") //y축 좌표
                    sb.append("]")
                    if(i<9) sb.append(",")
                }
                sb.append("]")

                return sb.toString()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //webView의 js engine이 기본 disable...
        binding.webView.settings.run {
            javaScriptEnabled = true
        }

        binding.webView.run {
            //js에게 객체 공개..
            //android라는 단어는 개발자 임의 단어. 공개한 객체의 js 이름
            addJavascriptInterface(JavascriptTest(),"android")
            //초기 html 로딩..
            loadUrl("file:///android_asset/test.html")
            //browser 이벤트 등록
            webChromeClient = object : WebChromeClient(){
                override fun onJsAlert(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    //js alert의 문자열을 toast로 띄우고..
                    //js alert 닫아버린다
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    result?.confirm()
                    return true
                }
            }
        }
        binding.webButton.setOnClickListener{
            //js 함수 호출 네이티브 버튼 클릭시
            binding.webView.loadUrl("javascript:jsFunction('hello')")
        }
    }
}