#!/bin/bash
#https://realpython.com/blog/python/kickstarting-flask-on-ubuntu-setup-and-deployment/
#sudo adduser newuser

#Current Directory
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#Install the Requirements
sudo apt-get update
sudo apt-get install -y python python-pip python-virtualenv nginx gunicorn

#Set up Flask
#Then create and activate a virtualenv:
sudo mkdir /home/www && cd /home/www
unset LC_ALL
export LC_ALL=C
source /home/alumne/.bashrc
sudo virtualenv env
pip install virtualenv
source env/bin/activate
#Install the requirements:
sudo pip install Flask==0.10.1
#set up  project:
sudo mkdir flask_project && cd flask_project
sudo cp $DIR/app.py .
#sudo gedit app.py
#from flask import Flask, jsonify
#
#app = Flask(__name__)
#
#
#@app.route('/')
#def index():
#    return 'Flask is running!'
#
#
#@app.route('/data')
#def names():
#    data = {"names": ["John", "Jacob", "Julie", "Jennifer"]}
#    return jsonify(data)
#
#
#if __name__ == '__main__':
#    app.run()
#Set up a static directory-and then add an index.html 
sudo mkdir static
cp $DIR/index.html ./static/
#sudo gedit static/index.html
#<h1>Test!</h1>

#Configure nginx
#Start nginx:
sudo /etc/init.d/nginx start
sudo rm /etc/nginx/sites-enabled/default
#sudo touch /etc/nginx/sites-available/flask_project
sudo cp $DIR/flask_project /etc/nginx/sites-available/flask_project
sudo ln -s /etc/nginx/sites-available/flask_project /etc/nginx/sites-enabled/flask_project
#sudo gedit /etc/nginx/sites-enabled/flask_project
#server {
#    location / {
#        proxy_pass http://localhost:8000;
#        proxy_set_header Host $host;
#        proxy_set_header X-Real-IP $remote_addr;
#    }
#    location /static {
#        alias  /home/www/flask_project/static/;
#    }
#}
#Restart nginx:
sudo /etc/init.d/nginx restart

#runs gunicorn on localhost port 8000.
cd /home/www/flask_project/
gunicorn app:app -b localhost:8000
#Stop guinicorn
#sudo pkill gunicorn

#Configure Supervisor
sudo apt-get install -y supervisor
sudo chmod 644 $DIR/flask_project.conf
sudo chown root:root $DIR/flask_project.conf
sudo cp $DIR/flask_project.conf /etc/supervisor/conf.d/
sudo supervisord -c /etc/supervisor/supervisord.conf
sudo supervisorctl -c /etc/supervisor/supervisord.conf
sudo pkill gunicorn
sudo supervisorctl reread
sudo supervisorctl update
sudo supervisorctl start flask_project

#github
#sudo apt-get install -y git
#sudo mkdir /home/git && cd /home/git
#sudo mkdir flask_project.git && cd flask_project.git
#sudo git init --bare


#sudo gedit hooks/post-receive
#sudo chmod +x hooks/post-receive

#cd /home/www/flask_project
#sudo git init
#git remote add production root@10.4.41.141:/home/git/flask_project.git

#Make some changes to your code in the app.py file:
#sudo git config --global user.email "taosnever@gmail.com"

#sudo git push production master

sudo supervisorctl restart flask_project
