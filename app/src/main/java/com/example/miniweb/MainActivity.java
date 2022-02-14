package com.example.miniweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SearchView searchview;
    ProgressBar progressbar;
    WebView webview;
    LinearLayout mainscreen;
    ImageView backward;
    ImageView forward;
    ImageView home;
    InputMethodManager inputMethodManager;
    String currenturl;
    private History_helper dbManager;
    public static final int REQUEST_OPEN_HIS = 0;
    static class TitleAndUrl {
        String title;
        String url;
    }
    private ArrayList<TitleAndUrl> closedTabs = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#111834"));
        actionBar.setBackgroundDrawable(colorDrawable);
        searchview=(SearchView)findViewById(R.id.searchview) ;
        progressbar=(ProgressBar) findViewById(R.id.progressbar);
        webview=(WebView) findViewById(R.id.webview);
        mainscreen=(LinearLayout)findViewById(R.id.mainscreen);
        backward=(ImageView)findViewById(R.id.backward);
        forward=(ImageView)findViewById(R.id.forward) ;
        home=(ImageView)findViewById(R.id.home) ;
        inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        dbManager=new History_helper(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAppCacheEnabled(false);
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressbar.setVisibility(View.VISIBLE);
                if (!(progressbar.isShown()))
                {
                    progressbar.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                }
                searchview.onActionViewExpanded();
                searchview.setQuery(webview.getUrl(),false);
                searchview.clearFocus();

            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                final String title=webview.getTitle();
                progressbar.setVisibility(View.GONE);

                if (progressbar.isShown())
                {
                    progressbar.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                }
                backward.setEnabled(true);
                backward.setImageResource(R.drawable.undo_orange);
                if(!(webview.canGoForward()))
                {
                    forward.setEnabled(false);
                    forward.setImageResource(R.drawable.redo_blue);
                }
                home.setEnabled(true);
                home.setImageResource(R.drawable.home_orange);
                currenturl=url;
                if (dbManager.checkUrl(url, History_helper.TABLE_NAME)) {
                    dbManager.deleteURL(url, History_helper.TABLE_NAME);
                    dbManager.addWebsite(new Website1(title,url, System.currentTimeMillis()));
                } else {
                    dbManager.addWebsite(new Website1(title,url, System.currentTimeMillis()));
                }


            }

        });
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("MainActivity","onQueryTextSubmit "+query);

                    Boolean bool = URLUtil.isValidUrl(query);
                    if (bool) {
                        webview.loadUrl(query);
                    }
                    else {
                        webview.loadUrl("https://www.google.com/search?q=" + query.replace("", "%20"));
                    }

                    backward.setEnabled(true);
                    backward.setImageResource(R.drawable.undo_orange);
                    home.setEnabled(true);
                    home.setImageResource(R.drawable.home_orange);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("MainActivity","onQueryTextChange "+newText);
                if (!TextUtils.isEmpty(newText))
                {
                    if (webview.getVisibility()!=View.VISIBLE) {
                        webview.clearHistory();
                        webview.setVisibility(View.VISIBLE);
                        mainscreen.setVisibility(View.GONE);
                    }
                }
                else
                {
                    webview.setVisibility(View.GONE);
                    mainscreen.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.super_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newtab:
                Intent intent=new Intent(MainActivity.this,NewTabActivity.class);
                startActivity(intent);
                break;
            case R.id.refresh:
                webview.reload();
                break;
            case R.id.history:
                Intent intent2 = new Intent();
                intent2.setClass(getApplicationContext(), HistoryActivity.class);
                startActivityForResult(intent2, REQUEST_OPEN_HIS);
                break;
                case R.id.addbookmark:
                Toast.makeText(MainActivity.this,"bookmark added",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bookmark:
                Toast.makeText(MainActivity.this,"view bookmark",Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Intent myintenet=new Intent(Intent.ACTION_SEND);
                myintenet.setType("text/plain");
                myintenet.putExtra(Intent.EXTRA_TEXT,currenturl);
                myintenet.putExtra(Intent.EXTRA_SUBJECT, "Copied url");
                startActivity(Intent.createChooser(myintenet,"Share"));


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case HistoryActivity.RESULT_HistoryActivity:
                String url = data.getStringExtra("historyUrl");
               webview.loadUrl(url);
                break;
//            case BookmarkActivity.RESULT_BookmarkActivity:
//                String bookmarkUrl = data.getStringExtra("bookmarkUrl");
//                aswm_view(bookmarkUrl,false, asw_error_counter, getCurrentWebView());
//                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void imageClicked(View view) {
        int id = view.getId();
        String ourId;
        ourId=view.getResources().getResourceEntryName(id);
        webview.loadUrl("https://www."+ourId+".com");
        webview.setVisibility(View.VISIBLE);
        mainscreen.setVisibility(View.GONE);
        searchview.onActionViewExpanded();
        searchview.setQuery(webview.getUrl(),false);
        searchview.clearFocus();
        backward.setEnabled(true);
        backward.setImageResource(R.drawable.undo_orange);
        home.setEnabled(true);
        home.setImageResource(R.drawable.home_orange);
        forward.setEnabled(false);
        forward.setImageResource(R.drawable.redo_blue);


        }
        public void iconClicked(View view) {
        switch (view.getId())
        {
            case R.id.home:
                homeDisplay();
               break;
            case R.id.backward:
                backwardDisplay();
                break;
            case R.id.forward:
                forwardDisplay();
                break;
        }

    }

    private void forwardDisplay()
    {
        if (webview.canGoForward())
        {
            webview.goForward();
            backward.setEnabled(true);
            backward.setImageResource(R.drawable.undo_orange);
            home.setEnabled(true);
            home.setImageResource(R.drawable.home_orange);
            searchview.onActionViewExpanded();
            searchview.setQuery(webview.getUrl(),false);
            searchview.clearFocus();
            if (!(webview.isShown()))
            {
                webview.setVisibility(View.VISIBLE);
                mainscreen.setVisibility(View.GONE);
            }

        }
        home.setEnabled(true);
        home.setImageResource(R.drawable.home_orange);
    }

    private void backwardDisplay() {
        if(webview.canGoBack())
        {
            webview.goBack();
            if (webview.canGoForward())
            {
                forward.setEnabled(true);
                forward.setImageResource(R.drawable.redo_orange);
            }
            if(webview.isShown())
            {
                webview.setVisibility(View.VISIBLE);
                mainscreen.setVisibility(View.GONE);
            }
        }
        else if (webview.isShown())
        {
            webview.setVisibility(View.GONE);
            mainscreen.setVisibility(View.VISIBLE);
            backward.setEnabled(false);
            backward.setImageResource(R.drawable.undo_blue);
            if (webview.canGoForward())
                forward.setEnabled(true);
            forward.setImageResource(R.drawable.redo_orange);


        }
    }

    private void homeDisplay() {
        if (webview.isShown())
        {
            webview.setVisibility(View.GONE);
            mainscreen.setVisibility(View.VISIBLE);
        }
        backward.setEnabled(false);
        forward.setEnabled(false);
        backward.setImageResource(R.drawable.undo_blue);
        forward.setImageResource(R.drawable.redo_blue);
        home.setEnabled(false);
        home.setImageResource(R.drawable.gome_blue);
        searchview.onActionViewExpanded();
        searchview.setQuery("",false);
        searchview.clearFocus();
        //inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack())
        {
              webview.goBack();
              return true;

        }
        else if (!(mainscreen.isShown()))
        {
            mainscreen.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
            searchview.setQuery("",false);
            searchview.clearFocus();
            backward.setEnabled(false);
            backward.setImageResource(R.drawable.undo_blue);
            if(webview.canGoForward())
            {
                forward.setEnabled(true);
                forward.setImageResource(R.drawable.redo_orange);
            }
        }
        return super.onKeyDown(keyCode, event);

    }
}