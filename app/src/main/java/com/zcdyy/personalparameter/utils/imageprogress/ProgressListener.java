package com.zcdyy.personalparameter.utils.imageprogress;

public interface ProgressListener {

    void progress(long bytesRead, long contentLength, boolean done);

}
