#!/usr/bin/env bash
cd /home/ec2-user/bounsweAss7API
echo "Stopping Practice App"
(source ./venv/bin/activate &&
    ([[ ! -f practice_app.uwsgi.pid ]] && echo "Practice App Wasn't Running, Skipping Stop") ||
    (uwsgi --stop practice_app.uwsgi.pid && rm practice_app.uwsgi.pid && echo "Successfully stopped Practice App")
) || (>&2 echo "Failed to stop Practice App")