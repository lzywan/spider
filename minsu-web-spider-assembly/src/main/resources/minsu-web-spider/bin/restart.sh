#!/bin/bash
#
# zy
# 20150516
# 重启服务脚本
source /etc/profile

export LANG=en_US.UTF-8
export LANG_ALL=en_US.UTF-8
item=`pwd | awk -F "/" '{print $4}' | tr -d '\r'`
tomcat_bin=/home/www/$item/bin

/usr/sbin/ntpdate ntp.ziroom.com
touch /tmp/8081_lock.txt
cd $tomcat_bin ;
echo "停止服务"
./spring-boot stop
for i in `seq 1 10`
do
echo -n .
if ! ps aux |grep $tomcat_bin |grep -vq grep ; then
  continue
fi
sleep 1
done
if ps aux |grep $item |grep -vq grep ; then
  ps aux |grep $item |grep -v grep  |awk '{print $2}'|xargs kill -9
fi
sleep 8

if [ "$1" == "stop" ] ; then
    exit
fi

#su - ziroom -c "cd $tomcat_bin ; sh spring-boot restart"
su -c "cd $tomcat_bin ; sh spring-boot restart"
echo "等待启动"
for  i in `seq 1 3`
do
sleep 5
tail -n 1 ../logs/stdout.log
done

echo "获取进程信息"
ps aux |grep $item|grep -v grep
if [ $? -gt 0 ] ; then
#   su - ziroom -c "cd $tomcat_bin ; sh spring-boot restart"
   su -c "cd $tomcat_bin ; sh spring-boot restart"
   sleep 20
fi

tail -n 300 ../logs/stdout.log
echo "获取日志"
echo  "查看端口号"
netstat -ntlp |grep `ps aux |grep java |grep $tomcat_bin|awk '{print $2}'`
echo "重启完成"
rm -fv /tmp/8081_lock.txt