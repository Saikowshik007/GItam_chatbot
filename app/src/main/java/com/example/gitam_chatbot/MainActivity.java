package com.example.gitam_chatbot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener {
    AIService aiService;
    AIRequest aiRequest;
    AIDataService aiDataService;
    EditText et;
    Vibrator vibe;
    String a;
    String b;
    private List<UserMessage> list = new ArrayList<>();
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessageRecycler =findViewById(R.id.reyclerview_message_list);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageAdapter = new MessageListAdapter(this,list);
        mMessageRecycler.setAdapter(mMessageAdapter);
        ((LinearLayoutManager)mMessageRecycler.getLayoutManager()).setStackFromEnd(true);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }


        final AIConfiguration config = new AIConfiguration("4c4992cdbd524358baa4f3adcb0213a0",
                AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        aiRequest = new AIRequest();
        aiDataService = new AIDataService(config);
        et=findViewById(R.id.editText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Intent i =new Intent(MainActivity.this,About.class);
        startActivity(i);
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton2:
                vibe.vibrate(20);
                aiService.startListening();
                break;
            case R.id.send:
                vibe.vibrate(20);
                text();
                break;

        }
    }


    protected void makeRequest () {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, String permissions[],
                                             int[] grantResults){
        switch (requestCode) {
            case 101: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {


                } else {

                }
                return;
            }
        }
    }

    @Override
    public void onResult ( final AIResponse response){
        Result result = response.getResult();
        a=result.getResolvedQuery();
        list.add(new UserMessage(a,true));
        b=result.getFulfillment().getSpeech();
        list.add(new UserMessage(true,b));
        mMessageAdapter.notifyDataSetChanged();
        mMessageRecycler.scrollToPosition(list.size()-1);
        vibe.vibrate(20);
    }


    @Override
    public void onError (AIError error){
        list.add(new UserMessage(true,"No Internet"));
        mMessageAdapter.notifyDataSetChanged();
        mMessageRecycler.scrollToPosition(list.size()-1);
        vibe.vibrate(20);
        vibe.vibrate(60);
        vibe.vibrate(40);
    }

    @Override
    public void onAudioLevel ( float level){

    }

    @Override
    public void onListeningStarted () {

    }

    @Override
    public void onListeningCanceled () {

    }

    @Override
    public void onListeningFinished () {

    }
    public void text(){
        aiRequest.setQuery(et.getText().toString());
        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    Result result = aiResponse.getResult();
                    a=result.getResolvedQuery();
                    list.add(new UserMessage(a,true));
                    b=result.getFulfillment().getSpeech();
                    list.add(new UserMessage(true,b));
                    et.setText("");
                    mMessageAdapter.notifyDataSetChanged();
                    mMessageRecycler.scrollToPosition(list.size()-1);
                    vibe.vibrate(20);
                }
                else{vibe.vibrate(60);
                    list.add(new UserMessage(true,"No Internet"));
                    mMessageAdapter.notifyDataSetChanged();
                    mMessageRecycler.scrollToPosition(list.size()-1);
                }
            }
        }.execute(aiRequest);
    }


}

