package com.postman.net.service;





import com.base.net.control.HttpResult;
import com.postman.net.bean.AreaInput;
import com.postman.net.bean.AreaOutput;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AreaService {

    @POST("/getArea")
    Observable<HttpResult<AreaOutput>> queryArea(@Body AreaInput input);
}
