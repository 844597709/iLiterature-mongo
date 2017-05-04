package com.swust.kelab.service.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service(value = "systemService")
public class SystemService {
    // 服务器信息
    public HashMap<String, Object> viewServerInfo() throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            int kb = 1024 * 1024;
            // 可使用内存
            long totalMemory = Runtime.getRuntime().totalMemory() / kb;
            // 剩余内存
            long freeMemory = Runtime.getRuntime().freeMemory() / kb;
            // 最大可使用内存
            long maxMemory = Runtime.getRuntime().maxMemory() / kb;
            // 操作系统
            String osName = System.getProperty("os.name");
            // 用户当前目录
            String userHome = System.getProperty("user.home");
            // JDK版本
            String jkdVersion = System.getProperty("java.specification.version");
            // JDK路径
            String jkdPath = System.getProperty("java.home");
            // 当前程序主目录
            String appDir = System.getProperty("user.dir");
            // 操作系统类型
            String osType = System.getProperty("os.arch");
            // 操作系统内部版本号
            String osVersion = System.getProperty("os.version");
            // 服务器IP
            InetAddress myIPaddress = null;
            try {
                myIPaddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String serIPAddr = myIPaddress.getHostAddress();
            map.put("result", "1");
            map.put("totalmemory", totalMemory);
            map.put("freememory", freeMemory);
            map.put("maxmemory", maxMemory);
            map.put("osname", osName);
            map.put("userhome", userHome);
            map.put("jdkversion", jkdVersion);
            map.put("jdkpath", jkdPath);
            map.put("ostype", osType);
            map.put("osversion", osVersion);
            map.put("servipaddr", serIPAddr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 服务器状态
    public HashMap<String, String> viewServerStatus() throws Exception {
        HashMap<String, String> smap = new HashMap<String, String>();
        // 操作系统
        String osName = System.getProperty("os.name");
        if (osName.indexOf("Windows") == -1) {
            try {
                smap.put("result", "1");
                smap.put("servstatus", viewServInfoLinux());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            smap.put("result", "1");
            smap.put("servstatus", "该操作系统不支持！");
        }
        return smap;
    }

    // 读取Linux服务器情况
    public String viewServInfoLinux() throws Exception {
        String str = "";
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("top -b -n 1");// 调用系统的“top"命令
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                // 取前五行
                int line;
                for (line = 0; line < 5; line++) {
                    str = str + in.readLine() + "<br />";
                }
                str = str + "PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND<br />";
                // 取java进程
                boolean flag = true;
                while (flag == true) {
                    String tmp = null;
                    tmp = in.readLine();
                    if (tmp == null || tmp.indexOf("java") != -1) {
                        str = str + tmp;
                        flag = false;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
