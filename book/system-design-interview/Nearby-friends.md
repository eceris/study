# 45 page. 
- Redis is an excellent choice because it provides super-fast read and write operations. It supports TTL, 

# 47page
- This puts a cap on the maximum amount of memory used. 
- The location data for each user is independent, and we can evenly spread the load among several Redis servers by sharding the location data based on user ID. 

- We assign a unique channel to every user who uses the “nearby friends” feature.

# 51page
- In this sense, a Pub/Sub server is stateful, and coordination with all subscribers to the server must be orchestrated to minimize service interruptions. 

- With stateful clusters, scaling up or down has some operational overhead and risks, so it should be done with careful planning. 


- 주의 사항
- When we resize a cluster, many channels will be moved to different servers on the
hash ring. When the service discovery component notifies all the WebSocket servers
of the hash ring update, there will be a ton of resubscription requests. 
- 대량의 재구독 이벤트동안, 몇몇 위치정보는 클라이언트에 의해 누락될수 있다. 
- 이런 잠재적 인터럽션으로 인해, 리사이징은 하루중 사용량이 적을때 진행되어야 함.


- 리사이징방법
- 그냥 새로운 링사이즈 정하고, 서버를 프로비저닝하면 됨.

