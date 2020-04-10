package com.github.hollykunge.security.simulation.biz;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DllInvoker extends Library {

    DllInvoker instance = (DllInvoker) Native.loadLibrary(
            "D:\\WorkSpace\\OpenSplice\\OSPL\\spliceForEngine.dll", DllInvoker.class);

    String dllSystemPrepare(String systemName, double startTime, double step, String users);

    String dllSystemStart(String systemName);

    String dllSystemPause(String systemName);

    String dllSystemStop(String systemName);

    String dllGetOnlineNodes(String systemName);
}
