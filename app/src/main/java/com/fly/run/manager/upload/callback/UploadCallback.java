package com.fly.run.manager.upload.callback;

public interface UploadCallback {

	/**
	 * 开始上传
	 */
	public void start();

	/**
	 * 上传进度－百分比
	 * 
	 * @param progress
	 */
	public void progress(int progress);

	/**
	 * 上传结束
	 */
	public void end();

	/**
	 * 上传完成（返回状态）
	 * 
	 * @param result
	 */
	public void success(String result);

	/**
	 * 上床失败
	 */
	public void error();
}
