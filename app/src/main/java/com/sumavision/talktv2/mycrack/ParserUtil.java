package com.sumavision.talktv2.mycrack;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import crack.cracker.JarCracker;
import crack.listener.JarCrackCompleteListener;
import crack.util.CrackRequest;
import crack.util.HttpDownloader;

public class ParserUtil implements JarCrackCompleteListener {
	private static final String TAG = "ParserUtil";
	private CrackCompleteListener crackCompleteListener;
	private Context mctx;
	private long cracktime,startcracktime;
	private Handler timeouttesthd;
	private Runnable timeoutrd;
	public ParserUtil(Context context,
					  CrackCompleteListener crackCompleteListener, String url, int type) {
		mctx = context;
		this.crackCompleteListener = crackCompleteListener;
		if (url == null) {
			Log.e(TAG, "url is null");
			if (crackCompleteListener != null) {
				crackCompleteListener.onCrackFailed(null);
			}
			return;
		}
		String tmp = CrackRequest.getRequestString(url, type);
//		final String tmp1 = tmp;
//		if (url.contains("letvvod.tvfan.cn")) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					String result = new HttpDownloader().download(tmp1);
//					try {
//						JSONObject jsonObject = new JSONObject(result);
//						String standar = jsonObject.getString("standar");
//						String videoType = jsonObject.getString("videoType");
//						HashMap<String, String> map = new HashMap<String, String>();
//						map.put("standardDef", standar);
//						map.put("videoType", videoType);
//						onJarCrackComplete(map);
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
//			}).start();
//		} else {
			JarCracker.getInstance().crack(context.getApplicationContext(), url,
					tmp, type, this);
//		}
//		if(isComplete >0&& BasePlayerActivity.playType==2){
//			avoidCrackTimeout();
//		}
	}

	/**
	 * 这是避免破解超时的方法
	 */
	private void avoidCrackTimeout() {
//		cracktime = 20000l;
//		startcracktime = System.currentTimeMillis();
//		timeouttesthd = new Handler();
//		timeoutrd = new Runnable() {
//			@Override
//			public void run() {
//					try {
//						((BasePlayUI)mctx).removehandler();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					((BasePlayUI)mctx).showErrorDialog(-1004, false, 0, 0);
//					timeouttesthd.removeCallbacks(timeoutrd);
//				}
//		};
//		timeouttesthd.postDelayed(timeoutrd, cracktime);
	}
	@Override
	public void onJarCrackComplete(HashMap<String, String> map) {
		Log.i(TAG, "jar包破解结束,map:" + map);
		if(timeouttesthd!=null){
			timeouttesthd.removeCallbacks(timeoutrd);
		}
			handleResult(map);
	}

	@Override
	public void onCrackFailed(HashMap<String, String> arg0) {
		if (crackCompleteListener != null) {
			crackCompleteListener.onCrackFailed(arg0);
		}
	}

	@Override
	public void onJarDownLoading(int process) {
		if (crackCompleteListener != null) {
			crackCompleteListener.onJarDownLoading(process);
		}
	}

	// @Override
	// public void onServerCrackComplete(HashMap<String, String> map) {
	// Log.i(TAG, "服务器破解结束");
	// handleResult(map);
	// }

	private void handleResult(HashMap<String, String> map) {
		String standUrl = map.get("standardDef");

		/*
		 * if(standUrl!=null && standUrl.contains(".tvfan.cn")){
		 * standUrl=BasePlayerActivity.getMobilePlayUrl(standUrl); }
		 */
		String highUrl = map.get("hightDef");
		/*
		 * if(highUrl != null &&highUrl.contains(".tvfan.cn")){
		 * highUrl=BasePlayerActivity.getMobilePlayUrl(highUrl); }
		 */

		String superUrl = map.get("superDef");
		/*
		 * if(superUrl!=null &&superUrl.contains(".tvfan.cn")){
		 * superUrl=BasePlayerActivity.getMobilePlayUrl(superUrl); }
		 */
		String type = map.get("videoType");
		if (null == standUrl) {
			standUrl = "";
		}
		if (null == highUrl) {
			highUrl = "";
		}
		if (null == superUrl) {
			superUrl = "";
		}

		CrackResult result = new CrackResult();
		result.type = type;
		if (!TextUtils.isEmpty(standUrl)) {
			result.path = standUrl;
			result.standUrl = standUrl;
		}
		if (!TextUtils.isEmpty(highUrl)) {
			result.highUrl = highUrl;
			if (TextUtils.isEmpty(standUrl)) {
				result.path = highUrl;
			}
		}
		if (!TextUtils.isEmpty(superUrl)) {
			result.superUrl = superUrl;
			if (TextUtils.isEmpty(standUrl) && TextUtils.isEmpty(highUrl)) {
				result.path = superUrl;
			}
		}

		if (crackCompleteListener != null) {
			crackCompleteListener.onCrackComplete(result);
		}
	}

	public void stop() {
		crackCompleteListener = null;
	}

}
