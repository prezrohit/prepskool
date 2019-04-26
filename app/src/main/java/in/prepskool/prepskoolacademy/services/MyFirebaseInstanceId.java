package in.prepskool.prepskoolacademy.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by gourav on 12/25/2018.
 */

public class MyFirebaseInstanceId extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        Log.d("Not","Token ["+tkn+"]");
    }

}
/*
*   Implement this to the chat fragment...................................
*
*
*   This method should be called in oncreate()...............
*
*       updateToken(FirebaseInstanceId.getInstance().getToken());
*
*
*
*
*   private void updateToken(String token){
*             DatabaseReference reference = FirebaseDataBase.getInstance().getReference("Token");
*             Token token1 = new Token(token);
*               reference.child(user.getUid()).setValue(token1);
*   }
*
*   */