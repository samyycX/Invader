package com.samyyc.invader.name;


import com.samyyc.invader.util.C;
import com.samyyc.invader.util.TextUtil;

public enum T {

    COMMAND_GAMEMODE_ALIAS(C.AQUA+"/gamemode <creative/survival/adventure/spectator> #切换你的游戏模式"),
    COMMAND_GAMEMODE_HINT(C.GREEN+"您已成功切换至{{mode}}模式!"),
    COMMAND_GAMEMODE_ERROR(C.RED+"未知的模式: {{mode}}"),

    LOGGER_COMMAND_GAMEMODE_CHANGE("{{player}}成功切换到了{{mode}}模式"),

    COMMAND_ERROR_NO_EXECUTOR(C.RED+"指令出现内部错误(未设置执行器)"),
    COMMAND_ERROR_NO_PERMISSION(C.RED+"您没有足够的权限使用该指令"),
    COMMAND_ERROR_NOT_SUPPORT_CONSOLE(C.RED+"此命令只能给玩家使用")

    ;
    private final String text;

    T(String text) {
        TextUtil.color(text);
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
