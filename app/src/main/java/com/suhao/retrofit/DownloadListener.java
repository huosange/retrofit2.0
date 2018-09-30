package com.suhao.retrofit;

public interface DownloadListener {

    void start();

    void onProgress(double currentLength);

    void onFinish(String localPath);

    void onFailure();
}
