package com.liantong.demo_part2.Utils;


import org.springframework.stereotype.Component;

/**
 * 
 * @author 
 *
 */
@Component
public class Result<T> {

	private Boolean success;//是否成功
	private T data;//数据对象
	private String msg;//处理结果信息
	private String code;//结果码10000为正确，其他为错误

	
	/**
	 * 无参构造器
	 */
	public Result(){
		super();
	}
	
	/**
	 * 只返回状态，状态码，消息
	 * @param

	 */

	public Result(Boolean success,String msg){
		super();
		this.success=success;
		this.msg=msg;
	}
	
	/**
	 * 只返回状态，状态码，数据对象
	 * @param
	 * @param
	 */
	public Result(Boolean success,  T result){
		super();
		this.success=success;
		this.data=result;
	}

	public Result(Boolean success, T data, String msg, String code) {
		this.success = success;
		this.data = data;
		this.msg = msg;
		this.code = code;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void packetResult(Boolean success,String msg,T data){
		setSuccess(success);
		setData(data);
		setMsg(msg);
		setCode("10000");
	}
	public void packetResult(Boolean success,String msg,T data,String code){
		setSuccess(success);
		setData(data);
		setMsg(msg);
		setCode(code);
	}
}
