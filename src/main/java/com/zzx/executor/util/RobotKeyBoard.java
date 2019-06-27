package com.zzx.executor.util;



import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;

@Component
public class RobotKeyBoard {

    private Robot robot;

    public boolean keyBoardInput(String input) {

        if (!inputCheck(input)) {
            return false;
        }
        int[] inputKey = stringToIntArray(input);

        for (int in : inputKey) {
            int temp = in;
            if (temp >= 0x5B) {
                in = in - 26;
            } else {
                RobotUtil.keyClick(robot, KeyEvent.VK_CAPS_LOCK);
            }
            RobotUtil.keyClick(robot, in);

            if (temp < 0x5B) {
                RobotUtil.keyClick(robot, KeyEvent.VK_CAPS_LOCK);
            }
        }

        return true;
    }


    private boolean inputCheck(String input) {
        if (input == null || input.trim().equals("")) {
            return false;
        }
        char[] in = input.toCharArray();

        for (char ch : in) {
            if (ch < 48 || (ch < 65 && ch > 57) || (ch < 97 && ch > 90) || ch > 123) {
                return false;
            }
        }

        return true;
    }

    private int[] stringToIntArray(String input) {
        char[] in = input.toCharArray();
        int[] out = new int[in.length];

        for (int i = 0; i < in.length; i++) {
            if (in[i] >= 48 && in[i] <= 65) {
                out[i] = 0x30 + in[i] - 48;
            }

            if (in[i] >= 65 && in[i] <= 90) {
                out[i] = 0x41 + in[i] - 65;
            }
            if (in[i] >= 97 && in[i] <= 123) {
                out[i] = 0x5B + in[i] - 97;
            }
        }
        return out;
    }


    public void setRobot(Robot robot) {
        this.robot = robot;
    }



}
