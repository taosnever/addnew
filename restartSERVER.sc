sudo supervisord -c /etc/supervisor/supervisord.conf
sudo supervisorctl -c /etc/supervisor/supervisord.conf
sudo pkill gunicorn
sudo supervisorctl reread
sudo supervisorctl update
sudo supervisorctl start flask_project