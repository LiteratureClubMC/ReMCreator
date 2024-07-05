#!/bin/bash

# 设置CLASSPATH，使用冒号分隔不同的路径
export CLASSPATH="./lib/mcreator.jar:./lib/*"

# 启动Java程序，并且指定--add-opens选项
java net.mcreator.Launcher
