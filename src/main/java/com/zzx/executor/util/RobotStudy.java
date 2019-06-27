package com.zzx.executor.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zzx.executor.bo.PositionEntity;
import com.zzx.executor.dao.PhoneDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
@Slf4j
public class RobotStudy implements Runnable{
    private static final String GET_ACCESSTOKEN_URL = "https://mobile.yangkeduo.com/proxy/api/login?pdduid=0";
    private static int times = 0;
    private static int x=906;
    private static int y=153;
    private static int X=1150;
    private static int Y=210;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PhoneDao dao;

    private static ArrayList<PositionEntity> list=new ArrayList<PositionEntity>();
    List<String> lists = new ArrayList<>();

    public void study() throws InterruptedException {
        JOptionPane.showMessageDialog(null,"请访问网址：【https://mobile.yangkeduo.com/login.html】并进入登录界面");
        Thread.sleep(1000);
        JOptionPane.showMessageDialog(null,"接下来的10秒请将鼠标停留在输入框上【停留时请不要移动鼠标】！");
        Thread.sleep(2000);
        for (int i=0;i<30;i++){
            list.add(dorecord()) ;
            Thread.sleep(200);
        }

        JOptionPane.showMessageDialog(null,"接下来的10秒请将鼠标停留在点击注册按钮上【停留时请不要移动鼠标】！");
        Thread.sleep(2000);
        for (int i=0;i<30;i++){
            list.add(dorecord()) ;
            Thread.sleep(200);
        }
        for(int i=0;i<list.size();i++){
            System.out.println("结果"+list.get(i).toString());
        }
        algorithmX(list);
        algorithmY(list);
        JOptionPane.showMessageDialog(null,"录入完成！");
    }

    private PositionEntity dorecord(){
        PositionEntity positionEntity =new PositionEntity();
        PointerInfo pinfo = MouseInfo.getPointerInfo();
        Point p = pinfo.getLocation();
        int mx = (int)p.getX();
        int my = (int) p.getY();

        positionEntity.setX(mx);
        positionEntity.setY(my);
        return positionEntity;
    }

    private void algorithmX(ArrayList<PositionEntity> list){
        ArrayList<Integer> inputX=new ArrayList<Integer>();
        ArrayList<Integer> inputY=new ArrayList<Integer>();
        for(int i=0;i<30;i++){
            inputX.add(list.get(i).getX());
            inputY.add(list.get(i).getY());
        }
         x=dolist(inputX);
         y=dolist(inputY);
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx="+x);
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyy="+y);
    }

    private void algorithmY(ArrayList<PositionEntity> list){
        ArrayList<Integer> inputX=new ArrayList<Integer>();
        ArrayList<Integer> inputY=new ArrayList<Integer>();
        for(int i=31;i<60;i++){
            inputX.add(list.get(i).getX());
            inputY.add(list.get(i).getY());
        }
         X=dolist(inputX);
         Y=dolist(inputY);
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX="+X);
        System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYY="+Y);
    }


    private int dolist(ArrayList<Integer> list){

        Map<Integer,Integer> map=new HashMap<>();
        for(int i=0;i<list.size();i++){
            if(map.containsKey(list.get(i))){
               int j= map.get(list.get(i))+1;
               map.remove(list.get(i));
               map.put(list.get(i),j);
            }else {
                map.put(list.get(i),1);
            }
        }
        int v=0;
        int k=0;
        for (Integer key : map.keySet()) {
            if(map.get(key)>v){
                v=map.get(key);
                k=key;
            }
            System.out.println("key= "+ key + " and value= " + map.get(key));
        }

        return k;
    }



    @Lazy(value = false)
    @Scheduled(cron = "* */2 * * * ?")
    public void openAndLogin() throws InterruptedException, AWTException {
        if (times == 0) {
            System.out.println("进入定时任务");
            Thread.sleep(35000);
            times = 1;
        }

        lists = dao.getphone();
        int times_one = lists.size();
        if (times_one == 0) {
            log.info("没有失效的token");
            return ;
        }
        for (int i = 0; i < times_one; i++) {
            //TODO 在这个将token存数据库！
            Thread.sleep(20000);
            String jsonObject= String.valueOf(dorobbot(lists.get(i)).get("access_token"));
            System.out.println("Token参数："+jsonObject);
            if(jsonObject.equals("null")){
                return;
            }
            int succesfull=dao.updateuser(jsonObject,lists.get(i));
            if(succesfull==1){
                log.info("成功！token:{},手机号:{}",jsonObject,jsonObject,lists.get(i));
            }
        }

    }


    private JSONObject dorobbot(String phone) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        Thread.sleep(3000);


//移动鼠标
        if (x == 0 || y == 0|| X==0||Y==0) {
            System.out.println("没有收到xy");
            return null;
        }
        robot.mouseMove(x, y);
//        robot.mouseMove(906, 153);
        RobotUtil.mouseClick(robot);
        RobotUtil.keyClick(robot, KeyEvent.VK_HOME);
        for (int i = 0; i < 12; i++) {
            RobotUtil.keyClick(robot, KeyEvent.VK_DELETE);
        }
        RobotApplication.input(phone);
//        core.mouseMove(700, 150);
//        robot.mouseMove(1150, 210);
        robot.mouseMove(X, Y);
        RobotUtil.mouseClick(robot);
        Thread.sleep(30000);
        String code = dao.getcode(phone);
        if (code != null) {
            return getAccessToken(phone, code);
        }
        log.info("验证码为空！");
        return null;
    }

    /**
     * 获取token
     *
     * @param mobile
     * @param code
     * @return
     */
    public JSONObject getAccessToken(String mobile, String code) {
        log.info("手机号:{}，短信code：{}", mobile, code);
        JSONObject res = new JSONObject();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Gson gson = new Gson();
            Map<String, Object> params = new HashMap<>();
            params.put("app_id", 5);
            params.put("mobile", mobile);
            params.put("code", code);
            HttpEntity<Object> entity = new HttpEntity<>(gson.toJson(params), headers);
            String result = restTemplate.postForObject(GET_ACCESSTOKEN_URL, entity, String.class);
            res = JSONObject.parseObject(result);
        } catch (HttpClientErrorException e) {
            //捕捉HTTP异常
            res = JSONObject.parseObject(e.getResponseBodyAsString());
            log.info("失败结果：{}", e.getResponseBodyAsString());
        } catch (Exception e) {
            //捕捉所有异常
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void run() {
        try {
            study();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
