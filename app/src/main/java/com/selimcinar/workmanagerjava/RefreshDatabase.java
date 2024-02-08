package com.selimcinar.workmanagerjava;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RefreshDatabase extends Worker {
Context myContext;
    public RefreshDatabase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.myContext = context; //workmanagerda context alındı
    }

    @NonNull
    @Override
    public Result doWork() {
    Data data = getInputData();   //Veri aldın dışardan
    int myNumber = data.getInt("intKey",0);  //veriyi değişkene ekledin
         refreshDatabase(myNumber);   //veriyi database adlı metoda ekledin.
        return Result.success();  //Başarılı ise çalıştırdın işlemi
    }
    private  void refreshDatabase(int myNumber){
        //geçici belleğe metotdan gelen değişken değeri ekledin ve consolda yazdırdın.
        SharedPreferences sharedPreferences =myContext.getSharedPreferences("com.selimcinar.workmanagerjava",Context.MODE_PRIVATE);
        int mySavedNumber = sharedPreferences.getInt("myNumber",0);
        mySavedNumber = mySavedNumber +myNumber;
        System.out.println(mySavedNumber);
        sharedPreferences.edit().putInt("myNumber",mySavedNumber).apply();
    }

}
