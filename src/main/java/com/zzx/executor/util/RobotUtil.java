package com.zzx.executor.util;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * @Description
 * @Author Alon
 * @Date 2019/5/24 23:24
 */

@Component
public class RobotUtil {

    public static void keyClick(Robot robot, int key){
        robot.keyPress(key);
        robot.keyRelease(key);
    }



    public static void mouseClick(Robot robot){
        //点击鼠标
        //鼠标左键
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        //鼠标右键
//        core.mousePress(InputEvent.BUTTON3_MASK);
//        core.mouseRelease(InputEvent.BUTTON3_MASK);
    }
}
