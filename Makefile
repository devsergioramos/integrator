start-monitor:
	sudo chmod -R 777 ./monitor/data
	docker compose -f ./monitor/docker-compose.yaml up -d

stop-monitor:
	docker compose -f ./monitor/docker-compose.yaml down