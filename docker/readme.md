# Orientation
리눅스 컨테이너를 사용해서 어플리케이션을 배포한 것을 **containerization** 이라고 함.

장점
Flexible : 아무리 복잡한 앱도 containerization 할 수 있다.
Lightweight : 호스트 커널을 공유
Portable : 로컬에서 만든걸 어디든 배포
Scalable : 컨테이너 레플리카를 자동으로 늘리거나 배포 
Stackable : 수직적으로 쌓을 수 있다.

## Images and containers
이미지는 executable package 인데 코드나 런타임, 라이브러리, 환경변수, configuation files 등이 모두 포함된 것.

컨테이너는 이미지의 런타임 인스턴스. 상태를 갖고 잇는 이미지 혹은 사용자 프로세스
`docker ps` 하면 지금 돌아가고 있는 컨테이너 목록을 볼 수 있음.

## Containers and Virtual machines
호스트의 자원을 container 들이 공유해서 사용.

vm은 호스트의 자원을 효율적으로 사용하지 못함.

## Recap and cheat sheet
```
## List Docker CLI commands
docker
docker container --help

## Display Docker version and info
docker --version
docker version
docker info

## Execute Docker image
docker run hello-world

## List Docker images
docker image ls

## List Docker containers (running, all, all in quiet mode)
docker container ls
docker container ls --all
docker container ls -aq
```

# Conatainers

## Dockerfile 정의하기
컨테이너 안의 환경이 어떻게 구성되어야 하는지를 정의.


## Recap and cheat sheet
```
docker build -t friendlyhello .  # Create image using this directory's Dockerfile
docker run -p 4000:80 friendlyhello  # Run "friendlyname" mapping port 4000 to 80
docker run -d -p 4000:80 friendlyhello         # Same thing, but in detached mode
docker container ls                                # List all running containers
docker container ls -a             # List all containers, even those not running
docker container stop <hash>           # Gracefully stop the specified container
docker container kill <hash>         # Force shutdown of the specified container
docker container rm <hash>        # Remove specified container from this machine
docker container rm $(docker container ls -a -q)         # Remove all containers
docker image ls -a                             # List all images on this machine
docker image rm <image id>            # Remove specified image from this machine
docker image rm $(docker image ls -a -q)   # Remove all images from this machine
docker login             # Log in this CLI session using your Docker credentials
docker tag <image> username/repository:tag  # Tag <image> for upload to registry
docker push username/repository:tag            # Upload tagged image to registry
docker run username/repository:tag                   # Run image from a registry
```


# Services

## Introduction
3 부에서는 어플리케이션을 확장하고 로그밸런싱을 활성화한다. 이렇게하려면 분산 응용 프로그램의 계층 구조에서 하나의 수준 높은 `service`로 이동해야 한다.

- Stack
- Services (you are here)
- Container (covered in part 2)

## About services
분산환경에서 각각의 앱을 `services`라고 부름.
services들은 사실 실제 배포 환경에서 컨테이너들이다. service는 하나의 이미지를 run한다. `service`의 scaling 작업은 많은 수의 컨테이너 인스턴스의 시스템 자원을 변경하는 것이다.

## Your first docker-compose.yml file
`docker-compose.yml` 파일은 도커 컨테이너들이 실제 배포 환경에서 어떻게 동작하는지에 대해 정의한 YAML 파일이다.


## Run your new load-balanced app
`docker stack deploy` 명령어를 사용하기 전에 알애ㅘ 같이 수행해야 한다.

```
docker swarm init
```

이제, run 해보자. app의 이름을 줘야되는데 아래는 getstartedlab 으로 주었다.
```
docker stack deploy -c docker-compose.yml getstartedlab
```

우리가 만든 하나의 service stack이 5개의 컨테이너 인스턴스에서 runnnig 중이다.
serviceID 를 얻기 위해 아래 명령어 수행.
```
docker service ls
```

서비스에서 돌고 있는 컨테이너 각각은 `task`라고 불린다. task들은 각각 unique한 ID를 갖고 있으며 `docker-compose.yml`에 정의한 `replicas`의 수 이다. task 목옥은 아래와 같이 볼 수 있다.
```
dockser service ps getstartedlab_web
```

task들은 service의 구분 없이 모두 볼 수도 있다.
```
docker container ls -q
```

## Scale the app
`docker-compose.yml` 을 변경하여 `replicas`의 수를 변경할 수 있다. 변경 후, 아래와 같은 명령어로 변경사항을 업데이트 해주어야 한다.
```
docker stack deploy -c docker-compose.yml getstartedlab
```
Docker는 전체 업데이트를 수행하며, stack을 떼어내거나 컨테이너를 죽일 필요가 없다.

## Take down the app and the swarm
앱을 죽이기 위해 `docker stack rm` 명령어를 날린다.
```
docker stack rm getstartedlab
```

swarm을 죽인다.
```
docker swarm leave --force
```


# Swarms

## Introduction
multiple machines에서 앱을 돌리기 위해, 클러스터에 배포하는 방법을 알아보자. swarm은 `Dockerized` 클러스터라고 부른다.


## Understanding Swarm clusters
swarm은 도커가 running 중이고 cluster에 참여된 장비들의 그룹이다. Swarm manager를 통해 클러스터에서 docker command를 사용할 수 있다. swarm 에 존재하는 장비들은 physical or virtual 이고 swarm에 합류한 이후에는 그 것들을 `node`로 참조된다.

Swarm manager들은 컨테이너를 운영하는 여러 전략을 사용할 수 있다. emptiest node를 사용하면 최소한의 컨테이너를 사용한다. 기본적으로는 각 장비에 하나의 특정한 컨테이너를 갖게 된다.이런 전략들을 사용하기 위해 compose file에 swarm manager를 구성한다.

`Swarm manager`는 swarm 안에서 command를 수행할 수 있는 단 하나에 machine이고, machine들을 swarm에 `worker`로 합류시키기 위해 인증을 해준다. Worker들은 컨테이너를 돌릴 수 있는 기본적인 리소스만 제공한다. 

지금까지는 single 머신에서만 했지만 swap mode로 변경하면, 현재 머신이 swarm manager가 된다. 


## Set up your swarm
swarm은 노드로 구성된다. `docker swarm init`을 하면 swarm manager가 되고, worker로 사용할 머신에서 `docker swarm join`을 하면 조인한다. 

### Create a cluster

vm 을 두대 만들고 시작하는데, 이건 로컬에서는 virtualbox로 한다.....

```
docker-machine ls
```
하면 머신목록을 조회할 수 있다.


# Stacks

## Introduction
distributed application

**Stack**은 dependency를 공유하는 상호 연관된 서비스들의 group이며 함께 scaled 되고 orchestrated 될 수 있다. single Stack은 전체 애플리케이션의 기능을 정의하고 조정한다.

