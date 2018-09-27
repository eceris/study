# firewalld
리눅스 커널 2.2 까지는 ipchains 라는 패킷 필터링/방화벽 프레임워크가 구현되어있었는데, 커널 2.4 부터는 유연하고 강력한 기능을 지닌 netfilter 로 프레임워크가 교체.

기존에 사용하던 iptables 은 사용하기가 어려운 단점이 있는데 RHEL/CentOS 7 부터는 방화벽을 firewalld 라는 데몬으로 교체.

따라서 명령줄에서는 firewalld-cmd 를 사용한다.

#### 방화벽 정책 재설정
```
firewall-cmd --reload
```
#### 방화벽 정책 영구 적용
```
firewall-cmd --permanent
```

> 만약 정책을 변경시 즉시 반영하고 재부팅시에도 유지하려면 firewall-cmd 를 두번 호출해야 한다.

### Network Zone
네트워크 구성과 영역을 설정으로 관리.

#### 기본 설정된 zone 목록
```
$ sudo firewall-cmd  --get-zones
```

#### zone 정책 목록
```
$ sudo firewall-cmd  --list-all-zones
```

#### 현재 활성화된 zone 
```
$ sudo firewall-cmd  --get-active-zone
```

#### 현재 zone 설정 보기
```
$ sudo firewall-cmd --list-all
```

#### 특정 zone 설정 보기
```
$ sudo firewall-cmd --list-all ZONENAME
```
#### 기본 zone 바꾸기
```
$ sudo firewall-cmd  --set-default-zone=dmz
```

#### zone 만들기

새로운 zone을 만든후에는 reload를 해야 변경된 정책이 반영.

```
$ sudo firewall-cmd --permanent --new-zone=webserver
```

### 사전 정의 서비스
firewalld 에는 방화벽 정책 변경시 용이하도록 많이 사용하는 서비스를 사전에 정의. 

#### 사전 정의 서비스 목록
```
$ sudo firewall-cmd --get-services
```

#### 서비스 추가
추가한 webserver 존에서는 http(80), https(443) 으로 접근을 허용해야 하므로 아래와 같이 추가
```
$ sudo firewall-cmd --permanent --zone=webserver --add-service=http
$ sudo firewall-cmd --permanent --zone=webserver --add-service=https
```

#### 포트 추가 
이미 정의된 서비스가 아닌 다른 포트를 사용하고 싶다면 아래와 같이 등록. 범위로 등록할 경우 `-` 구분자로 등록
```
$ sudo firewall-cmd --permanent --zone=webserver --add-port=9090-9100/tcp
```

#### 포트 삭제
방법은 포트 추가와 동일함.
```
$ sudo firewall-cmd --permanent --zone=webserver --remove-port=9090-9100/tcp
```

> 포트 추가/변경, IP 추가/변경은 --reload 옵션으로 firewalld 를 재실행해야 반영.

### 인터페이스 변경 및 ssh 서비스 추가

#### NIC마다 zone 추가 
```
$ sudo firewall-cmd --permanent --zone=webserver --change-interface=eth0
$ sudo firewall-cmd --permanent --zone=dmz --change-interface=eth1
```

#### 특정 ip에서만 접근가능하도록 추가
**--add-source=<source range\>/netmask** 옵션을 사용하여 허용할 IP 를 추가

아래 예는 dmz 에 ssh 서비스를 추가하고 내부 망(192.168.10.0/24) 에서만 ssh 가 연결 가능하도록 설정하고 webserver 존은 --remove-service 명령어로 ssh 서비스를 삭제

```
$ sudo firewall-cmd --permanent --zone=dmz --add-service=ssh
$ sudo firewall-cmd --permanent --zone=dmz --add-source=192.168.10.0/24
$ sudo firewall-cmd --permanent --zone=webserver --remove-service=ssh
```

> 참고 [링크](https://www.lesstif.com/pages/viewpage.action?pageId=43844015#firewalld%EB%B0%A9%ED%99%94%EB%B2%BD%EC%82%AC%EC%9A%A9-firewalld%EB%9E%80)
