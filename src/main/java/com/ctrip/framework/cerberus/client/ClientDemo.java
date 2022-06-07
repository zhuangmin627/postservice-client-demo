package com.ctrip.framework.cerberus.client;

import com.ctrip.framework.cerberus.client.app.AppTokenManager;
import com.ctrip.framework.cerberus.client.app.CerberusApp;
import com.ctrip.framework.cerberus.client.http.HttpMethod;
import com.ctrip.framework.cerberus.client.http.HttpRequest;
import com.ctrip.soa.caravan.util.serializer.ssjson.SSJsonSerializer;
import com.ctrip.soa.flight.order.postbookingservice.v1.xorderdetailsearch.XOrderDetailRequest;
import com.ctrip.soa.flight.order.postbookingservice.v1.xorderdetailsearch.XOrderDetailResponse;
import com.ctrip.soa.flight.order.postbookingservice.v1.xorderdetailsearch.XOrderRequestInfo;
import com.google.common.collect.Lists;
import org.apache.http.entity.StringEntity;

import java.net.URL;
import java.nio.charset.Charset;

public class ClientDemo {
    public static void main(String[] args) throws Exception {
        /**
         * for PRO users:
         * 1. set env to EnvHelper.PRO
         * 2. set your own appKey "d7XXXXXX6a8" and your own appSecret "07d9XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX125"
         *    appKey and appSecret is given by Trip.com, Please contact us to take it.
         * 3. use url "https://apiproxy.ctrip.com/apiproxy/flight/postbookingservice/distributor/v1/XOrderDetail"
         *    here use the interface 'XOrderDetail' as example.
         *
         * for TEST users:
         * 1. set env to EnvHelper.FWS
         * 2. set your own appKey "d7XXXXXX6a8" and your own appSecret "07d9XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX125"
         *    appKey and appSecret is given by Trip.com, Please contact us to take it.
         * 3. use url "https://apiproxy-fws.ctripqa.com/apiproxy/flight/postbookingservice/distributor/v1/XOrderDetail"
         *    here use the interface 'XOrderDetail' as example.
         * **/
        String postUrl = "https://apiproxy-fws.ctripqa.com/apiproxy/flight/postbookingservice/distributor/v1/XOrderDetail";

        EnvHelper.setEnv(EnvHelper.FWS);
        AppTokenManager appTokenManager = new AppTokenManager();
        // Here the appKey and the appSecret is an example.
        CerberusApp app = new CerberusApp("a850dc98ed152f74", "secret:f08562073e984d818115d06eb49c076f1751af8e51bb4218005da6e213f35ede");
        appTokenManager.addApp(app);
        appTokenManager.start();

        // build request
        XOrderDetailRequest request = new XOrderDetailRequest();
        request.setXOrderRequestList(Lists.newArrayList(new XOrderRequestInfo() {{
            setOrderID(32442742349L);
        }}));

        String postRequestJson = SSJsonSerializer.DEFAULT.serialize(request);
        StringEntity requestEntity = new StringEntity(postRequestJson, Charset.forName("UTF-8"));

        String res = app.request(
                new HttpRequest.Buidler()
                        .method(HttpMethod.POST)
                        .url(new URL(postUrl))
                        .entity(requestEntity)
                        .build()
        );

        // deserialize
        XOrderDetailResponse responseEntity = SSJsonSerializer.DEFAULT.deserialize(res, XOrderDetailResponse.class);
        // Status.Code = 0 means 'Success', otherwise is fail.
        if (responseEntity != null && responseEntity.getStatus() != null && responseEntity.getStatus().getCode() == 0) {
            System.out.println("success!");
        }
    }
}
