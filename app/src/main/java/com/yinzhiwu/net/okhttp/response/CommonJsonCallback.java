package com.yinzhiwu.net.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.yinzhiwu.net.okhttp.exception.OkHttpException;
import com.yinzhiwu.net.okhttp.listener.DisposeDataHandle;
import com.yinzhiwu.net.okhttp.listener.DisposeDataListener;
import com.yinzhiwu.net.okhttp.listener.DisposeHandleCookieListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * @author vision
 * @function 专门处理JSON的回调
 */
public class CommonJsonCallback implements Callback {

	/**
	 * the logic layer exception, may alter in different app
	 */
	protected final String RESULT_CODE = "ecode"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
	protected final int RESULT_CODE_VALUE = 0;
	protected final String ERROR_MSG = "emsg";
	protected final String EMPTY_MSG = "";
	protected final String COOKIE_STORE = "Set-Cookie"; // decide the server it
														// can has the value of
														// set-cookie2

	/**
	 * Yiwu Json Formate;
	 */
	protected final String RETURN_CODE_INT 		 = "returnCode";
	protected final String RETURN_SECURE_BOOL	 = "secure";
	protected final String RETURN_MESSAGE_STR 	 = "msg";
	protected final String RETURN_DATA_JSON 	 = "data";
	protected final String RETURN_RESULT_BOOL 	 = "result";

	/**
	 * the java layer exception, do not same to the logic error
	 */
	protected final int NETWORK_ERROR = -1; // the network relative error
	protected final int JSON_ERROR = -2; // the JSON relative error
	protected final int OTHER_ERROR = -3; // the unknow error

	/**
	 * 将其它线程的数据转发到UI线程
	 */
	private Handler mDeliveryHandler;
	private DisposeDataListener mListener;
	private Class<?> mClass;
	private Gson mGson;

	public CommonJsonCallback(DisposeDataHandle handle) {
		this.mListener = handle.mListener;
		this.mClass = handle.mClass;
		this.mDeliveryHandler = new Handler(Looper.getMainLooper());
		this.mGson = new GsonBuilder().create();
	}

	@Override
	public void onFailure(final Call call, final IOException ioexception) {
		/**
		 * 此时还在非UI线程，因此要转发
		 */
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));
			}
		});
	}

	@Override
	public void onResponse(final Call call, final Response response) throws IOException {
		final String result = response.body().string();
		final ArrayList<String> cookieLists = handleCookie(response.headers());
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				handleResponse(result);
				/**
				 * handle the cookie
				 */
				if (mListener instanceof DisposeHandleCookieListener) {
					((DisposeHandleCookieListener) mListener).onCookie(cookieLists);
				}
			}
		});
	}

	private ArrayList<String> handleCookie(Headers headers) {
		ArrayList<String> tempList = new ArrayList<String>();
		for (int i = 0; i < headers.size(); i++) {
			if (headers.name(i).equalsIgnoreCase(COOKIE_STORE)) {
				tempList.add(headers.value(i));
			}
		}
		return tempList;
	}

	private void handleResponse(Object responseObj) {
		if (responseObj == null) {
			mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
			return;
		}

		try {
			JsonObject result =(JsonObject) mGson.toJsonTree(responseObj.toString());
			if (result.has(RETURN_CODE_INT)) {
				if (result.get(RETURN_CODE_INT).getAsInt() == 200) {
					if (mClass == null) {
						mListener.onSuccess(result);
					} else {
						JsonElement data = result.get("RETURN_DATA_JSON");
						if (data instanceof JsonNull) {
							mListener.onFailure(new OkHttpException(JSON_ERROR,
									result.get(RETURN_MESSAGE_STR).getAsString()));
						} else if (data instanceof JsonArray) {
							List<?> list = mGson.fromJson(data)
							mListener.onSuccess(mGson);
						} else {
							Object obj = mGson.fromJson(data, mClass);
							if (obj != null) {
								mListener.onSuccess(obj);
							} else {
								mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
							}
						}
					}
				} else {
					if (result.has(ERROR_MSG)) {
						mListener.onFailure(
								new OkHttpException(result.optInt(RESULT_CODE), result.optString(ERROR_MSG)));
					} else {
						mListener.onFailure(new OkHttpException(result.optInt(RESULT_CODE), EMPTY_MSG));
					}
				}
			} else {
				if (result.has(ERROR_MSG)) {
					mListener.onFailure(new OkHttpException(OTHER_ERROR, result.optString(ERROR_MSG)));
				}
			}
		} catch (Exception e) {
			mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
			e.printStackTrace();
		}
	}
}