package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserDao mUserDao;
    private View decorView;
    private int	uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //홈 버튼 숨기기
        //몰입 모드 설정
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );

        final EditText[] userid = {findViewById(R.id.userid)};
        final EditText[] password = {findViewById(R.id.password)};
        Button  register = findViewById(R.id.register);
        Button login = findViewById(R.id.login);


        UserDatabase database = Room.databaseBuilder(getApplicationContext(),UserDatabase.class,"user_db")
                .fallbackToDestructiveMigration() // 스키마(데이터베이스) 버전 변경 가능
                .allowMainThreadQueries() // 메이쓰레드에서 db의 io를 가능하게 함
                .build();
        mUserDao = database.userDao(); // 인터페이스 객체 생성



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> userList = mUserDao.getUserAll();
                User user = new User(); // 객체 인스턴스 생성
                userid[0] = findViewById(R.id.userid);
                password[0] = findViewById(R.id.password);
                int existId = 0;
                for (int i = 0; i < userList.size(); i++) {
                    if(userList.get(i).getId().equals(userid[0].getText().toString())){
                        existId = 1;
                        break;
                    }
                }
                if(existId == 0) {
                    if(userid[0].getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"ID를 입력해주세요!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        user.setId(userid[0].getText().toString());
                        user.setPassword(password[0].getText().toString());
                        mUserDao.setInsertUser(user);
                        Toast.makeText(getApplicationContext(), "회원가입 완료!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"ID가 존재합니다!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> userList = mUserDao.getUserAll();
                userid[0] = findViewById(R.id.userid);
                password[0] = findViewById(R.id.password);
                int existId = 0;
                int passwordWrong = 0;
                for (int i = 0; i < userList.size(); i++) {
                    if(userList.get(i).getId().equals( userid[0].getText().toString())){
                        existId = 1;
                        if(userList.get(i).getId().equals(password[0].getText().toString())){
                            passwordWrong = 1;
                        }
                        break;
                    }
                }
                if(existId == 1) {
                    Toast.makeText(getApplicationContext(),"로그인 성공!",Toast.LENGTH_SHORT).show();
                    //다음 액티비티로 넘기기
                    //startActivity(new Intent(getApplicationContext(), nextActivity.class));
                }
                else if(passwordWrong == 1){
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"일치하는 회원정보가 없습니다!",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}