���׸����� ����� Ÿ���� ���÷������ε� ������ ������

���� ��� new ArrayList<String>(); �� �ڵ�� ��Ÿ�� ������ TypeErasure �۾����� Ÿ�� ������ �Ҿ����.
�����δ� new ArrayList()�� �ٲ�.....

�ٵ� ���� �̰��� 
List<String> s = new ArrayList<String>();
Type t =  s.getClass().getGenericSuperClass();
ParameterizedType p = (ParameterizedType) t;
p.getActuralTypeArguments()[0]
�̷��� �ϸ� String�� �����´�.<sup>TypeErasure�۾��� �Ͼ�� ����</sup>