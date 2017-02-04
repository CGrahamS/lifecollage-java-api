#!/bin/bash
#RUN AS ROOT!
if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

yum update -y
yum install -y mysql56-server

mysql_secure_installation

#start mysql Server
service mysqld start

#start the atutomated installation
echo -n "Enter a password for the localhost MySQL root user > "
read -s DATABASE_PASS
mysqladmin -u root password "$DATABASE_PASS"
mysql -u root -p"$DATABASE_PASS" -e "UPDATE mysql.user SET Password=PASSWORD('$DATABASE_PASS') WHERE User='root'"
mysql -u root -p"$DATABASE_PASS" -e "DELETE FROM mysql.user WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1')"
mysql -u root -p"$DATABASE_PASS" -e "DELETE FROM mysql.user WHERE User=''"
mysql -u root -p"$DATABASE_PASS" -e "DELETE FROM mysql.db WHERE Db='test' OR Db='test\_%'"
mysql -u root -p"$DATABASE_PASS" -e "FLUSH PRIVILEGES"

#create an admin user that can be used outside localhost
echo -n "Enter a password for MySQL admin user 'caleb' > "
read -s password
mysql -u root -p"$DATABASE_PASS" -e "CREATE USER 'caleb'@'%' IDENTIFIED BY '$password';"
mysql -u root -p"$DATABASE_PASS" -e "GRANT ALL PRIVILEGES ON *.* TO 'caleb'@'%' WITH GRANT OPTION;"



#restart the mysql server
service mysqld stop

#configure server to auto start
chkconfig mysqld on

#restart the ec2 instance
reboot
