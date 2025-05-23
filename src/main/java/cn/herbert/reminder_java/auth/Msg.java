package cn.herbert.reminder_java.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Msg {
    int code;   //错误码
    String Message; //消息提示
    Map<String,Object> data=new HashMap<String,Object>();   //数据

    //无权访问
    public static Msg denyAccess(String message){
        Msg result=new Msg();
        result.setCode(300);
        result.setMessage(message);
        return result;
    }

    //操作成功
    public static Msg success(String message){
        Msg result=new Msg();
        result.setCode(200);
        result.setMessage(message);
        return result;
    }

    //客户端操作失败
    public static Msg fail(String message){
        Msg result=new Msg();
        result.setCode(400);
        result.setMessage(message);
        return result;
    }
    public static Msg wrongUser(){
        Msg result=new Msg();
        result.setCode(402);
        result.setMessage("账号密码错误");
        return result;
    }
    public static Msg wrongIdCode(){
        Msg result=new Msg();
        result.setCode(410);
        result.setMessage("身份证格式错误");
        return result;
    }
    public static Msg existPhoneNumber(){
        Msg result=new Msg();
        result.setCode(411);
        result.setMessage("电话已被注册");
        return result;
    }
    public static Msg expired(){
        Msg result=new Msg();
        result.setCode(401);
        result.setMessage("身份过期");
        return result;
    }

    public Msg add(String key,Object value){
        this.data.put(key,value);
        return this;
    }
}
