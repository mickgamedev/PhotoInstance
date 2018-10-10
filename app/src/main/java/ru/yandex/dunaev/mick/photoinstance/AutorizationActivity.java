package ru.yandex.dunaev.mick.photoinstance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AutorizationActivity extends AppCompatActivity {

    private String CLIENT_ID = "812c719707cb4c72bc1b260a13321cb4";
    private final String REDIRECT_URI = "https://yx812c719707cb4c72bc1b260a13321cb4.oauth.yandex.ru";

    public static int REQUEST_CODE = 1;
    public static String EXTRA_TOKEN_REQUEST = "EXTRA_TOKEN_REQUEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorization);

        WebView webView = (WebView)findViewById(R.id.auth_web_view);
        webView.loadUrl("https://oauth.yandex.ru/authorize?response_type=token&client_id=" + CLIENT_ID);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.v("Redirect","OK");
                if (url.startsWith(REDIRECT_URI)) {
                    requestAccessToken(url); // запросить постоянный access token вместо временного
                }

                return true;
            }

            private void requestAccessToken(String url) {
                Log.v("Redirect","requestAccessToken");
                if(url.lastIndexOf("error") == -1)
                {
                    String patern_token = "#access_token=";
                    //ошибки нету можно доставать токен
                    int i = url.lastIndexOf(patern_token);
                    if(i == -1){
                        //токен не найден
                        autorizationError();
                    }
                    i+=patern_token.length();
                    String token = url.substring(i, url.indexOf('&'));
                    Log.v("Token",token);
                    returnToken(token);

                }
                else {
                    //ошибка авторизации
                    autorizationError();
                }
            }

            private void autorizationError(){
                setResult(RESULT_CANCELED);
                finish();
            }

            private void returnToken(String token){
                Intent intent = new Intent();
                intent.putExtra(EXTRA_TOKEN_REQUEST, token);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
