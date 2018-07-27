# JVM 메모리 구조
1. Heap = New + Old (new 연산자로 생성된 객체와 배열을 저장하는 영역으로 GC 대상이 되는 영역이다.)

New = Eden + Survivor1 + Survivor2
Old


Gc는 기본적으로 rootSet으로부터의 참조사슬에 포함되지 않은 객체를 정리하는데, 객체의 참조는 아래와 같다.
1. strongly reachable: root set으로부터 시작해서 어떤 reference object도 중간에 끼지 않은 상태로 참조 가능한 객체, 다시 말해, 객체까지 도달하는 여러 참조 사슬 중 reference object가 없는 사슬이 하나라도 있는 객체
2. softly reachable: strongly reachable 객체가 아닌 객체 중에서 weak reference, phantom reference 없이 soft reference만 통과하는 참조 사슬이 하나라도 있는 객체
3. weakly reachable: strongly reachable 객체도 softly reachable 객체도 아닌 객체 중에서, phantom reference 없이 weak reference만 통과하는 참조 사슬이 하나라도 있는 객체
4. phantomly reachable: strongly reachable 객체, softly reachable 객체, weakly reachable 객체 모두 해당되지 않는 객체. 이 객체는 파이널라이즈(finalize)되었지만 아직 메모리가 회수되지 않은 상태이다.
5. unreachable: root set으로부터 시작되는 참조 사슬로 참조되지 않는 객체

오직 SoftReferencce 객체로만 참조된 객체는 힙에 남아 있는 메모리의 크기와 해당 객체의 사용 빈도에 따라 GC 여부가 결정된다. 그래서 softly reachable 객체는 weakly reachable 객체와는 달리 GC가 동작할 때마다 회수되지 않으며 자주 사용될수록 더 오래 살아남게 된다
weakly reachable 객체는 특별한 정책에 의해 GC 여부가 결정되는 softly reachable 객체와는 달리 GC를 수행할 때마다 회수 대상이 된다. 앞서 설명한 것처럼 WeakReference 내의 참조가 null로 설정되고 weakly reachable 객체는 unreachable 객체와 마찬가지 상태가 되어 GC에 의해 메모리가 회수된다. 
 
 

2. Non-Heap = Perm (Class 메타정보, Method 메타정보, Static Object, 상수화된 String Object, Calss관련 배열 메타정보, JVM내부 객체와 최적화컴파일러(JIT)최적화 정보 등 포함)

# JVM Option 정리
| 이름 | 설명 | 비고 |
|:--------|:--------|:--------|
| **-Xms** |  초기 Heap Size (init, default 64m) ||
| **-Xmx** |최대 Heap Size (Max,  default 256m)||
| **-XX:NewSize** |최소 new size (객체가 생성되어 저장되는 초기공간의 Size로 Eden+Survivor 영역)||
| **-XX:MaxNewSize** |최대 new Size||
| **-XX:SurvivorRatio** |New/Survivor영역 비율 (n으로 지정시 Eden : Survivor = 1:n)||
| **-XX:NewRatio** |Young Gen과 Old Gen의 비율 (n으로 지정시 Young : Old = 1:n)||
| **-XX:+DisableExplicitGC** |System.gc() 콜을 무시||
| **-XX:+UseConcMarkWeepGC** |표준 gc가 아니나 Perm Gen영역도 gc하는 Concurrent Collertor를 사용||
| **-XX:+HeapDumpOnOutOfMemoryError** | OOM 발생시 HeapDump 생성 ||
| **-XX:HeapDumpPath=$CATALINA_BASE/logs** | HeapDump 생성 위치 ||
| **-XX:PermSize** |초기 PermSize| Java 8에서 옵션이 사라짐. |
| **-XX:MaxPermSize** |최대 PermSize| Java 8에서 옵션이 사라짐. |
| **-XX:+UseG1GC** | G1GC 쓰겠다.|  |



정리하면 java7  까지는
new / survive / old / perm /  native  로 구분했다면
java 8에서는
new / survive / old / metaspace 로 아키텍쳐가 변경되었고
기존의 perm에 저장되어 문제를 유발하던
static obect는 heap으로 옮겨서 GC 대상이 최대한 될 수 있도록 하고 (Final 로 해놓으면 나두 모름)

기타 정말 수정이 될 일 없어보는 정보는
Native(Metaspace) 로 몰아넣고 사이즈는 자동적으로 조정되도록 개선 되었다고 정리하면 되겠습니다.

