package com.zzx.executor.util;

import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class RobotApplication {
    private static RobotKeyBoard robbotKeyBoard;

    static {
        robbotKeyBoard = new RobotKeyBoard();
        Robot robot = null;
        try {
            robot = new Robot();
            robbotKeyBoard.setRobot(robot);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static boolean input(String inout){
        return robbotKeyBoard.keyBoardInput(inout);
    }




}
