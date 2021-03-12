package com.fiszki.plastyka;

public interface IDownloadingEvent
{
    void onDownloadingAction(String fileName, float progress);
}
