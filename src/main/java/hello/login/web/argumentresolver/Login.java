package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //필수로 넣어줘야함 파라미터에 넣을 것이다.라는 뜻
@Retention(RetentionPolicy.RUNTIME) //어플리케이션이 기동되어 있을때 계속 유지, 리플렉션 등을 활용할 수 있도록 런타임까지 에노테이션 정보가 남아있음
public @interface Login {

}
