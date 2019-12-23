package com.arijeet.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.arijeet.newsapp.Account.LoginActivity;
import com.arijeet.newsapp.Comments.CommentsActivity;
import com.arijeet.newsapp.models.Feed;
import com.arijeet.newsapp.models.entry.Entry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {
    ListView lvRss;
    ArrayList<String> titles;
    ArrayList<String> links;

    private static final String BASE_RRL="https://www.reddit.com/r/";
    private static final String send_RRL="https://www.reddit.com/";
    private static final String TAG="MainActivity";

    private Button btnRefreshFeed;
    private EditText editText;
    private String currentFeed;
    private ImageButton shareButton;
    private ImageButton saveButton;

    URLS urls = new URLS();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.navigation_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.navLogin:
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                break;




            default:
                Toast.makeText(getApplicationContext(),"Internal error occurred!!",Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRefreshFeed=(Button)findViewById(R.id.btnRefreshFeed);
        editText=(EditText)findViewById(R.id.etFeedName);
        shareButton=(ImageButton)findViewById(R.id.shareButton);
        saveButton=(ImageButton)findViewById(R.id.saveButton);
        //setupToolbar();
        Toolbar toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        currentFeed="earthporn";
        Toast.makeText(MainActivity.this,"You are viewing the popular subreddit",Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this,"Please search for different feed...",Toast.LENGTH_SHORT).show();
        init();

        btnRefreshFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedName=editText.getText().toString();
                if(!feedName.equals("")){
                    currentFeed=feedName;
                    Toast.makeText(MainActivity.this,"Your feed is :"+currentFeed,Toast.LENGTH_SHORT).show();
                    init();

                }
                else {init();}

            }
        });





    }





    private void init()
    {
        Retrofit retrofit=new Retrofit.Builder().baseUrl(BASE_RRL).addConverterFactory(SimpleXmlConverterFactory.create()).build();

        Api feedApi=retrofit.create(Api.class);

        Call<Feed> call=feedApi.getFeed(currentFeed);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {

                // Log.d(TAG,"onResponse : feed"+response.body().toString());
                Log.d(TAG,"onServerResponse :"+response.toString());

                try{
                List<Entry> entry=response.body().getEntry();

                 Log.d(TAG,"onServerResponse entry:"+response.body().getEntry());
                 Log.d(TAG,"onServerResponse author:"+entry.get(0).getAuthor());
                 Log.d(TAG,"onServerResponse updated:"+entry.get(0).getUpdated());
                 Log.d(TAG,"onServerResponse title:"+entry.get(0).getTitle());

                final ArrayList<post> posts=new ArrayList<post>();


                for(int i=0;i<entry.size();i++)
                {
                    ExtractXML extractXML1=new ExtractXML(entry.get(i).getContent(),"<a href=","NONE");
                    List<String> postContent=extractXML1.start();


                    ExtractXML extractXML2=new ExtractXML(entry.get(i).getContent(),"<img src=","NONE");
                    try {
                        postContent.add(extractXML2.start().get(0));


                    }catch (NullPointerException e){
                        postContent.add(null);
                        Log.e(TAG,"onFailure: NullpointerException(thumbail) :"+e.getMessage());
                    }
                    catch (IndexOutOfBoundsException e){
                        postContent.add(null);
                        Log.e(TAG,"onFailure: IndecOutOfBoundsException(thumbail) :"+e.getMessage());
                    }
                    int lastPosition=postContent.size()-1;
                    try{
                    posts.add(new post(
                            entry.get(i).getTitle(),
                            entry.get(i).getAuthor().getName(),
                            entry.get(i).getUpdated(),
                            postContent.get(0),
                            postContent.get(lastPosition)
                    ));}
                    catch (NullPointerException e){
                        posts.add(new post(
                                "none",
                                "none",
                                entry.get(i).getUpdated(),
                                "none",
                                postContent.get(lastPosition)
                        ));
                        Log.e(TAG,"onImageFailure: NullpointerException(thumbail) :"+e.getMessage());
                    }


                }


                 for(int j=0;j<posts.size();j++)
                 {
                 Log.d(TAG,"From Post we have : \n" +
                 "postUrl :"+posts.get(j).getPostURL()+"\n"+"ThumbnailUrl :"+posts.get(j).getThumbnailURL()
                 +"\n"+"Title :"+posts.get(j).getTitle()+"\n"
                 +"Author :"+posts.get(j).getAuthor()+"\n"
                 +"Updated :"+posts.get(j).getDate_updated()+"\n"
                 );
                 }

                ListView listView=(ListView)findViewById(R.id.listview);
                CustomListAdapter customListAdapter=new CustomListAdapter(MainActivity.this,R.layout.card_layout_main,posts);
                listView.setAdapter(customListAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemClick: Clicked: " + posts.get(position).toString());
                        Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
                        intent.putExtra("@string/post_url", posts.get(position).getPostURL());
                        intent.putExtra("@string/post_thumbnail", posts.get(position).getThumbnailURL());
                        intent.putExtra("@string/post_title", posts.get(position).getTitle());
                        intent.putExtra("@string/post_author", posts.get(position).getAuthor());
                        intent.putExtra("@string/post_updated", posts.get(position).getDate_updated());
                        startActivity(intent);
                    }
                });


            }catch(NullPointerException e){
                    editText.setText("");
                    Toast.makeText(MainActivity.this,currentFeed+"feed not present try another example news ",Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"onImageFailure: NullpointerException(thumbail) :"+e.getMessage());}
        }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

                Log.e(TAG,"onFailure: Unable to retrieve Rss :"+t.getMessage());
                Toast.makeText(MainActivity.this,"Internal Error Occured",Toast.LENGTH_SHORT).show();

            }
        });


    }




}
