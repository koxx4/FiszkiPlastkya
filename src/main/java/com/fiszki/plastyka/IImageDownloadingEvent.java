package com.fiszki.plastyka;

public interface IImageDownloadingEvent
{
    void onDownloadingAction(String fileName, float progress);
}
