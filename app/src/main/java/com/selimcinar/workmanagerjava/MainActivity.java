package com.selimcinar.workmanagerjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Data : gelecek olan veri
        Data data = new Data.Builder().putInt("intKey",1).build();

        //Constraints: durumlar ağ bağlı ise şarz oluyor ise.
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build();
/*
        //One time work : tek sefer çalışacak iş
        //WorkRequest arka planda çalışacak işlem oluşturuldu.
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setConstraints(constraints)
                .setInputData(data)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .addTag("myTag")
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);*/



        //Periodic work request : periyodik çalıştırma arka planda , 15 dakika da bir çalışır 1 dakika yazsanda
        WorkRequest workRequest = new PeriodicWorkRequest.Builder(RefreshDatabase.class,15,TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build();
        WorkManager.getInstance(this).enqueue(workRequest);


        //oluşan işi gözlemleme ve durumlara göre bilgilendirme OBSERVE : GÖZLEMLE
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(this,new Observer<WorkInfo>()
        {

            @Override
            public void onChanged(WorkInfo workInfo) {
                //çalışıyorsa bu yazar
                if (workInfo.getState() == WorkInfo.State.RUNNING){
                    System.out.println("running");
                }
                else if (workInfo.getState()== WorkInfo.State.SUCCEEDED) {
                    System.out.println("succeded");
                }
                //çalışmıyorsa bu 
                else if (workInfo.getState() == WorkInfo.State.FAILED){
                    System.out.println("failed");
                }
            }
        });

        // cancelAllWork : tüm işlemleri iptal et , tage göre iptal , Id idye göre iptal
      //  WorkManager.getInstance(this).cancelAllWork();

        /*
        //Chaining : zincirleme bağlama birbirine bağlı bir şekilde bir defa çalışacak işleri bağlama.
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        // begin with : işleme başla .then sonra bu işlemi yap .then diğer işlemi yap
        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .enqueue();
*/
    }


}