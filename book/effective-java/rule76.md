# 규칙 76. readObject 메서드는 방어적으로 구현하라.

[이전에 변경 가능한 private Date 필드들을 이용해 변경 불가능한 기간 클래스를 구현하였다.](rule39.md) 그 클래스를 직렬화 가능한 클래스로 만들고 싶다면..문제가 있다.

## readObject 메소드가 실질적으로는 public 생성자와 마찬가지이다.
바이트 스트림을 인자로 받는 생성자와 같아서 아래와 같은 작업이 필요하다.

코드를 예로 들면
```java
public class BogusPeriod {
    // 바이트 스트림을 꼭 실제 Period 객체로만 만들어 낼 수 있는 것은 아님.
    private static final byte[] serializedForm = new byte[] {...};
    
    public static void main(String[] args) {
        Period p = (Period) deserialize(serializedForm);
        System.out.println(p);
    }
    
    // 지정된 직렬화 형식을 준수하는 객체 생성
    private static Object deserialize(byte[] sf) {
        try {
            InputStream is = new ByteArrayInputStream(sf);
            ObjectInputStream ois = new ObjectInputStream(is);
            return ois.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
```

- 생성자와 마찬가지로 인자의 유효성 검사.
```java
private void readObject(ObjectInputStream ois) {
    s.defaultReadObject();
    
    if (start.compareTo(end) > 0) {
        throw new InvalidObjectException(start + " after " + end);
    }
}
```

- 필요하다면 인자를 방어적으로 복사.
```java
//방어적 복사와 불변식 검사를 모두 시행하는 readObject 메서드
private void readObject(ObjectInputStream ois) {
    s.defaultReadObject();
    
    // 모든 변경 가능한 필드를 방어적으로 복사
    start = new Date(start.getTime());
    end = new Date(end.getTime());
    
    // 불변식이 만족되는지 검사
    if (start.compareTo(end) > 0) {
        throw new InvalidObjectException(start + " after " + end);
    }
}
```

> readObject 메소드를 재정의하여 사용하지 않는 것이 좋다. 만약 재정의 해야 한다면, public 생성자를 구현할 때와 마찬가지로 신경써야 한다.
- 어떤 바이트 스트림이 주어지더라도 유효한 객체가 생성되도록 할 것.
- private 로 남아 있어야 하는 객체 참조 필드를 가진 클래스는 객체를 방어적으로 복사해야 함. 
- 불변식을 위반할 경우, InvalidObjectException 을 던져라. 불변식 검사는 방어적 복사 이후에~
- 직접적이건, 간접적이건, 재정의 가능 메서드를 호출하지 마라.