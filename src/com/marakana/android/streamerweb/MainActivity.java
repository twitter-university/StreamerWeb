package com.marakana.android.streamerweb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
  private WebView webView;

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Find the WebView
    webView = (WebView) findViewById(R.id.web_view);

    // Add JavaScript-Android binding
    webView.addJavascriptInterface(new StreamerJavaScriptInterface(this),
        "Android");

    // Add our custom WebViewClient
    webView.setWebViewClient(new StreamerClient());

    // Enable JavaScript
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);

    // Load the page
    webView.loadUrl("file:///android_asset/index.html");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // Was the key pressed a Back button, and is there anything to go back to?
    if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
      webView.goBack();
      return true;
    }

    // Return default behavior, which may be existing the activity
    return super.onKeyDown(keyCode, event);
  }

  /** Interface to bind JavaScript to Android code. */
  class StreamerJavaScriptInterface {
    private Context context;

    public StreamerJavaScriptInterface(Context context) {
      this.context = context;
    }

    public void showAbout() {
      Toast.makeText(context,
          "Marakana Streamer shows latest tech news from marakana.com",
          Toast.LENGTH_LONG).show();
    }
  }

  /** Custom WebViewClient. */
  class StreamerClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      // If it's one of our pages, load it within our app
      if (Uri.parse(url).getHost().equals("marakana.com")) {
        return false;
      }

      // Otherwise, use the default browser to handle this URL
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      startActivity(intent);
      return true;
    }

  }
}