package common.base.netAbout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import org.json.JSONException;
import org.json.JSONObject;
import common.base.utils.CommonLog;
import common.base.utils.Util;

/**
 * ******************(^_^)***********************<br>
 * User: fee(QQ/WeiXin:1176610771)<br>
 * Date: 2018/8/28<br>
 * Time: 9:59<br>
 * <P>DESC:
 * 基于Okgo网络请求框架的通用请求接口
 * </p>
 * ******************(^_^)***********************
 */
public class BaseOkgoApi extends BaseApi {

    static final byte GET = 1;
    static final byte POST = GET + 1;
    static final byte PUT = POST + 1;

    /**
     * 不带请求参数的get请求
     * @see #getWithParams(String, AbsCallback, String[], String...)
     * @param apiUrl api url地址
     * @param callback 网络响应回调
     */
    public static void get(String apiUrl,AbsCallback callback) {
//        excute(createRequest(wholeUrl,GET),callback);
        getWithParams(apiUrl, callback, null, "");
    }

    public static void getWithParams(String wholeApiUrl, AbsCallback callback, String[] appendKeys, String... keyValues) {
        GetRequest getRequest = OkGo.get(wholeApiUrl);
        buildCommonRequestParams(getRequest, appendKeys, keyValues);
        excute(getRequest, callback);
    }
    /**
     * @param request
     * @param appendKeys keys
     * @param values values 要一一对应到key的值
     * @return request请求自身
     */
    public static BaseRequest buildCommonRequestParams(BaseRequest request, String[] appendKeys, String... values) {
        if (appendKeys != null && values != null) {//如果有外部传入的参数
            int keysLen = appendKeys.length;
            int valuesLen = values.length;
            if (keysLen > 0 && valuesLen > 0 && valuesLen >= keysLen) {
                for (int keyIndex = 0; keyIndex < keysLen; keyIndex++) {
                    String curKey = appendKeys[keyIndex];
                    String curValue = values[keyIndex];
                    request.params(curKey, curValue);
                }
            }
        }
        return request;
    }
    public static BaseRequest getRequest(String wholeUrl) {
        return createRequest(wholeUrl, GET);
    }

    /**
     * 不带请求参数指定请求类型值的get请求
     * @param apiUrl 可以是全路径，也可以是部分路径
     * @param callback 网络响应回调
     * @param requestType 请求类型值
     */
    public static void getWithType(String apiUrl, AbsCallback callback, int requestType) {
        if (!Util.isEmpty(URL_HOST)) {
            if (!Util.isEmpty(apiUrl)) {
                if (!apiUrl.startsWith(URL_HOST)) {
                    apiUrl = URL_HOST + apiUrl;
                }
            }
        }
        callback.requestType = requestType;
        get(apiUrl,callback);
    }

    /**
     * 带有请求参数/值，键值对的post请求
     * @param wholeApiUrl 完整的api url地址
     * @param callback 回调
     * @param keysInParams 参数键
     * @param keysValues 参数值
     */
    public static void postWithParams(String wholeApiUrl, AbsCallback callback, String[] keysInParams, String... keysValues) {
        PostRequest postRequest = postRequest(wholeApiUrl);
        buildCommonRequestParams(postRequest, keysInParams, keysValues);
        excute(postRequest, callback);
    }
    public static void post(String wholeUrl, AbsCallback callback) {
        excute(createRequest(wholeUrl,POST),callback);
    }
    public static PostRequest postRequest(String wholeUrl) {
        return (PostRequest) createRequest(wholeUrl, POST);
    }
    public static void postWithType(String apiUrl, AbsCallback callback, int requestType) {
        if (!Util.isEmpty(URL_HOST)) {//表示URL_HOST已赋值
            if (!Util.isEmpty(apiUrl)) {
                if (!apiUrl.startsWith(URL_HOST)) {
                    apiUrl = URL_HOST + apiUrl;
                }
            }
        }
        callback.requestType = requestType;
        post(apiUrl, callback);
    }
    protected static BaseRequest createRequest(String wholeUrl, byte requestMethod) {
        switch (requestMethod) {
            case GET:
                return OkGo.get(wholeUrl);
            case POST:
                return OkGo.post(wholeUrl);
            case PUT:
                return OkGo.put(wholeUrl);
        }
        return null;
    }
    public static void excute(BaseRequest request, AbsCallback callback) {
        request.tag(request.getUrl()).execute(callback);
    }

    /**
     * 根据String类型的keys和String类型的values创建JSONObject
     * @param keys
     * @param values
     * @return JSONObject
     */
    public static JSONObject createJsonWithParams(String[] keys, String... values) {
        JSONObject jsonObj = new JSONObject();
        if (keys != null && values != null && values.length >= keys.length) {
            int keyLen = keys.length;
            for(int i = 0; i < keyLen;i++) {
                String curKey = keys[i];
                String curV = values[i];
                try {
                    jsonObj.put(curKey,curV);
                } catch (JSONException e) {
                    CommonLog.e("info", "AppBaseAPi-->createJsonWithParams() put the key :" + curKey + " the value: " + curV + " occur :" + e);
                }
            }
        }
        return jsonObj;
    }

    /**
     * 根据键、值对，生成JSONObject
     * @param keys
     * @param values 值可为任意数据类型
     * @return 如果没有keys也会有JSONObject生成
     */
    public static JSONObject createJsonWithParams(String[] keys, Object... values) {
        JSONObject jsonObj = new JSONObject();
        if (keys != null && values != null && values.length >= keys.length) {
            int keyLen = keys.length;
            for(int i = 0; i < keyLen;i++) {
                String curKey = keys[i];
                Object curV = values[i];
                try {
                    jsonObj.put(curKey,curV);
                } catch (JSONException e) {
                    CommonLog.e("info", "AppBaseAPi-->createJsonWithParams() put the key :" + curKey + " the value: " + curV + " occur :" + e);
                }
            }
        }
        return jsonObj;
    }

    /**
     * 仅可指定Param JSon数据体的键、值 的post请求
     * @param apiUrl 可以是全路径也可以是部分路径
     * @param callback 回调
     * @param keysInParmJsonObject JSOn请求体的参数 ：键
     * @param valuesInParamJsonObject JSOn请求体的参数 ：键对应的值
     */
    protected static void jsonPost(String apiUrl, AbsCallback callback, String[] keysInParmJsonObject, Object... valuesInParamJsonObject) {
        if (!Util.isEmpty(URL_HOST)) {
            if (!Util.isEmpty(apiUrl)) {
                if (!apiUrl.startsWith(URL_HOST)) {
                    apiUrl = URL_HOST + apiUrl;
                }
            }
        }
        JSONObject jsonBody = createJsonWithParams(keysInParmJsonObject, valuesInParamJsonObject);
        postRequest(apiUrl)
                .upJson(jsonBody)
                .execute(callback);
    }

}
