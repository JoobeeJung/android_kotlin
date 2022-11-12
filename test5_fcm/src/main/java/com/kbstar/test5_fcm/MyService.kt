package com.kbstar.test5_fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//Q. 푸시 발송 시스템?
//Q. manager id 는 unique?

//fcm token 혹은 message를 전달받기 위한 서비스..
class MyService : FirebaseMessagingService() {
    override fun onNewToken(p0: String){
        super.onNewToken(p0)
        //앱의 식별자 토큰 전달하기 위해 자동호출.. 매개변수가 식별자..
        //원래.. 서버에서 사용해야하는 토큰이다.. 서버에 넘겨서.. 서버에 저장되게 해야한다..
        Log.d("kkang", "fcm token $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage){
        super.onMessageReceived(p0)
        //서버 메시지가 .. fcm 서버로부터 전달된 경우.. 매개변수가 메시지..
        //메시지 받아서.. 대부분의 경우.. notification을 띄우는..
        Log.d("kkang", "fcm message : ${p0.data}")

        //notification 은 시스템 영역인 status ba 에 우리 정보가 뜨는 것이다..
        //시스템에 의뢰한다.. 그 의뢰..
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager //의뢰자
        //Notification 은 직접 생성 안되고 Builder 로 대신 생성된다
        //버전 26부터 builder 에 channel 개념 도입..(광고 푸시 vs 정보성 푸시) 이전버전에는 channel 개념 없다.

        val builder: NotificationCompat.Builder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //O가 26버전 의미함
            val channel = NotificationChannel(
                "oneChannel",
                "oneName",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "description"//이 내용이 사용자 휴대폰 환경설정에 보이는 내용
            //만들어진 채널을 매니저에 등록..
            manager.createNotificationChannel(channel)
            //manager에 등록된 채널을 하나 이용해서 빌더를 만든다..
            builder = NotificationCompat.Builder(this, "oneChannel")
        }else
        {//하위 버전의 경우
            builder = NotificationCompat.Builder(this)
        }

        builder.run {
            //화면에 뜨는 정보.. Notification
            setSmallIcon(android.R.drawable.ic_notification_overlay)//알람에 들어가는 작은 아이콘
            setWhen(System.currentTimeMillis())
            setContentTitle("fcm")
            setContentText("${p0.data}")
            //클릭시 들어가는 화면 설정은 padding intents
        }
        //noti 발생..
        //padding intent
        manager.notify(11,builder.build()) //추후 cancel 메소드 사용 시 필요

    }
}